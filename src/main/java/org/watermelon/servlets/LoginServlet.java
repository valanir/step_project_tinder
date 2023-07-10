package org.watermelon.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.watermelon.ServerApp;
import org.watermelon.cookies.CookiesController;
import org.watermelon.users.UsersController;

import java.io.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.jetty.util.Loader.getResource;

public class LoginServlet extends HttpServlet {
    UsersController usersController = new UsersController();
    CookiesController cookiesController = new CookiesController();

    public LoginServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        InputStream inputStream = LoginServlet.class.getClassLoader().getResourceAsStream("html/login.html");
        Stream<String> result = new BufferedReader(new InputStreamReader(inputStream))
                .lines();
        List<String> strings = result.collect(Collectors.toList());


        try(PrintWriter w = resp.getWriter()){
            strings.forEach(l -> w.println(l));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String login = req.getParameter("login");
        String pass = req.getParameter("password");
        if (this.usersController.getUserByLoginPass(login,pass) != null){
            Cookie cookie = this.cookiesController.getUserCookies(usersController.getUserByLoginPass(login, pass).getId()).get(0);

            resp.addCookie(cookie);
            resp.sendRedirect("/users");
        }else {resp.sendRedirect("/login");}
    }
}
