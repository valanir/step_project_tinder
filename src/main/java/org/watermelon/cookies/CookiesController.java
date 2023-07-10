package org.watermelon.cookies;

import jakarta.servlet.http.Cookie;
import org.watermelon.Logger;
import org.watermelon.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CookiesController {

    private final CookiesService cookiesService = new CookiesService();


    public CookiesController() {
    }

    //Метод возвращает общее кол-во печенек в БД
    public Integer countTotalCookies() {
        return this.cookiesService.countTotalCookies();
    }

    //Метод возвращает общее кол-во печенек пользователя
    public Integer countUserCookies(User user) {
        return this.cookiesService.countUserCookies(user);
    }

    //Метод возвращает общее кол-во печенек пользователя по ID
    public Integer countUserCookies(Integer userID) {
        return this.cookiesService.countUserCookies(userID);
    }

    //Метод возвращает логический результат наличия печеньки в БД
    public boolean checkCookieDB(Cookie cookie) {
        return this.cookiesService.checkCookieDB(cookie);
    }

    //Метод возвращает коллекцию печенек пользователя
    public List<Cookie> getUserCookies(Integer userId) {
        return this.cookiesService.getUserCookies(userId);
    }

    //Метод возвращает печеньку по ее имени
    public Cookie getCookieByName(String name) {
        return this.cookiesService.getCookieByName(name);
    }

    //Метод возвращает id пользователя по печеньке
    public Integer getUserIDbyCookie(Cookie cookie) {
        return this.cookiesService.getUserIDbyCookie(cookie);
    }

    //Метод добавляет запись о печеньке для пользователя
    public void addCookie(Cookie cookie, User user) {
        this.cookiesService.addCookie(cookie, user);
    }

    //Метод добавляет запись о печеньке для пользователя
    public void addCookie(Cookie cookie, Integer userId) {
        this.cookiesService.addCookie(cookie, userId);
    }

    //Метод позволяет изменить/обновить печеньку пользователя
    public void changeCookie(Cookie cookieOld, Cookie cookieNew, User user) {
        this.cookiesService.changeCookie(cookieOld, cookieNew, user);
    }

    //Метод позволяет изменить печеньку пользователя
    protected void changeCookie(Cookie cookieOld, Cookie cookieNew, Integer userID) {
        this.cookiesService.changeCookie(cookieOld, cookieNew, userID);
    }

    //Метод удаляет все печеньки пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    public Integer deleteAllUserCookies(User user) {
        return this.cookiesService.deleteAllUserCookies(user);
    }

    //Метод удаляет все печеньки пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    public Integer deleteAllUserCookie(Integer userID) {
        return this.cookiesService.deleteAllUserCookie(userID);
    }

    //Метод удаляет печеньку пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    public Integer deleteCookie(Cookie cookie, User user) {
        return this.cookiesService.deleteCookie(cookie, user);
    }

    //Метод удаляет печеньку пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    public Integer deleteCookie(Cookie cookie, Integer userID) {
        return this.cookiesService.deleteCookie(cookie, userID);
    }
}
