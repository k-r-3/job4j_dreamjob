package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    @Test
    public void whenCreatePost() throws IOException, ServletException {
        Store store = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PowerMockito.when(req.getParameter("id")).thenReturn("0");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");
        new PostServlet().doPost(req, resp);
        Post result = store.findAllPosts().iterator().next();
        Assert.assertThat(result.getName(), is("n"));
        Assert.assertThat(result.getDescription(), is("d"));
    }

    @Test
    public void whenEditPost() throws IOException, ServletException {
        Store store = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PowerMockito.when(req.getParameter("id")).thenReturn("1");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");
        new PostServlet().doPost(req, resp);
        Post result = store.findAllPosts().iterator().next();
        Assert.assertThat(result.getName(), is("n"));
        Assert.assertThat(result.getDescription(), is("d"));
        PowerMockito.when(req.getParameter("edit")).thenReturn("edit");
        PowerMockito.when(req.getParameter("id")).thenReturn("1");
        PowerMockito.when(req.getParameter("description")).thenReturn("d1");
        new PostServlet().doPost(req, resp);
        Post changed = store.findAllPosts().iterator().next();
        Assert.assertThat(changed.getName(), is("n"));
        Assert.assertThat(changed.getDescription(), is("d1"));
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
        when(req.getRequestDispatcher("/post/edit.jsp")).thenReturn(disp);
        new PostServlet().doGet(req, resp);
        verify(req).getRequestDispatcher("/post/edit.jsp" );
    }
}