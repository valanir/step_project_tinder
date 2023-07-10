package org.watermelon;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import org.watermelon.services.UsersGenerator;
import org.watermelon.servlets.*;
import java.util.EnumSet;

import org.eclipse.jetty.servlet.ServletHolder;
import org.watermelon.servlets.DynamicServlet;
import org.watermelon.servlets.MessagesServlet;
import org.watermelon.servlets.StaticContentServlet;


// http://localhost:8080/users
// http://localhost:8080/liked
// http://localhost:8080/messages/{id}
// http://localhost:8080/login

public class ServerApp {
    private static final EnumSet<DispatcherType> ft = EnumSet.of(DispatcherType.REQUEST);
    public static void main(String[] args) throws Exception {
        Server tinder = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler();


        //UsersGenerator ug = new UsersGenerator();
        //ug.initUsers();   //запустити один раз




        //handler.addServlet(TestServlet.class, "/users");
        //UsersProfileServlet usersProfileServlet = new UsersProfileServlet();
        //usersProfileServlet.init();
        //handler.addServlet(UsersProfileServlet.class, "/users");
        DynamicServlet dynamicServlet = new DynamicServlet();
        dynamicServlet.initUserProfiles();
        handler.addServlet(StaticContentServlet.class, "/resources/*");
        handler.addServlet(StaticContentServlet.class, "/messages/resources/*");
        handler.addServlet(UserServlet.class, "/users");
        handler.addServlet(LoginServlet.class, "/login");
        handler.addServlet(RegisterServlet.class, "/register");
        handler.addServlet(LikedServlet.class, "/liked");
        handler.addServlet(LogoutServlet.class, "/logout");
        handler.addServlet(MessagesServlet.class, "/messages/*");
        handler.addServlet(MainServlet.class, "/");
        //привет костыль
        handler.addServlet(CssServlet1.class, "/resources/html/css/bootstrap.min.css");
        handler.addServlet(CssServlet2.class, "/resources/html/css/style.css");


        handler.addFilter(CheckCookieFilter.class, "/", ft);
        handler.addFilter(CheckCookieFilter.class, "/users", ft);
        handler.addFilter(CheckCookieFilter.class, "/liked", ft);
        tinder.setHandler(handler);

        tinder.start();
        tinder.join();
    }
}
