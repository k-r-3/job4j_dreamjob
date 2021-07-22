package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        try {
            PsqlStore.instOf().save(user);
            HttpSession sc = request.getSession();
            sc.setAttribute("user", user);
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (ConstraintViolationException e) {
            request.setAttribute("error", e);
            request.getRequestDispatcher("reg.jsp").forward(request, response);
        }
    }
}
