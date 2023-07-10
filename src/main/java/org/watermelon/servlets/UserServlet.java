package org.watermelon.servlets;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.watermelon.Like;
import org.watermelon.Profile;
import org.watermelon.ServerApp;
import org.watermelon.User;
import org.watermelon.cookies.CookiesController;
import org.watermelon.like.LikesController;
import org.watermelon.users.UsersController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;


public class UserServlet extends HttpServlet {
    private UsersController usersController = new UsersController();
    private CookiesController cookiesController = new CookiesController();
    LikesController likesController = new LikesController();
    private List<String> userLog = new ArrayList<>();
    private static Map<Integer, Profile> userProfiles;
    private Integer userIdCounter = 0;
    private User currentUser; // юзер который залогиненый
    private Profile showProfile;

    public UserServlet() throws SQLException {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Cookie c = Optional.ofNullable(req.getCookies())
                        .flatMap(cc -> Arrays.stream(cc).filter(c1 -> c1.getName().equals("id")).findFirst())
                        .get();



        //получить юзера через куки
        currentUser = this.usersController.getUserById(this.cookiesController.getUserIDbyCookie(c));

        /*URL u = UserServlet.class.getClassLoader().getResource("html/0");
        String s = u.getFile();
        Path p = Paths.get(s.substring(0, s.length() - 1));*/



        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
        config.setClassForTemplateLoading(UserServlet.class.getClass(), "/html");
        TemplateLoader ldr = new ClassTemplateLoader(UserServlet.class, "/html");
        config.setTemplateLoader(ldr);
        //config.setDirectoryForTemplateLoading(new File("src/main/resources/html"));


        //Получаем список id пользователей
        List<Integer> ids = this.usersController.getAllUsersId();




        if (userIdCounter == ids.size()) {
            userIdCounter = 0;
            resp.sendRedirect("/liked");
        };

        //пропуск профиля который равен залогиненому юзеру
        if (ids.get(userIdCounter).equals(currentUser.getId())) userIdCounter++;

        if (userIdCounter == ids.size()) {
            userIdCounter = 0;
            resp.sendRedirect("/liked");
        };

        userLog.add(req.getPathInfo());


        showProfile = usersController.getProfileById(ids.get(userIdCounter));

        userIdCounter++;




        Map<String, Object> data = new HashMap<>();
        data.put("username", showProfile.getNickName());
        data.put("image", showProfile.getPhotoSource());
        try (PrintWriter writer = resp.getWriter()) {
            config.getTemplate("like-page.html").process(data, writer);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("result").equals("yes")){
            System.out.println("true");
            likesController.addLike(new Like(currentUser.getId(), showProfile.getId()));
        }
        userLog.add(req.getPathInfo());
        this.doGet(req, resp);
    }
}
