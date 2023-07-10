package org.watermelon.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.watermelon.User;
import org.watermelon.cookies.CookiesController;
import org.watermelon.users.UsersController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegisterServlet extends HttpServlet {
    CookiesController cookiesController = new CookiesController();
    UsersController usersController = new UsersController();

    public RegisterServlet() throws SQLException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = 0;
        String login = req.getParameter("login");;
        String password = req.getParameter("password");
        String photoLink = "https://gnatkovsky.com.ua/wp-content/uploads/2015/02/130220152333.jpg";
        String nickName = login;
        Cookie cookie = new Cookie("id", UUID.randomUUID().toString());

        if (!usersController.checkUserByLogin(login)){
            id = usersController.addUser(new User(login, photoLink, login,password));
            cookiesController.addCookie(cookie, id);
            resp.addCookie(cookie);
            resp.sendRedirect("/users");
        } resp.sendRedirect("/register");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream inputStream = LoginServlet.class.getClassLoader().getResourceAsStream("html/register.html");
        Stream<String> result = new BufferedReader(new InputStreamReader(inputStream))
                .lines();
        List<String> strings = result.collect(Collectors.toList());
        try(PrintWriter w = resp.getWriter()){
            strings.forEach(l -> w.println(l));
        }
    }
}
