package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.MemStore;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        request.setAttribute("id", id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/photoUpload.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pattern ext = Pattern.compile("(.*)(\\.[A-Za-z]+)");
        String name = request.getParameter("name");
        ServletContext context = this.getServletConfig().getServletContext();
        File repo = (File) context.getAttribute("javax.servlet.context.tempdir");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(repo);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(request);
            File folder = new File("c:\\images\\");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    Matcher m = ext.matcher(item.getName());
                    String extension = "";
                    while (m.find()) {
                        extension = m.group(2);
                    }
                    File photo = new File(folder + File.separator + name + extension);
                    Candidate candidate = MemStore.instOf().findCandById(Integer.parseInt(name));
                    candidate.setPhoto(photo.getName());
                    try (FileOutputStream out = new FileOutputStream(photo)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/candidate/candidates.do");
    }
}
