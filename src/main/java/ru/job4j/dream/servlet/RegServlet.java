package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Objects;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("error", "заполните все поля!");
            request.getRequestDispatcher("reg.jsp").forward(request, response);
            return;
        }
        User user = PsqlStore.instOf().findByEmail(email);
        if (Objects.isNull(user.getEmail())) {
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            PsqlStore.instOf().save(user);
            HttpSession sc = request.getSession();
            sc.setAttribute("user", user);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            String message = String.format("пользователь с email %s уже зарегестрирован", email);
            request.setAttribute("error", message);
            request.getRequestDispatcher("reg.jsp").forward(request, response);
        }
    }
}
