package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HttpSession sc = req.getSession();
        if ("root@local".equals(email) && "root".equals(password)) {
            User admin = PsqlStore.instOf().findByEmail(email);
            System.out.println(admin);
            sc.setAttribute("user", admin);
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            User user = PsqlStore.instOf().findByEmail(email);
            if (Objects.nonNull(user.getPassword())) {
                if (user.getPassword().equals(password) &&
                        user.getEmail().equals(email)) {
                    sc.setAttribute("user", user);
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                }
            } else {
                req.setAttribute("error", "Не верный email или пароль");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        }
    }
}