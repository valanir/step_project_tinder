package org.watermelon.users;

import org.watermelon.Logger;
import org.watermelon.Profile;
import org.watermelon.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersService {
    private final UsersDAO usersDAO = new UsersDAO("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");
    private Map<Integer, User> userProfiles = new HashMap<>(); //ключ - id

    protected UsersService() {
    }

    //Метод возвращает кол-во записей о профилях пользователей в БД
    protected Integer countUsers() {
        try {
            Integer count = this.usersDAO.countUsers();
            Logger.info(count + " User profiles at the moment present in DB");
            return count;
        } catch (Exception exception) {
            Logger.error("Got " + exception.toString() + " when try count all user profiles in DB");
            return null;
        }
    }

    //Метод возвращает значение ID последней внесенной записи о профиле пользователя в БД
    protected Integer getLastUserId() {
        try {
            Integer lastId = this.usersDAO.getLastUserId();
            Logger.info("Last recorded user ID: " + lastId);
            return lastId;
        } catch (Exception exception) {
            Logger.error("Got " + exception.toString() + " when try to get last user ID");
            return null;
        }
    }

    //Метод возвращает коллекцию id пользователей
    protected List<Integer> getAllUsersId() {
        List<Integer> id = new ArrayList<>();
        try {
            ResultSet result = this.usersDAO.getAllUsersId();
            while (result.next()) {
                id.add(result.getInt("id"));
            }
            result.last();
            Logger.info(result.getRow() + " user IDs was downloaded from DB");
            result.close();
            return id;
        } catch (Exception e) {
            Logger.error("Got" + e.toString() + " whe try to get all user profiles id from DB");
            return null;
        }
    }

    //Метод помещает в поле класса коллекцию профилей пользователей из БД с id профиля в качестве ключа
    private void getAllUserProfiles() {
        try {
            ResultSet result = this.usersDAO.getAllUserProfiles();
            while (result.next()) {
                this.userProfiles.put(result.getInt("id"),
                        new User(result.getInt("id"), result.getString("nickname"),
                                result.getString("photo"), result.getString("login"),
                                result.getString("password")));
            }
            result.last();
            Logger.info(result.getRow() + " user profiles was downloaded from DB");
            result.close();
        } catch (Exception e) {
            Logger.error("Got" + e.toString() + " whe try to get all user profiles from DB");
        }
    }

    //Метод возвращает коллекцию профилей из БД с id профиля в качестве ключа
    protected Map<Integer, Profile> getAllProfiles() {
        try {
            ResultSet result = this.usersDAO.getAllProfiles();
            Map<Integer, Profile> profiles = new HashMap<>();
            while (result.next()) {
                profiles.put(result.getInt("id"),
                        new Profile(result.getInt("id"), result.getString("nickname"), result.getString("photo")));
            }
            result.last();
            Logger.info(result.getRow() + " profiles was downloaded from DB");
            result.close();
            return profiles;
        } catch (Exception e) {
            Logger.error("Got" + e.toString() + " whe try to get all profiles from DB");
            return null;
        }
    }

    //Метод возвращает только профили с фото
    protected Map<Integer, Profile> getAllProfilesForView() {
        try {
            ResultSet result = this.usersDAO.getAllProfilesForView();
            Map<Integer, Profile> profiles = new HashMap<>();
            while (result.next()) {
                profiles.put(result.getInt("id"),
                        new Profile(result.getInt("id"), result.getString("nickname"), result.getString("photo")));
            }
            result.last();
            Logger.info(result.getRow() + " profiles was downloaded from DB");
            result.close();
            return profiles;
        } catch (Exception e) {
            Logger.error("Got" + e.toString() + " whe try to get all profiles from DB");
            return null;
        }
    }

    //Метод возвращает профиль пользователя по ID из БД (возвращает null, если такого в БД нет или произошла ошибка коммуникации с БД)
    protected Profile getProfileById(Integer id) {
        try {
            ResultSet resultSet = this.usersDAO.getProfileById(id);
            if (resultSet.next()) {
                Profile result = new Profile(resultSet.getInt("id"), resultSet.getString("nickname"), resultSet.getString("photo"));
                Logger.info("Profile ID: " + resultSet.getInt("id") + " was downloaded from DB");
                resultSet.close();
                return result;
            } else {
                Logger.info("Info about profile ID: " + resultSet.getInt("id") + " is absent in DB");
                resultSet.close();
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get profile with id: " + id + " from DB");
            return null;
        }
    }

    //Метод возвращает профиль пользователя по никнейму из БД (возвращает null, если такого в БД нет, или произошла ошибка коммуникации с БД)
    protected Profile getProfileByNickName(String nickname) {
        try {
            ResultSet resultSet = this.usersDAO.getProfileByNickname(nickname);
            if (resultSet.next()) {
                Profile result = new Profile(resultSet.getInt("id"), resultSet.getString("nickname"), resultSet.getString("photo"));
                Logger.info("Profile with nickname: " + resultSet.getString("nickname") + " was downloaded from DB");
                resultSet.close();
                return result;
            } else {
                Logger.info("Info about profile with nickname: " + resultSet.getString("nickname") + " is absent in DB");
                resultSet.close();
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get profile with nickname: " + nickname + " from DB");
            return null;
        }
    }

    //Метод возвращает профиль пользователя по ID из БД (возвращает null, если такого в БД нет или произошла ошибка коммуникации с БД)
    protected User getUserById(Integer id) {
        try {
            ResultSet resultSet = this.usersDAO.getUserById(id);
            if (resultSet.next()) {
                User result = new User(resultSet.getInt("id"), resultSet.getString("nickname"),
                        resultSet.getString("photo"), resultSet.getString("login"),
                        resultSet.getString("password"));
                Logger.info("User profile ID: " + resultSet.getInt("id") + " was downloaded from DB");
                resultSet.close();
                return result;
            } else {
                Logger.info("Info about user profile ID: " + resultSet.getInt("id") + " is absent in DB");
                resultSet.close();
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get user profile with id: " + id + " from DB");
            return null;
        }
    }

    //Метод возвращает профиль пользователя по никнейму из БД (возвращает null, если такого в БД нет, или произошла ошибка коммуникации с БД)
    protected User getUserByNickName(String nickname) {
        try {
            ResultSet resultSet = this.usersDAO.getUserByNickname(nickname);
            if (resultSet.next()) {
                User result = new User(resultSet.getInt("id"), resultSet.getString("nickname"),
                        resultSet.getString("photo"), resultSet.getString("login"),
                        resultSet.getString("password"));
                Logger.info("User profile with nickname: " + resultSet.getString("nickname") + " was downloaded from DB");
                resultSet.close();
                return result;
            } else {
                Logger.info("Info about user profile with nickname: " + resultSet.getString("nickname") + " is absent in DB");
                resultSet.close();
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get user profile with nickname: " + nickname + " from DB");
            return null;
        }
    }

    //Метод возвращает запись профиля пользователя по логину-паролю
    protected User getUserByLoginPass(String login, String password) {
        try {
            ResultSet resultSet = this.usersDAO.getUserByLoginPass(login, password);
            if (resultSet.next()) {
                User result = new User(resultSet.getInt("id"), resultSet.getString("nickname"),
                        resultSet.getString("photo"), login, password);
                Logger.info("User profile with login: " + resultSet.getString("login") + " was downloaded from DB");
                resultSet.close();
                return result;
            } else {
                Logger.info("Info about user profile with login: " + resultSet.getString("login") + " is absent in DB");
                resultSet.close();
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get user profile with login: " + login + " from DB");
            return null;
        }
    }

    //Метод проверяет наличие записи в БД по ID
    protected boolean checkUserById(Integer id) {
        if (id != null || id == 0) {
            try {
                boolean result = this.usersDAO.checkUserById(id);
                Logger.info("User profile with ID: " + id + " checked, status: " + result);
                return result;
            } catch (Exception exception) {
                Logger.error("Got " + exception.toString() + " when try to check user profile with ID:" + id);
                return false;
            }
        } else {
            Logger.info("Attempt to search user profile with null/0 id");
            return false;
        }
    }

    //Метод проверяет наличие записи в БД по никнейму
    protected boolean checkUserByNickname(String nickName) {
        if (nickName != null) {
            try {
                boolean result = this.usersDAO.checkUserByNickname(nickName);
                Logger.info("User profile with nickname: " + nickName + " checked, status: " + result);
                return result;
            } catch (Exception exception) {
                Logger.error("Got " + exception.toString() + " when try to check user profile with nickname:" + nickName);
                return false;
            }
        } else {
            Logger.info("Attempt to search user profile with null nickname");
            return false;
        }
    }

    //Метод проверяет наличие записи о пользователе в БД по логину (нужно гарантировать уникальность логина!)
    protected boolean checkUserByLogin(String login) {
        if (login != null) {
            try {
                boolean result = this.usersDAO.checkUserByLogin(login);
                Logger.info("User profile with login: " + login + " checked, status: " + result);
                return result;
            } catch (Exception exception) {
                Logger.error("Got " + exception.toString() + " when try to check user profile with login: " + login);
                return false;
            }
        } else {
            Logger.info("Attempt to search user profile with null login");
            return false;
        }
    }

    //Метод проверяет наличие такого пароля в БД (нужен при регистрации чтобы не дублировать пароли)
    protected boolean checkUserByPassword(String password) {
        if (password != null) {
            try {
                boolean result = this.usersDAO.checkUserByPassword(password);
                Logger.info("Password: " + password + " checked in DB, status: " + result);
                return result;
            } catch (Exception exception) {
                Logger.error("Got " + exception.toString() + " when try to check presence of password: " + password + " in DB");
                return false;
            }
        } else {
            Logger.info("Attempt to check null password");
            return false;
        }
    }

    //Метод проверяет пароль у пользователя с логином (проверка при входе)
    protected boolean checkUsersPassword(String login, String password) {
        if (this.checkUserByLogin(login)) {
            try {
                boolean result = this.usersDAO.checkUsersPassword(login, password);
                Logger.info("Login-Password combination: " + login + "-" + password + " checked in DB, status: " + result);
                return result;
            } catch (Exception exception) {
                Logger.error("Got " + exception.toString() + " when try to check login-password: " + login + "-" + password + " in DB");
                return false;
            }
        } else {
            Logger.info("User with login: " + login + " is absent in DB");
            return false;
        }
    }

    //Метод добавляет/обновляет профиль в БД и возвращает id новой записи
    protected Integer addUser(User user) {
        try {
            return usersDAO.addUser(user);
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when tried to add user profile with nickname " + user.getNickName());
            return null;
        }
    }

    //Метод удаляет запись переданного профайла из возвращает кол-во удаленных записей
    protected Integer deleteUser(User user) {
        try {
            if (usersDAO.checkUserById(user.getId())) {
                Integer count = usersDAO.deleteUser(user);
                Logger.info("User profile ID:" + user.getId() + " was deleted from DB as " + count + " record(s)");
                return count;
            } else {
                Logger.info("User profile ID: " + user.getId() + " is absent in DB");
                return 0;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete user profile with ID: " + user.getId());
            return 0;
        }
    }

    //Метод удаляет запись профайла c переданным id из возвращает кол-во удаленных записей
    protected Integer deleteUser(Integer id) {
        try {
            if (usersDAO.getUserById(id).next()) {
                Integer count = usersDAO.deleteUser(id);
                Logger.info("User profile ID:" + id + " was deleted from DB as " + count + " record(s)");
                return count;
            } else {
                Logger.info("User profile ID: " + id + " is absent in DB");
                return 0;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete user profile with ID: " + id);
            return 0;
        }
    }

    //Метод удаляет запись профайла c переданным nickname из возвращает результат успешности операции
    protected Integer deleteUser(String nickName) {
        if (!nickName.isEmpty()) {
            try {
                if (usersDAO.checkUserByNickname(nickName)) {
                    Integer count = usersDAO.deleteUser(nickName);
                    Logger.info("User profile with nickname: " + nickName + " was deleted from DB as " + count + " record(s)");
                    return count;
                } else {
                    Logger.info("User profile with nickname: " + nickName + " is absent in DB");
                    return 0;
                }
            } catch (Exception e) {
                Logger.error("Got " + e.toString() + " when try to delete user profile with nickname: " + nickName);
                return 0;
            }
        } else {
            Logger.info("Attempt to delete user profile with empty nickname");
            return 0;
        }
    }
}

