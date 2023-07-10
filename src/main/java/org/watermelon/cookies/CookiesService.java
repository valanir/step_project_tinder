package org.watermelon.cookies;

import jakarta.servlet.http.Cookie;
import org.watermelon.Logger;
import org.watermelon.User;
import org.watermelon.users.UsersDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CookiesService {
    private final CookiesDAO cookiesDAO = new CookiesDAO("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");

    private final UsersDAO usersDAO = new UsersDAO("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");

    protected CookiesService() {
    }


    //Метод возвращает общее кол-во печенек в БД
    protected Integer countTotalCookies() {
        try {
            Integer count = this.cookiesDAO.countTotalCookies();
            Logger.info("DB contains " + count + " cookies records");
            return count;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get total messages count");
            return null;
        }
    }

    //Метод возвращает общее кол-во печенек пользователя
    protected Integer countUserCookies(User user) {
        try {
            if (user.getId() != null || this.usersDAO.checkUserById(user.getId())) {
                Integer count = this.cookiesDAO.countUserCookies(user);
                Logger.info("Total amount of cookies of User ID: " + user.getId() + " : " + count);
                return count;
            } else {
                Logger.error("Attempt to get total cookies amount of user absent in DB");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get amount of cookies of User ID: " + user.getId());
            return null;
        }
    }

    //Метод возвращает общее кол-во печенек пользователя по ID
    protected Integer countUserCookies(Integer id) {
        try {
            if (id != null || this.usersDAO.checkUserById(id)) {
                Integer count = this.cookiesDAO.countUserCookies(id);
                Logger.info("Total amount of cookies of User ID: " + id + " : " + count);
                return count;
            } else {
                Logger.error("Attempt to get total cookies amount of user absent in DB");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get cookies amount of user ID: " + id);
            return null;
        }
    }

    //Метод возвращает логический результат наличия печеньки в БД
    protected boolean checkCookieDB(Cookie cookie) {
        try {
            boolean result = this.cookiesDAO.checkCookieDB(cookie);
            Logger.info("Presence of cookie: " + cookie.toString() + " checked in DB, result: " + result);
            return result;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to check presence of cookie: " + cookie.toString() + " in DB");
            return false;
        }
    }

    //Метод возвращает коллекцию печенек пользователя
    protected List<Cookie> getUserCookies(Integer userId) {
        List<Cookie> cookies = new ArrayList<>();
        try {
            if (this.usersDAO.checkUserById(userId)) {
                ResultSet result = this.cookiesDAO.getUserCookies(userId);
                while (result.next()) {
                    cookies.add(new Cookie(result.getString("name"), result.getString("value")));
                }
                Logger.info("Received users (ID:" + userId + ") cookies from DB");
                result.close();
                return cookies;
            } else {
                Logger.error("User with ID: " + userId + " is absent in DB");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get cookies from user ID: " + userId);
            return null;
        }
    }

    //Метод возвращает печеньку по ее имени
    protected Cookie getCookieByName(String name) {
        try {
            ResultSet resultSet = this.cookiesDAO.getCookieByName(name);
            if (resultSet.next()) {
                return new Cookie(resultSet.getString("name"), resultSet.getString("value"));
            } else {
                Logger.error("Cookie with name: " + name + " not found in DB");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get cookie with name: " + name);
            return null;
        }
    }

    //Метод возвращает id пользователя по печеньке
    protected Integer getUserIDbyCookie(Cookie cookie) {
        try {
            if (this.cookiesDAO.checkCookieDB(cookie)) {
                ResultSet resultSet = this.cookiesDAO.getUserIDbyCookie(cookie);
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    Logger.info("Received user ID: " + id + " by cookie " + cookie.getValue());
                    return id;
                } else {
                    Logger.error("No users with cookie: " + cookie.getValue() + " found");
                    return null;
                }
            } else {
                Logger.error("Attempt to search user ID by not existed cookie:" + cookie.getValue());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Сталась помилка при спробі отримати ID користувача який має печеньку: " + cookie.getValue());
            return null;
        }
    }

    //Метод добавляет запись о печеньке для пользователя
    protected void addCookie(Cookie cookie, User user) {
        try {
            if (this.usersDAO.checkUserById(user.getId())) {
                this.cookiesDAO.addCookie(cookie, user);
                Logger.info("User ID: " + user.getId() + " got cookie: " + cookie.getValue());
            } else Logger.error("Attemp to add cookie to not existed user!");
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to add cookie: " + cookie.getValue() + " to user ID: " + user.getId());
        }
    }

    //Метод добавляет запись о печеньке для пользователя
    protected void addCookie(Cookie cookie, Integer userId) {
        try {
            if (this.usersDAO.checkUserById(userId)) {
                this.cookiesDAO.addCookie(cookie, userId);
                Logger.info("User ID: " + userId + " got cookie: " + cookie.getValue());
            } else Logger.error("Attempt to add cookie to not existed user!");
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to add cookie: " + cookie.getValue() + " to user ID: " + userId);
        }
    }

    //Метод позволяет изменить/обновить печеньку пользователя
    protected void changeCookie(Cookie cookieOld, Cookie cookieNew, User user) {
        try {
            if (this.cookiesDAO.checkCookieDB(cookieOld)) {
                this.cookiesDAO.changeCookie(cookieOld, cookieNew, user);
                Logger.info("User ID: " + user.getId() + "has changed cookie (" + cookieOld.getValue() + "->" + cookieNew + ")");
            } else Logger.error("Attempt to modify not existed cookie: " + cookieOld.getValue());
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to modify cookie: " + cookieOld.getValue() + " od user ID: " + user.getId());
        }
    }

    //Метод позволяет изменить печеньку пользователя
    protected void changeCookie(Cookie cookieOld, Cookie cookieNew, Integer userID) {
        try {
            if (this.cookiesDAO.checkCookieDB(cookieOld)) {
                this.cookiesDAO.changeCookie(cookieOld, cookieNew, userID);
                Logger.info("User ID: " + userID + "has changed cookie (" + cookieOld.getValue() + "->" + cookieNew + ")");
            } else Logger.error("Attempt to modify not existed cookie: " + cookieOld.getValue());
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to modify cookie: " + cookieOld.getValue() + " od user ID: " + userID);
        }
    }

    //Метод удаляет все печеньки пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteAllUserCookies(User user) {
        try {
            if (this.usersDAO.checkUserById(user.getId())) {
                Integer result = this.cookiesDAO.deleteAllUserCookies(user);
                Logger.info("All cookies of user ID: " + user.getId() + " was deleted");
                return result;
            } else {
                Logger.error("Attempt to delete cookies of not existed User");
                return 0;
            }
        } catch (
                Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete all cookies of User ID: " + user.getId());
            return 0;
        }
    }

    //Метод удаляет все печеньки пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteAllUserCookie(Integer userID) {
        try {
            if (this.usersDAO.checkUserById(userID)) {
                Integer result = this.cookiesDAO.deleteAllUserCookies(userID);
                Logger.info("All cookies of user ID: " + userID + " was deleted");
                return result;
            } else {
                Logger.error("Attempt to delete cookies of not existed User");
                return 0;
            }
        } catch (
                Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete all cookies of User ID: " + userID);
            return 0;
        }
    }

    //Метод удаляет печеньку пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteCookie(Cookie cookie, User user) {
        try {
            if (this.usersDAO.checkUserById(user.getId())) {
                Integer result = this.cookiesDAO.deleteCookie(cookie, user);
                Logger.info("Cookie : " + cookie.getValue() + " was deleted from user ID: " + user.getId());
                return result;
            } else {
                Logger.error("Attempt to delete cookie from not existed User ID: " + user.getId());
                return 0;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete cookie: " + cookie.getValue() + ", from user ID: " + user.getId());
            return 0;
        }
    }

    //Метод удаляет печеньку пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteCookie(Cookie cookie, Integer userID) {
        try {
            if (this.usersDAO.checkUserById(userID)) {
                Integer result = this.cookiesDAO.deleteCookie(cookie, userID);
                Logger.info("Cookie : " + cookie.getValue() + " was deleted from user ID: " + userID);
                return result;
            } else {
                Logger.error("Attempt to delete cookie from not existed User ID: " + userID);
                return 0;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete cookie: " + cookie.getValue() + ", from user ID: " + userID);
            return 0;
        }
    }
}
