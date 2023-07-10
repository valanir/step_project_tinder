package org.watermelon.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticContentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String urlSource = req.getPathInfo();
        if (urlSource.startsWith("/")) {
            urlSource = urlSource.substring(1);
        }
        int indexR = urlSource.indexOf("html");
        Path source = Path.of("src/main/resources", urlSource.substring(indexR));
        try (ServletOutputStream sos  = resp.getOutputStream()) {
            Files.copy(source, sos);
        }
    }
}
