package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PsqlStore.class, HttpSession.class})
public class AuthServletTest {

    @Test
    public void whenAuth() throws ServletException, IOException {
        Store store = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);
        PowerMockito.mockStatic(HttpSession.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        User admin = new User();
        admin.setId(1);
        admin.setEmail("root@local");
        admin.setPassword("root");
        admin.setName("Admin");
        store.save(admin);
        PowerMockito.when(req.getParameter("email")).thenReturn("root@local");
        PowerMockito.when(req.getParameter("password")).thenReturn("root");
        HttpSession sc = mock(HttpSession.class);
        PowerMockito.when(req.getSession()).thenReturn(sc);
        new AuthServlet().doPost(req, resp);
        verify(resp).sendRedirect(req.getContextPath() + "/index.jsp");
        verify(req, never()).getRequestDispatcher("login.jsp");
    }

    @Test
    public void whenGet() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher disp = mock(RequestDispatcher.class);
        when(req.getParameter(any(String.class))).thenReturn("");
        when(req.getRequestDispatcher("login.jsp")).thenReturn(disp);
        new AuthServlet().doGet(req, resp);
        verify(req).getRequestDispatcher("login.jsp");
    }
}