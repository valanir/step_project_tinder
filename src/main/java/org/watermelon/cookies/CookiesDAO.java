package org.watermelon.cookies;

import jakarta.servlet.http.Cookie;
import org.watermelon.*;


import java.sql.*;


public class CookiesDAO {

    protected Connection connection;

    private CookiesDAO() throws SQLException {
    }

    protected CookiesDAO(String sourceDB, String login, String password) {
        try {
            this.connection = DriverManager.getConnection(sourceDB, login, password);
        } catch (Exception e) {
            Logger.error("Unable to establish connection: " + e.toString());
        }
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void getConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");
        } catch (Exception e) {
            Logger.error("Unable to establish connection: " + e.toString());
        }
    }


    //Метод возвращает общее кол-во печенек в БД
    protected Integer countTotalCookies() throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(value) from public.cookies")) {
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во печенек пользователя
    protected Integer countUserCookies(User user) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(value) from public.cookies WHERE id = ?")) {
            stm.setInt(1, user.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во печенек пользователя по ID
    protected Integer countUserCookies(Integer id) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(value) from public.cookies WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает логический результат наличия печеньки в БД
    protected boolean checkCookieDB(Cookie cookie) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT * from public.cookies "
                + "WHERE value = ? AND name = ?")) {
            stm.setString(1, cookie.getValue());
            stm.setString(2, cookie.getName());
            ResultSet resultSet = stm.executeQuery();
            boolean check = resultSet.next();
            this.connection.close();
            return check;
        } catch (Exception e) {
            System.out.println("Сталась помилка при спробі перевірки наявності печеньки у БД");
            this.connection.close();
            return false;
        }
    }

    //Метод возвращает коллекцию печенек пользователя по его id
    protected ResultSet getUserCookies(Integer userId) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT name, value from public.cookies WHERE id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, userId);
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати печеньки користувача ID: " + userId + " з ДБ");
            this.connection.close();
            return null;
        }
    }

    //Метод возвращает коллекцию печенек пользователя по его профилю
    protected ResultSet getUserCookies(User user) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT name, value from public.cookies WHERE id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, user.getId());
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати печеньки користувача ID: " + user.getId() + " з ДБ");
            return null;
        }
    }

    //Метод возвращает id пользователя по печеньке
    protected ResultSet getUserIDbyCookie(Cookie cookie) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT id from public.cookies WHERE value = ? AND name= ? ");
            stm.setString(1, cookie.getValue());
            stm.setString(2, cookie.getName());
            return stm.executeQuery();
        }
        catch (Exception e) {
            System.out.println("Сталась помилка при спробі отримати ID користувача який має печеньку: " + cookie.getValue());
            return null;
        }
    }

    //Метод возвращает печеньку по ее имени
    protected ResultSet getCookieByName(String name) throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT id, name, value from public.cookies WHERE name = ?");
        stm.setString(1, name);
        return stm.executeQuery();
    }

    //Метод добавляет запись о печеньке пользователю
    protected void addCookie(Cookie cookie, User user) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("INSERT INTO public.cookies (id, name, value) "
                + "VALUES (?, ?, ?)")) {
            stm.setInt(1, user.getId());
            stm.setString(2, cookie.getName());
            stm.setString(3, cookie.getValue());
            stm.executeUpdate();
            System.out.println(" Користувач ID: " + user.getId() + " отримав печеньку у БД");
            this.connection.close();
        }
    }

    //Метод добавляет запись о печеньке пользователю
    protected void addCookie(Cookie cookie, Integer userId) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("INSERT INTO public.cookies (id, name, value) "
                + "VALUES (?, ?, ?)")) {
            stm.setInt(1, userId);
            stm.setString(2, cookie.getName());
            stm.setString(3, cookie.getValue());
            stm.executeUpdate();
            System.out.println(" Користувач ID: " + userId + " отримав печеньку у БД");
            this.connection.close();
        }
    }

    //Метод позволяет изменить печеньку пользователя
    protected void changeCookie(Cookie cookieOld, Cookie cookieNew, User user) throws SQLException {
        this.getConnection();
        if (this.checkCookieDB(cookieOld)) {
            try (PreparedStatement stm = connection.prepareStatement("UPDATE public.cookies SET value = ? WHERE value = ? AND id = ? AND name = ?")) {
                stm.setString(1, cookieNew.getValue());
                stm.setString(2, cookieOld.getValue());
                stm.setInt(3, user.getId());
                stm.setString(4, cookieOld.getName());
                stm.executeUpdate();
                this.connection.close();
            }
        } else {
            System.out.println("Не вдалося змінити печеньку користувача з ID: " + user.getId() + ", можливо він її вже з'їв...");
            this.connection.close();
        }
    }

    //Метод позволяет изменить печеньку пользователя
    protected void changeCookie(Cookie cookieOld, Cookie cookieNew, Integer userID) throws SQLException {
        this.getConnection();
        if (this.checkCookieDB(cookieOld)) {
            try (PreparedStatement stm = connection.prepareStatement("UPDATE public.cookies SET value = ? WHERE value = ? AND id = ? AND name = ?")) {
                stm.setString(1, cookieNew.getValue());
                stm.setString(2, cookieOld.getValue());
                stm.setInt(3, userID);
                stm.setString(4, cookieOld.getName());
                stm.executeUpdate();
                this.connection.close();
            }
        } else {
            System.out.println("Не вдалося змінити печеньку користувача з ID: " + userID + ", можливо він її вже з'їв...");
            this.connection.close();
        }
    }

    //Метод удаляет все печеньки пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteAllUserCookies(User user) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.cookies WHERE id = ?")) {
            stm.setInt(1, user.getId());
            Integer deleted = stm.executeUpdate();
            this.connection.close();
            return deleted;
        }
    }

    //Метод удаляет все печеньки пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteAllUserCookies(Integer userID) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.cookies WHERE id = ?")) {
            stm.setInt(1, userID);
            Integer deleted = stm.executeUpdate();
            this.connection.close();
            return deleted;
        }
    }

    //Метод удаляет печеньку пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteCookie(Cookie cookie, User user) throws SQLException {
        this.getConnection();
        if (this.checkCookieDB(cookie))
            try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.cookies WHERE id = ? AND value = ? AND name = ?")) {
                stm.setInt(1, user.getId());
                stm.setString(2, cookie.getValue());
                stm.setString(3, cookie.getName());
                Integer deleted = stm.executeUpdate();
                this.connection.close();
                return deleted;
            }
        else {
            System.out.println("Не вдалося видалити печеньку користувача з ID: " + user.getId() + ", можливо він її вже з'їв...");
            this.connection.close();
            return 0;
        }
    }

    //Метод удаляет печеньку пользователя из БД и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteCookie(Cookie cookie, Integer userID) throws SQLException {
        this.getConnection();
        if (this.checkCookieDB(cookie))
            try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.cookies WHERE id = ? AND value = ? AND name = ?")) {
                stm.setInt(1, userID);
                stm.setString(2, cookie.getValue());
                stm.setString(3, cookie.getName());
                Integer deleted = stm.executeUpdate();
                this.connection.close();
                return deleted;
            }
        else {
            System.out.println("Не вдалося видалити печеньку користувача з ID: " + userID + ", можливо він її вже з'їв...");
            this.connection.close();
            return 0;
        }
    }
}
