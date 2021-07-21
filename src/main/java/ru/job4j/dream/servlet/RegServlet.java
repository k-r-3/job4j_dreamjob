package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.setName(request.getParameter("name"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        PsqlStore.instOf().save(user);
        HttpSession sc = request.getSession();
        sc.setAttribute("user", user);
        request.setAttribute("email", user.getEmail());
        request.setAttribute("password", user.getPassword());
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
