package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class CandidateServletTest {

    @Test
    public void whenCreatePost() throws IOException, ServletException {
        Store store = MemStore.instOf();
        Collection<City> list = MemStore.instOf().findAllCity();
        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PowerMockito.when(req.getParameter("id")).thenReturn("0");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("city")).thenReturn("1");
        new CandidateServlet().doPost(req, resp);
        Candidate result = store.findAllCandidates().iterator().next();
        Assert.assertThat(result.getName(), is("n"));
    }

    @Test
    public void whenGet() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession sc = mock(HttpSession.class);
        RequestDispatcher disp = mock(RequestDispatcher.class);
        when(req.getParameter("edit")).thenReturn("edit");
        PowerMockito.when(req.getSession()).thenReturn(sc);
        when(req.getParameter("id")).thenReturn("1");
        when(req.getRequestDispatcher("/candidate/edit.jsp")).thenReturn(disp);
        new CandidateServlet().doGet(req, resp);
        verify(req).getRequestDispatcher("/candidate/edit.jsp");
    }
}