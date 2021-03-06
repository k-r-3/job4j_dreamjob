package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PhotoDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Candidate candidate = PsqlStore.instOf().findCandById(id);
        candidate.setPhoto(null);
        PsqlStore.instOf().save(candidate);
        for (File file : new File("c:\\images\\").listFiles()) {
            if (file.getName().startsWith(String.valueOf(id))) {
                Files.delete(file.toPath());
            }
        }
        response.sendRedirect(request.getContextPath() + "/candidate/candidates.do");
    }
}
