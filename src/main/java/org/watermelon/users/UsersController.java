package org.watermelon.users;

import org.watermelon.Profile;
import org.watermelon.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UsersController {
    private final UsersService usersService = new UsersService();

    public UsersController()  {
    }

    //Метод возвращает кол-во записей о профилях пользователей БД
    public Integer countUsers() {
        return this.usersService.countUsers();
    }

    //Метод возвращает коллекцию id пользователей
    public List<Integer> getAllUsersId(){ return this.usersService.getAllUsersId();}

    //Метод возвращает коллекцию всех профайлов пользователей (не путать с объектами класса User)
    public Map<Integer, Profile> getAllProfiles() {
        return this.usersService.getAllProfiles();
    }

    //Метод возвращает только профили с фото
    public Map<Integer, Profile> getAllProfilesForView() {
        return this.usersService.getAllProfilesForView();
    }

    //Метод возвращает профиль пользователя по ID из БД (возвращает null, если такого в БД нет или произошла ошибка коммуникации с БД)
    public Profile getProfileById(Integer id) {
        return this.usersService.getProfileById(id);
    }

    //Метод возвращает профиль пользователя по никнейму из БД (возвращает null, если такого в БД нет, или произошла ошибка коммуникации с БД)
    public Profile getProfileByNickName(String nickname) {
        return this.usersService.getProfileByNickName(nickname);
    }

    //Метод возвращает значение ID последней внесенной записи в БД
    public Integer getLastUserId() {
        return this.usersService.getLastUserId();
    }

    //Метод возвращает профиль пользователя по ID из БД (возвращает null, если такого в БД нет или произошла ошибка коммуникации с БД)
    public User getUserById(Integer id) {
        return this.usersService.getUserById(id);
    }

    //Метод возвращает профиль пользователя по никнейму из БД (возвращает null, если такого в БД нет, или произошла ошибка коммуникации с БД)
    public User getUserByNickName(String nickname) {
        return this.usersService.getUserByNickName(nickname);
    }

    //Метод возвращает запись профиля пользователя по логину-паролю
    public User getUserByLoginPass(String login, String password) {
        return this.usersService.getUserByLoginPass(login, password);
    }

    //Метод проверяет наличие записи о пользователе в БД по ID
    public boolean checkUserById(Integer id) {
        return this.usersService.checkUserById(id);
    }

    //Метод проверяет наличие записи в БД по никнейму
    public boolean checkUserByNickname(String nickName)  {
        return this.usersService.checkUserByNickname(nickName);
    }

    //Метод проверяет наличие записи о пользователе в БД по логину (нужно гарантировать уникальность логина!)
    public boolean checkUserByLogin(String login){
        return this.usersService.checkUserByLogin(login);
    }

    //Метод проверяет наличие такого пароля в БД (нужен при регистрации чтобы не дублировать пароли)
    public boolean checkUserByPassword(String password) {
        return this.usersService.checkUserByPassword(password);
    }

    //Метод проверяет пароль у пользователя с логином (проверка при входе)
    protected boolean checkUsersPassword(String login, String password)  {
        return this.usersService.checkUsersPassword(login, password);
    }

    //Метод добавляет/обновляет профиль в БД и возвращает id новой записи (логи пишутся в SQLApp)
    public Integer addUser(User user) {
        return this.usersService.addUser(user);
    }

    //Метод удаляет запись переданного профайла из возвращает кол-во удаленных записей
    public Integer deleteUser(User user) {
        return this.usersService.deleteUser(user);
    }

    //Метод удаляет запись профайла c переданным id из возвращает кол-во удаленных записей
    public Integer deleteUser(Integer id) {
        return this.usersService.deleteUser(id);
    }

    //Метод удаляет запись профайла c переданным nickname из возвращает результат успешности операции
    public Integer deleteUser(String nickName) {
        return this.usersService.deleteUser(nickName);
    }
}

