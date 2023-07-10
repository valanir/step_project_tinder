package org.watermelon.users;

import org.watermelon.Logger;
import org.watermelon.User;

import java.sql.*;

public class UsersDAO {

    protected Connection connection;

    private UsersDAO() throws SQLException {
    }

    public UsersDAO(String sourceDB, String login, String password) {
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

    //Метод возвращает кол-во записей пользователей в БД
    protected Integer countUsers() throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id) from public.users");
        ResultSet result = stm.executeQuery();
        result.next();
        Integer count = result.getInt("count");
        result.close();
        this.connection.close();
        return count;
    }

    //Метод возвращает значение ID последней внесенной записи в БД
    protected Integer getLastUserId() throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT MAX(id) from public.users");
        ResultSet result = stm.executeQuery();
        result.next();
        Integer lastId = result.getInt("max");
        result.close();
        this.connection.close();
        return lastId;
    }

    //Метод возвращает коллекцию id пользователей
    protected ResultSet getAllUsersId() throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT id from public.users",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stm.executeQuery();
    }

    //Метод возвращает все доступные записи о профилях пользователей из БД
    protected ResultSet getAllUserProfiles() throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * from public.users",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stm.executeQuery();
    }

    //Метод возвращает все доступные записи о профилях из БД
    protected ResultSet getAllProfiles() throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT id, nickname, photo from public.users",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stm.executeQuery();
    }

    //Метод возвращает только профили с фото
    protected ResultSet getAllProfilesForView() throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT id, nickname, photo from public.users where " +
                        "photo != 'https://gnatkovsky.com.ua/wp-content/uploads/2015/02/130220152333.jpg'",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stm.executeQuery();
    }

    protected ResultSet getProfileById(Integer id) throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT id, nickname, photo from public.users WHERE id = ?",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stm.setInt(1, id);
        return stm.executeQuery();
    }

    //Метод возвращает запись профиля по никнейму
    protected ResultSet getProfileByNickname(String nickName) throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT id, nickname, photo from public.users WHERE nickname = ?",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stm.setString(1, nickName);
        return stm.executeQuery();
    }

    //Метод возвращает следующее значение Id, которое будет присвоено новой записи. При вызове смещает указатель на +1, поэтому особо не эффективен
   /* public Integer getSeqId() throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT nextval('profiles_id_seq')");
        ResultSet result = stm.executeQuery();
        result.next();
        return result.getInt("nextval");
    }*/

    //Метод возвращает запись профиля пользователя по ID
    protected ResultSet getUserById(Integer id) throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * from public.users WHERE id = ?",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stm.setInt(1, id);
        return stm.executeQuery();
    }

    //Метод возвращает запись профиля пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    public ResultSet getUserByNickname(String nickName) throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * from public.users WHERE nickname = ?",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stm.setString(1, nickName);
        return stm.executeQuery();
    }

    //Метод возвращает запись профиля пользователя по логину-паролю
    protected ResultSet getUserByLoginPass(String login, String password) throws SQLException {
        this.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * from public.users WHERE login = ? AND password = ?");
        stm.setString(1, login);
        stm.setString(2, String.valueOf(password.hashCode()));
        return stm.executeQuery();
    }

    //Метод проверяет наличие записи пользователя в БД по ID
    public boolean checkUserById(Integer id) throws SQLException {
        if (id != null) {
            this.getConnection();
            try (PreparedStatement stm = connection.prepareStatement("SELECT id from public.users WHERE id = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                stm.setInt(1, id);
                ResultSet result = stm.executeQuery();
                this.connection.close();
                return result.next();
            }
        } else {
            System.out.println("id = null, так працювати не буде");
            this.connection.close();
            return false;
        }
    }

    //Метод проверяет наличие записи о пользователе в БД по логину (нужно гарантировать уникальность логина!)
    protected boolean checkUserByLogin(String login) throws SQLException {
        if (!login.isEmpty()) {
            this.getConnection();
            try (PreparedStatement stm = connection.prepareStatement("SELECT login from public.users WHERE login = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                stm.setString(1, login);
                ResultSet result = stm.executeQuery();
                this.connection.close();
                return result.next();
            }
        } else {
            System.out.println("Логін пустий, так працювати не буде");
            this.connection.close();
            return false;
        }
    }

    //Метод проверяет наличие такого пароля в БД (нужен при регистрации чтобы не дублировать пароли)
    protected boolean checkUserByPassword(String password) throws SQLException {
        if (!password.isEmpty()) {
            this.getConnection();
            try (PreparedStatement stm = connection.prepareStatement("SELECT password from public.users WHERE password = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                stm.setString(1, String.valueOf(password.hashCode()));
                ResultSet result = stm.executeQuery();
                this.connection.close();
                return result.next();
            }
        } else {
            System.out.println("Логін пустий, так працювати не буде");
            this.connection.close();
            return false;
        }
    }

    //Метод проверяет пароль у пользователя с логином (проверка при входе)
    protected boolean checkUsersPassword(String login, String password) throws SQLException {
        if (this.checkUserByLogin(login)) {
            this.getConnection();
            try (PreparedStatement stm = connection.prepareStatement("SELECT * from public.users WHERE login = ? AND password = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                stm.setString(1, login);
                stm.setString(2, String.valueOf(password.hashCode()));
                ResultSet result = stm.executeQuery();
                this.connection.close();
                return result.next();
            }
        } else {
            System.out.println("Користувача із таким логіном нема!");
            this.connection.close();
            return false;
        }
    }

    //Метод проверяет наличие записи о пользователе в БД по никнейму
    protected boolean checkUserByNickname(String nickName) throws SQLException {
        if (!nickName.isEmpty()) {
            this.getConnection();
            try (PreparedStatement stm = connection.prepareStatement("SELECT * from public.users WHERE nickname = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                stm.setString(1, nickName);
                ResultSet result = stm.executeQuery();
                this.connection.close();
                return result.next();
            }
        } else {
            System.out.println("Нікнейм пустий, так працювати не буде");
            this.connection.close();
            return false;
        }
    }

    //Метод добавляет/обновляет запись о профиле пользователя в БД и возвращает ID записи
    protected Integer addUser(User user) throws SQLException {
        if (user.getId() == null || !this.checkUserById(user.getId())) {
            this.getConnection();
            try (PreparedStatement stm = this.connection.prepareStatement("INSERT INTO public.users (login, password, nickname, photo) VALUES (?, ?, ?, ?)", new String[]{"id"})) {
                stm.setString(1, user.getLogin());
                stm.setString(2, String.valueOf(user.getPassword().hashCode()));
                stm.setString(3, user.getNickName());
                stm.setString(4, user.getPhotoSource());
                stm.executeUpdate();
                ResultSet rid = stm.getGeneratedKeys();
                if (rid.next()) {
                    Logger.info("User Profile ID: " + rid.getInt(1) + " was added to DB");
                    Integer id = rid.getInt(1);
                    this.connection.close();
                    return id;
                } else {
                    Logger.error("Something went wrong in attempt to add user with nickname: " + user.getNickName() + " to DB");
                    this.connection.close();
                    return null;
                }
            }
        } else if (user.getId() != null && this.checkUserById(user.getId())) {
            System.out.println("Профайл користувача з ID: " + user.getId() + " вже присутній у базі! Інформація буде оновлена");
            this.getConnection();
            try (PreparedStatement stm = connection.prepareStatement("UPDATE public.users SET login = ?, password = ?, nickname = ?, photo = ? WHERE id = ?")) {
                stm.setString(1, user.getLogin());
                stm.setString(2, user.getPassword());
                stm.setString(3, user.getNickName());
                stm.setString(4, user.getPhotoSource());
                stm.setInt(5, user.getId());
                stm.executeUpdate();
                Logger.info("User Profile ID: " + user.getId() + " was updated in DB");
                this.connection.close();
                return user.getId();
            }
        }
        return null;
    }

    //Метод удаляет запись профиля пользователя из БД
    protected Integer deleteUser(User user) throws SQLException {
        if (user.getId() != null) {
            this.getConnection();
            try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.users WHERE id = ?")) {
                stm.setInt(1, user.getId());
                System.out.println("Профайл користувача з ID: " + user.getId() + " видалено");
                Integer deleted = stm.executeUpdate();
                this.connection.close();
                return deleted;
            }
        } else {
            this.connection.close();
            return 0;
        }
    }

    //Метод удаляет запись профиля по ID из БД и возвращает кол-во удаленных записей
    protected Integer deleteUser(Integer id) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.users WHERE id = ?")) {
            stm.setInt(1, id);
            System.out.println("Профайл користувача з ID: " + id + " видалено");
            Integer deleted = stm.executeUpdate();
            this.connection.close();
            return deleted;
        }
    }

    //Метод удаляет запись профиля по ID из БД (нужно гарантировать уникальность никнеймов!)
    protected Integer deleteUser(String nickName) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.users WHERE nickname = ?")) {
            stm.setString(1, nickName);
            System.out.println("Профайл користувача з нікнеймом: " + nickName + " видалено");
            Integer deleted = stm.executeUpdate();
            this.connection.close();
            return deleted;
        }
    }
}
