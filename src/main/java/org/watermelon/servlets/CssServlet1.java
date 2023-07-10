package org.watermelon.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CssServlet1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream inputStream = LoginServlet.class.getClassLoader().getResourceAsStream("html/css/bootstrap.min.css");
        Stream<String> result = new BufferedReader(new InputStreamReader(inputStream))
                .lines();
        List<String> strings = result.collect(Collectors.toList());


        try(PrintWriter w = resp.getWriter()){
            strings.forEach(l -> w.println(l));
        }
    }
}
