package org.watermelon.messages;

import org.watermelon.*;
import org.watermelon.users.UsersDAO;

import java.sql.*;
import java.util.Objects;

public class MessagesDAO {

    protected Connection connection;

    private final UsersDAO usersDAO = new UsersDAO("jdbc:postgresql://localhost:5432/webstep", "postgres", "pg123456");

    private MessagesDAO() throws SQLException {
    }

    protected MessagesDAO(String sourceDB, String login, String password) {
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

    //Метод возвращает общее кол-во сообщений в БД
    protected Integer countTotalMessages() throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(message) from public.messages")) {
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во сообщений от пользователя по профилю
    protected Integer countMessagesFromUser(User user) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(message) from public.messages WHERE id_from= ?")) {
            stm.setInt(1, user.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во сообщений от пользователя по id
    protected Integer countMessagesFromUser(Integer id) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(message) from public.messages WHERE id_from= ?")) {
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во сообщений от пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    protected Integer countMessagesFromUser(String nickName) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(message) from public.messages WHERE id_from= ?")) {
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

    //Метод возвращает логический результат наличия сообщения с заданным id в БД
    protected boolean checkMessageDB(Integer id) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT * from public.messages "
                + "WHERE id= ?")) {
            stm.setInt(1, id);
            ResultSet resultSet = stm.executeQuery();
            boolean check = resultSet.next();
            this.connection.close();
            return check;
        } catch (Exception e) {
            System.out.println("Сталась помилка при спробі перевірки наявності повідомлення у БД");
            this.connection.close();
            return false;
        }
    }

    /*    //Метод возвращает логический результат проверки наличия сообщения в БД без учета id
    public boolean checkMessageDB(Message message) throws SQLException {
        try (PreparedStatement stm = connection.prepareStatement("SELECT * from public.messages "
                + "WHERE id_from = ? AND id_to = ? AND message= ? AND time= ?")) {
            stm.setInt(1, message.getSourceID());
            stm.setInt(2, message.getDestID());
            stm.setString(3, message.getMessage());
            stm.setTimestamp(4, message.getTimestamp());
            ResultSet resultSet = stm.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println("Сталась помилка при спробі перевірки наявності повідомлення у БД");
            return false;
        }
    }*/

    //Метод возвращает сообщение по его ID
    protected ResultSet getMessage(Integer messageId) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * from public.messages WHERE id = ?");
            stm.setInt(1, messageId);
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати повідомлення з ДБ");
            this.connection.close();
            return null;
        }
    }

    //Метод возвращает сообщение по профилям собеседников и метке времени
    protected ResultSet getMessage(User source, User dest, Timestamp timestamp) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * from public.messages "
                    + "WHERE id_from = ? AND id_to = ? AND time= ?");
            stm.setInt(1, source.getId());
            stm.setInt(2, dest.getId());
            stm.setTimestamp(3, timestamp);
            ResultSet resultSet = stm.executeQuery();
            Logger.info("");
            return resultSet;
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати повідомлення з ДБ");
            this.connection.close();
            return null;
        }
    }

    //Метод возвращает сообщение по ID профилей собеседников и метке времени
    protected ResultSet getMessage(Integer sourceId, Integer destId, Timestamp timestamp) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT * from public.messages "
                + "WHERE id_from = ? AND id_to = ? AND time= ?")) {
            stm.setInt(1, sourceId);
            stm.setInt(2, destId);
            stm.setTimestamp(3, timestamp);
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Cталась помилка при спробі отримати повідомлення з ДБ");
            this.connection.close();
            return null;
        }
    }

    //Метод добавляет запись о сообщении source пользователя к dest в БД и возвращает id записи, бросает исключения если id отправителя и адреса совпадают/
    protected Integer addMessage(Message message) throws SQLException, MessageException {
        this.getConnection();
        if (!Objects.equals(message.getSourceID(), message.getDestID())) {
            try (PreparedStatement stm = connection.prepareStatement("INSERT INTO public.messages (id_from, id_to, message) "
                    + "VALUES (?, ?, ?)", new String[]{"id"})) {
                stm.setInt(1, message.getSourceID());
                stm.setInt(2, message.getDestID());
                stm.setString(3, message.getMessage());
                stm.executeUpdate();
                System.out.println("Повідомлення додано до БД");
                ResultSet rid = stm.getGeneratedKeys();
                if (rid.next()) {
                    Integer id = rid.getInt(1);
                    this.connection.close();
                    return id;
                } else {
                    this.connection.close();
                    return null;
                }
            }
        } else {
            this.connection.close();
            throw new MessageException("Profile ID: " + message.getSourceID() + " attempts to send message to itself!");
        }
    }

    //Метод удаляет сообщение из БД по ID и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteMessage(Integer id) throws SQLException {
        this.getConnection();
        if (this.checkMessageDB(id))
            try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.messages WHERE id= ?")) {
                stm.setInt(1, id);
                Integer deleted = stm.executeUpdate();
                this.connection.close();
                return deleted;
            }
        else {
            System.out.println("Повідомлення с id: " + id + " не зареєстровано у БД");
            this.connection.close();
            return 0;
        }
    }

    //Метод удаляет запись о сообщении source пользователя к dest из БД и возвращает кол-во записей, которые были удалены
    protected Integer deleteMessage(Message message) throws SQLException {
        this.getConnection();
        if (this.checkMessageDB(message.getId())) {
            try (PreparedStatement stm = connection.prepareStatement("DELETE FROM public.messages WHERE message = ? AND id_from = ? AND id_to = ? AND time= ? AND id = ?")) {
                stm.setString(1, message.getMessage());
                stm.setInt(2, message.getSourceID());
                stm.setInt(3, message.getDestID());
                stm.setTimestamp(4, message.getTimestamp());
                stm.setInt(5, message.getId());
                System.out.println("Повідомлення видалено з БД");
                Integer deleted = stm.executeUpdate();
                this.connection.close();
                return deleted;
            }
        } else {
            System.out.println("Таке повідомлення відсутнє у БД!");
            Logger.info("Attempt to delete absent message from DB (from_id: " + message.getSourceID()
                    + ", to_id: " + message.getDestID() + ", text: " + message.getMessage() + ", time: " + message.getTimestamp());
            this.connection.close();
            return 0;
        }
    }

/*    public void deleteMessage(Integer sourceID, Integer destID, String message, Timestamp timestamp) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT message from public.messages WHERE message = ? AND id_from = ? AND id_to = ? AND time= ?");
        stm.setString(1, message);
        stm.setInt(2, sourceID);
        stm.setInt(3, destID);
        stm.setTimestamp(4, timestamp);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            stm = connection.prepareStatement("DELETE FROM public.messages WHERE message = ? AND id_from = ? AND id_to = ? AND time= ?");
            stm.setString(1, message);
            stm.setInt(2, sourceID);
            stm.setInt(3, destID);
            stm.setTimestamp(4, timestamp);
            stm.execute();
            System.out.println("Повідомлення видалено з БД");
        } else {
            System.out.println("Таке повідомлення відсутнє у БД!");
            Logger.info("Attempt to delete absent message from DB (from_id: " + sourceID + ", to_id: " + destID + ", text: " + message + ", time: " + timestamp);
        }
        stm.close();
    }*/

    //Метод позволяет изменить/обновить текст сообщения source пользователя к dest в БД, возвращает новое время записи
    protected Timestamp editMessage(Integer id, String newMessageBody) throws SQLException {
        this.getConnection();
        if (this.checkMessageDB(id)) {
            try (PreparedStatement stm = this.connection.prepareStatement("UPDATE public.messages SET message= ?, time = DEFAULT WHERE id = ?")) {
                stm.setString(1, newMessageBody);
                stm.setInt(2, id);
                stm.executeUpdate();
                System.out.println("Повідомлення ID:" + id + " відредаговане у БД");
                Logger.info("Message with ID: " + id + " was modified in DB");
                this.connection.close();
            }
            try (PreparedStatement stm2 = connection.prepareStatement("SELECT time from public.messages WHERE id = ?")) {
                stm2.setInt(1, id);
                ResultSet rtime = stm2.executeQuery();
                rtime.next();
                Timestamp time =  rtime.getTimestamp("time");
                this.connection.close();
                return time;
            }
        } else {
            System.out.println("Таке повідомлення відсутнє у БД!");
            Logger.info("Attempt to edit absent message from DB (id = " + id + " )");
            this.connection.close();
            return null;
        }
    }

    //Первоначальная извращенная реализация в попытках уйти от id каждого сообщения
/*    public Timestamp editMessage(Message message, String newMessageBody) throws SQLException {
        if (this.checkMessageDB(message)) {
            try (PreparedStatement stm = connection.prepareStatement("UPDATE public.messages SET message= ?, time = DEFAULT WHERE id_from = ? AND id_to = ? AND message = ? AND time= ?")) {
                stm.setString(1, newMessageBody);
                stm.setInt(2, message.getSourceID());
                stm.setInt(3, message.getDestID());
                stm.setString(4, message.getMessage());
                stm.setTimestamp(5, message.getTimestamp());
                stm.executeUpdate();
                System.out.println("Повідомлення додано до БД");
            }
            try (PreparedStatement stm2 = connection.prepareStatement("SELECT time from public.messages WHERE id_from= ? AND id_to= ? AND message= ?")) {
                stm2.setInt(1, message.getSourceID());
                stm2.setInt(2, message.getDestID());
                stm2.setString(3, newMessageBody);
                ResultSet time = stm2.executeQuery();
                time.next();
                return time.getTimestamp("time");
            }
        } else {
            System.out.println("Таке повідомлення відсутнє у БД!");
            Logger.info("Attempt to edit absent message from DB (from_id: " + message.getSourceID() + ", to_id: " + message.getDestID() + ", text: " + message.getMessage());
            return message.getTimestamp();
        }
    }*/

    //Метод возвращает коллекцию сообщений между 2мя пользователями, сортированную по дате, с маркерами принадлежности R, L для сепарации
    protected ResultSet getChat(User source, User dest) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT message, time, 'R' side from public.messages "
                    + "WHERE id_from = ? AND id_to = ? "
                    + "UNION "
                    + "SELECT message, time 'L' from public.messages WHERE id_from = ? AND id_to = ? "
                    + "ORDER BY time");
            stm.setInt(1, source.getId());
            stm.setInt(2, dest.getId());
            stm.setInt(3, dest.getId());
            stm.setInt(4, source.getId());
            System.out.println("Повідомлення користувачів з ID: " + source.getId() + " та ID: " + dest.getId() + " звантажені із БД");
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Сталась помилка при спробі завантажити чат з БД");
            this.connection.close();
            return null;
        }
    }

    protected ResultSet getChat(Integer sourceId, Integer destId) throws SQLException {
        this.getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT *, 'R' side from public.messages "
                    + "WHERE id_from = ? AND id_to = ? "
                    + "UNION "
                    + "SELECT *, 'L' from public.messages WHERE id_from = ? AND id_to = ? "
                    + "ORDER BY time", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, sourceId);
            stm.setInt(2, destId);
            stm.setInt(3, destId);
            stm.setInt(4, sourceId);
            System.out.println("Повідомлення користувачів з ID: " + sourceId + " та ID: " + destId + " звантажені із БД");
            return stm.executeQuery();
        } catch (Exception e) {
            System.out.println("Сталась помилка при спробі завантажити чат з БД");
            this.connection.close();
            return null;
        }
    }


    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями
    protected Integer countMessagesFromChat(User source, User dest) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(message) from public.messages WHERE id_from= ? AND id_to= ?")) {
            stm.setInt(1, source.getId());
            stm.setInt(2, dest.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями
    protected Integer countMessagesFromChat(Integer sourceId, Integer destId) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(message) from public.messages WHERE id_from= ? AND id_to= ?")) {
            stm.setInt(1, sourceId);
            stm.setInt(2, destId);
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями
    protected Integer countMessagesFromChat(String sourceNickname, String destNickname) throws SQLException {
        this.getConnection();
        try (PreparedStatement stm = connection.prepareStatement("SELECT COUNT(message) from public.messages WHERE id_from= ? AND id_to= ?")) {
            ResultSet temp = usersDAO.getUserByNickname(sourceNickname);
            temp.next();
            Profile source = new Profile(temp.getInt("id"), temp.getString("nickname"), temp.getString("photo"));
            temp = usersDAO.getUserByNickname(destNickname);
            temp.next();
            Profile dest = new Profile(temp.getInt("id"), temp.getString("nickname"), temp.getString("photo"));
            stm.setInt(1, source.getId());
            stm.setInt(2, dest.getId());
            ResultSet result = stm.executeQuery();
            result.next();
            Integer count = result.getInt("count");
            this.connection.close();
            return count;
        }
    }
}
