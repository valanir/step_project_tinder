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
import org.watermelon.Message;
import org.watermelon.Profile;
import org.watermelon.cookies.CookiesController;
import org.watermelon.messages.MessagesController;
import org.watermelon.users.UsersController;
import org.watermelon.users.UsersService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MessagesServlet extends HttpServlet {

    private List<Message> rawChat = new ArrayList<>();

    private Integer authorID;
    private Integer interlocutorID;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_31);
        config.setDefaultEncoding(String.valueOf(StandardCharsets.UTF_8));
        config.setClassForTemplateLoading(UserServlet.class.getClass(), "/html");
        TemplateLoader ldr = new ClassTemplateLoader(UserServlet.class, "/html");
        config.setTemplateLoader(ldr);
        resp.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

        UsersController usersController = new UsersController();
        MessagesController messagesController = new MessagesController();
        CookiesController cookiesController = new CookiesController();

        //Определяем id автора сообщения
        Cookie[] currentUserCookie = req.getCookies();
        Optional<Cookie> cookieId = Optional.ofNullable(currentUserCookie)
                .flatMap(cuc -> Arrays.stream(cuc).filter(uc -> uc.getName().equals("id")).findFirst());
        if (cookieId.isPresent()) {
            authorID = cookiesController.getUserIDbyCookie(cookieId.get());
        } else resp.sendRedirect("/login"); //Если печенек нет, то отправляем их получать, хотя это работа фильтра

        //Определяем id получателя
        this.interlocutorID = Integer.parseInt(req.getPathInfo().substring(1));

        Profile interlocutor = usersController.getProfileById(interlocutorID);
        Profile author = usersController.getUserById(authorID);
        if (authorID != 0) {
            rawChat = messagesController.getChat(authorID, interlocutorID);
        }

        List<String> chat = new ArrayList<>();
        for (Message message : rawChat) {
            if (message.getSourceID() == authorID) {
                chat.add(this.addOutMessageToChat(author, message));
            } else {
                chat.add(this.addIncomingMessageToChat(interlocutor, message));
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("chat", chat);
        data.put("receiver", interlocutor.getNickName());
        data.put("id", interlocutorID);

        try (PrintWriter writer = resp.getWriter()) {
            config.getTemplate("chat2.ftl").process(data, writer);
        } catch (
                TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MessagesController messagesController = new MessagesController();
        req.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        if (!req.getParameter("message").isEmpty()) {
            messagesController.addMessage(new Message(authorID, interlocutorID, req.getParameter("message")));
        }
        resp.sendRedirect("/messages/" + interlocutorID);
    }

    private String addIncomingMessageToChat(Profile intercolutor, Message message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a");
        if (this.checkLinkInMessage(message)) {
            List<String> fragmentedMessage = this.getTextWithLink(message.getMessage(), this.getLinkPrefix(message));
            return new String(" <li class=\"receive-msg float-left mb-2\">\n" +
                    "<div class=\"sender-img\">\n <img src=\"" + intercolutor.getPhotoSource() +
                    "\" class=\"float-left\">\n </div>\n <div class=\"receive-msg-desc float-left ml-2\">\n" +
                    "<p class=\"bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded\">\n" + fragmentedMessage.get(0) +
                    " <a href=\"" + fragmentedMessage.get(1) + "/\" class=\"text-dark rounded\" target=\"_blank\"><u>"
                    + fragmentedMessage.get(1) + "</u></a> " + fragmentedMessage.get(2) + "\n" +
                    "</p>\n <span class=\"receive-msg-time\">" + intercolutor.getNickName() + ", "
                    + LocalDateTime.ofInstant(message.getTimestamp().toInstant(), ZoneId.systemDefault()).format(formatter)
                    + "</span>\n </div>\n </li>");
        } else {
            return new String(" <li class=\"receive-msg float-left mb-2\">\n" +
                    "<div class=\"sender-img\">\n <img src=\"" + intercolutor.getPhotoSource() +
                    "\" class=\"float-left\">\n </div>\n <div class=\"receive-msg-desc float-left ml-2\">\n" +
                    "<p class=\"bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded\">\n" + message.getMessage() + "\n" +
                    "</p>\n <span class=\"receive-msg-time\">" + intercolutor.getNickName() + ", "
                    + LocalDateTime.ofInstant(message.getTimestamp().toInstant(), ZoneId.systemDefault()).format(formatter)
                    + "</span>\n </div>\n </li>");
        }
    }

    private String addOutMessageToChat(Profile author, Message message) {
        //Корявая реализация счетчика времени с момента отправки сообщения (нужно поправить офсеты и оптимизировать массив кода)
        LocalDateTime presentTime = LocalDateTime.now();
        LocalDateTime messageTime = LocalDateTime.ofInstant(message.getTimestamp().toInstant(), ZoneId.systemDefault());
        long diffTime = presentTime.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now())) -
                messageTime.toEpochSecond(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
        int time = 0;
        String s = "";

        if (diffTime > 31536000) {
            time = presentTime.getYear() - messageTime.getYear();
            s = " years ago";
        } else if (diffTime > 86400) {
            time = (int) (diffTime / 86400);
            s = " days ago";
        } else if (diffTime < 86400 && diffTime > 3600) {
            time = (int) (diffTime / 3600);
            s = " hours ago";
        } else if (diffTime < 3600 && diffTime > 60) {
            time = (int) (diffTime / 60);
            s = " mins ago";
        } else if (diffTime < 60) {
            time = (int) diffTime;
            s = " secs ago";
        }

        if (this.checkLinkInMessage(message)) {
            List<String> fragmentedMessage = this.getTextWithLink(message.getMessage(), this.getLinkPrefix(message));
            return new String("<li class=\"send-msg float-right mb-2\">\n" +
                    "<p class=\"pt-1 pb-1 pl-2 pr-2 m-0 rounded\">\n" + fragmentedMessage.get(0) +
                    " <a href=\"" + fragmentedMessage.get(1) + "/\" class=\"text-dark rounded\" target=\"_blank\"><u>"
                    + fragmentedMessage.get(1) + "</u></a> " + fragmentedMessage.get(2) + "\n" + "</p>\n <span class=\"send-msg-time\">" + time + s + "</span>\n </li>");
        } else {
            return new String("<li class=\"send-msg float-right mb-2\">\n" +
                    "<p class=\"pt-1 pb-1 pl-2 pr-2 m-0 rounded\">\n" + message.getMessage() + "\n" + "</p>\n <span class=\"send-msg-time\">" + time + s + "</span>\n </li>");
        }
    }

    private boolean checkLinkInMessage(Message message) {
        return message.getMessage().contains("http:") || message.getMessage().contains("https:") || message.getMessage().contains("www.");
    }

    private String getLinkPrefix(Message message) {
        String httpPrefix = "http:";
        String httpsPrefix = "https:";
        String wwwPrefix = "www.";
        if (checkLinkInMessage(message)) {
            if (message.getMessage().contains(httpPrefix))
                return httpPrefix;
            else if (message.getMessage().contains(httpsPrefix))
                return httpsPrefix;
            else if (message.getMessage().contains(wwwPrefix))
                return wwwPrefix;
        }
        return null;
    }

    private List<String> getTextWithLink(String message, String prefix) {
        String text1;
        String link;
        String text2;
        int linkStart;
        int linkEnd;

        linkStart = message.indexOf(prefix);
        text1 = message.substring(0, linkStart);
        link = message.substring(linkStart);
        linkEnd = link.indexOf(" ");
        if (linkEnd != -1) {
            text2 = link.substring(linkEnd);
            link = link.substring(0, linkEnd);
        } else text2 = "";
        List<String> fragmentedMessage = new ArrayList<>();
        fragmentedMessage.add(text1);
        fragmentedMessage.add(link);
        fragmentedMessage.add(text2);
        return fragmentedMessage;
    }
}
