package org.watermelon.like;

import org.watermelon.*;
import org.watermelon.users.UsersDAO;

import java.sql.*;
import java.util.Objects;

public class LikesDAO {

    protected Connection connection;

    private UsersDAO usersDAO = new UsersDAO("jdbc:postgresql://localhost:5432/webstep", "postgres", "pg123456");

    private LikesDAO() throws SQLException {
    }

    protected LikesDAO(String sourceDB, String login, String password) {
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


    //Метод возвращает общее кол-во лайков в БД
    protected Integer countTotalLikes() throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id_from) from public.likes")) {
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по профилю
    protected Integer countLikesFromUser(User user) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id_from) from public.likes WHERE id_from= ?")) {
            stm.setInt(1, user.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по id
    protected Integer countLikesFromUser(Integer id) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id_from) from public.likes WHERE id_from= ?")) {
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    protected Integer countLikesFromUser(String nickName) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id_from) from public.likes WHERE id_from= ?")) {
            ResultSet temp = this.usersDAO.getUserByNickname(nickName);
            temp.next();
            Profile profile = new Profile(temp.getInt("id"), temp.getString("nickname"), temp.getString("photo"));
            stm.setInt(1, profile.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во лайков пользователю по профилю
    protected Integer countLikesToUser(User user) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id_to) from public.likes WHERE id_to= ?")) {
            stm.setInt(1, user.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по id
    protected Integer countLikesToUser(Integer id) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id_to) from public.likes WHERE id_to= ?")) {
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    protected Integer countLikesToUser(String nickName) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(id_to) from public.likes WHERE id_to= ?")) {
            ResultSet temp = this.usersDAO.getUserByNickname(nickName);
            temp.next();
            Profile profile = new Profile(temp.getInt("id"), temp.getString("nickname"), temp.getString("photo"));
            stm.setInt(1, profile.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает логический результат наличия лайка в БД
    protected boolean checkLikeDB(Like like) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT * from public.likes "
                + "WHERE id_from= ? AND id_to")) {
            stm.setInt(1, like.getIdSource());
            stm.setInt(2, like.getIdTarget());
            ResultSet resultSet = stm.executeQuery();
            boolean check = resultSet.next();
            this.connection.close();
            return check;
        } catch (Exception e) {
            System.out.println("Сталась помилка при спробі перевірки наявності лайка у БД");
            this.connection.close();
            return false;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователем по его id
    protected ResultSet getLikesFromUser(Integer id) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * from public.likes WHERE id_from = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, id);
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати лайки користувача ID: " + id + " з ДБ");
            return null;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователем по его профилю
    protected ResultSet getLikesFromUser(User user) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * from public.likes WHERE id_from = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, user.getId());
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати лайки користувача ID: " + user.getId() + " з ДБ");
            this.connection.close();
            return null;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователю по его id
    protected ResultSet getLikesToUser(Integer id) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * from public.likes WHERE id_to = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, id);
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати лайки користувачу з ID: " + id + " з ДБ");
            this.connection.close();
            return null;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователю по его id
    protected ResultSet getLikesToUser(User user) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * from public.likes WHERE id_to = ?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, user.getId());
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати лайки користувачу з ID: " + user.getId() + " з ДБ");
            this.connection.close();
            return null;
        }
    }

    //Метод добавляет запись о лайке source пользователя к dest в БД, бросает исключения если id отправителя и адресата совпадают
    protected void addLike(Like like) throws SQLException, LikeException {
        this.getConnection();
        if (!Objects.equals(like.getIdSource(), like.getIdTarget())) {
            try (PreparedStatement stm = connection.prepareStatement("INSERT INTO public.likes (id_from, id_to) "
                    + "VALUES (?, ?)")) {
                stm.setInt(1, like.getIdSource());
                stm.setInt(2, like.getIdTarget());
                stm.executeUpdate();
                System.out.println("Лайк від користувача ID: " + like.getIdSource() + " користувачу ID: " + like.getIdTarget() + " додано до БД");
                this.connection.close();
            }
        } else {
            this.connection.close();
            throw new LikeException("User ID: " + like.getIdSource() + " attempts to send like to itself! How pathetic!");
        }
    }

    //Метод позволяет изменить получателя лайка
    protected void changeLike(Like like, Integer newTargetId) throws SQLException {
        this.getConnection();
        if (this.checkLikeDB(like)){
            try (PreparedStatement stm = connection.prepareStatement("UPDATE public.likes SET id_to= ? WHERE id_from = ? AND id_to = ?")){
                stm.setInt(1, newTargetId);
                stm.setInt(2, like.getIdSource());
                stm.setInt(3, like.getIdTarget());
                stm.executeUpdate();
                this.connection.close();
            }
        } else {
            this.connection.close();
            System.out.println("Не вдалося змінити адресата лайку, бо він не горобець");
        }
    }

    //Метод удаляет лайк из БД по ID и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteLike(Like like) throws SQLException {
        this.getConnection();
        if (this.checkLikeDB(like))
            try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.likes WHERE id_from= ? AND id_to= ?")) {
                stm.setInt(1, like.getIdSource());
                stm.setInt(2,like.getIdTarget());
                Integer deleted = stm.executeUpdate();
                this.connection.close();
                return deleted;
            }
        else {
            System.out.println("Лайк від користувача ID: " + like.getIdSource() + " користувачу ID: " + like.getIdTarget() + " не зареєстровано у БД");
            this.connection.close();
            return 0;
        }
    }
}
