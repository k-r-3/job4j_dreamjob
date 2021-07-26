package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class RegServletTest {

    @Test
    public void whenReg() throws ServletException, IOException {
        Store store = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession sc = mock(HttpSession.class);
        RequestDispatcher disp = mock(RequestDispatcher.class);
        PowerMockito.when(req.getParameter("name")).thenReturn("name");
        PowerMockito.when(req.getParameter("email")).thenReturn("e@mail.com");
        PowerMockito.when(req.getParameter("password")).thenReturn("password");
        PowerMockito.when(req.getSession()).thenReturn(sc);
        PowerMockito.when(req.getRequestDispatcher("login.jsp")).thenReturn(disp);
        new RegServlet().doPost(req, resp);
        assertThat(store.findByEmail("e@mail.com").getName(), is("name"));
        verify(req).getRequestDispatcher("login.jsp");
    }
}