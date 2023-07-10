package org.watermelon.servlets;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.watermelon.User;
import org.watermelon.cookies.CookiesController;
import org.watermelon.like.LikesController;
import org.watermelon.users.UsersController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class LikedServlet extends HttpServlet {
    UsersController usersController = new UsersController();
    LikesController likesController = new LikesController();
    CookiesController cookiesController = new CookiesController();
    User currentUser;

    public LikedServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie c = Optional.ofNullable(req.getCookies())
                .flatMap(cc -> Arrays.stream(cc).filter(c1 -> c1.getName().equals("id")).findFirst()
                ).get();

        //получить юзера через куки
        currentUser = usersController.getUserById(cookiesController.getUserIDbyCookie(c));

        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
        config.setClassForTemplateLoading(UserServlet.class.getClass(), "/html");
        TemplateLoader ldr = new ClassTemplateLoader(UserServlet.class, "/html");
        config.setTemplateLoader(ldr);
        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));


        ArrayList<User> userList = new ArrayList<>();

        likesController.getLikesFromUser(currentUser.getId())
                .forEach(l -> userList.add(usersController.getUserById(l.getIdTarget())));

        Map<String, Object> data = new HashMap<>();
        data.put("user", userList);


        try (PrintWriter writer = resp.getWriter()) {
            config.getTemplate("people-list.ftl").process(data, writer);
        } catch (
                TemplateException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
