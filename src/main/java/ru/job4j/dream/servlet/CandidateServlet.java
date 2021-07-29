package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String edit = req.getParameter("edit");
        String path = edit != null ? "/candidate/edit.jsp" : "/candidate/candidates.jsp";
        req.setAttribute("user", req.getSession().getAttribute("user"));
        if (edit == null) {
            req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
            req.setAttribute("cand_city", PsqlStore.instOf().cityOfCandidate());
        }
        String id = req.getParameter("id");
        req.setAttribute("id", id);
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Candidate candidate = PsqlStore.instOf().findCandById(Integer.parseInt(req.getParameter("id")));
        if (Objects.isNull(candidate)) {
            candidate = new Candidate();
        }
        String cityName = req.getParameter("city");
        int cityId = PsqlStore.instOf()
                .findAllCity().stream()
                .filter(c -> c.getName().equals(cityName))
                .findFirst()
                .get().getId();
        candidate.setCityId(cityId);
        candidate.setName(req.getParameter("name"));
        PsqlStore.instOf().save(candidate);
        resp.sendRedirect(req.getContextPath() + "/candidate/candidates.do");
    }
}
