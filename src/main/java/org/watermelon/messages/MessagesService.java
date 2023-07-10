package org.watermelon.messages;

import org.watermelon.Logger;
import org.watermelon.Message;
import org.watermelon.User;
import org.watermelon.users.UsersDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessagesService {

    private final MessagesDAO messagesDAO = new MessagesDAO("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");

    private final UsersDAO usersDAO = new UsersDAO("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");

    protected MessagesService() {
    }


    //Метод возвращает общее кол-во сообщений в БД
    protected Integer countTotalMessages() {
        try {
            Integer count = this.messagesDAO.countTotalMessages();
            Logger.info("DB contains " + count + " message records");
            return count;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get total messages count");
            return null;
        }
    }

    //Метод возвращает общее кол-во сообщений от пользователя по профилю
    protected Integer countMessagesFromUser(User user) {
        try {
            if (user.getId() != null || this.usersDAO.checkUserById(user.getId())) {
                Integer count = this.messagesDAO.countMessagesFromUser(user);
                Logger.info("Total amount of messages from User ID: " + user.getId() + " : " + count);
                return count;
            } else {
                Logger.info("Attempt to get total amount of messages from user absent in DB");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get amount of messages from User ID: " + user.getId());
            return null;
        }
    }

    //Метод возвращает общее кол-во сообщений от пользователя по ID
    protected Integer countMessagesFromUser(Integer id) {
        try {
            if (id != null || this.usersDAO.checkUserById(id)) {
                Integer count = this.messagesDAO.countMessagesFromUser(id);
                Logger.info("Total amount of messages from User ID: " + id + " : " + count);
                return count;
            } else {
                Logger.info("Attempt to get total amount of messages from user absent in DB");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get amount of messages from User ID: " + id);
            return null;
        }
    }

    //Метод возвращает общее кол-во сообщений от пользователя по никнейму
    protected Integer countMessagesFromUser(String nickName) {
        try {
            if (!nickName.isEmpty()) {
                Integer count = this.messagesDAO.countMessagesFromUser(nickName);
                Logger.info("Total amount of messages from User " + nickName + " : " + count);
                return count;
            } else {
                Logger.info("Attempt to get total amount of messages from user absent in DB (nickname empty)");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get amount of messages from User " + nickName);
            return null;
        }
    }

    //Метод возвращает логический результат наличия сообщения с заданным id в БД
    protected boolean checkMessageDB(Integer id) {
        if (id != null)
            try {
                boolean result = this.messagesDAO.checkMessageDB(id);
                Logger.info("Presence of message with ID: " + id + " checked in DB, result: " + result);
                return result;
            } catch (Exception e) {
                Logger.error("Got " + e.toString() + " when try to check presence of message with ID: " + id + " in DB");
                return false;
            }
        else {
            Logger.info("Attempt to check presence of message with null ID");
            return false;
        }
    }

    //Метод возвращает сообщение по времени и адресату и получателю
    protected Message getMessage(Integer messageId) {
        if (messageId != null)
            try {
                ResultSet result = this.messagesDAO.getMessage(messageId);
                if (result.next()) {
                    Message message = new Message(result.getInt("id_from"), result.getInt("id_to"),
                            result.getString("message"), result.getInt("id"), result.getTimestamp("time"));
                    Logger.info("Received message with ID: " + messageId + " from DB");
                    result.close();
                    return message;
                } else {
                    Logger.info("Message with ID: " + messageId + " is absent in DB");
                    result.close();
                    return null;
                }
            } catch (Exception e) {
                Logger.error("Got " + e.toString() + " when try to get message with ID: " + messageId + " from DB");
                return null;
            }
        else {
            Logger.info("Attempt to get message with null ID");
            return null;
        }
    }

    //Метод возвращает сообщение по профилям собеседников и метке времени
    protected Message getMessage(User source, User dest, Timestamp timestamp) {
        try {
            if (this.usersDAO.checkUserById(source.getId()) && this.usersDAO.checkUserById(dest.getId()) && timestamp != null) {
                ResultSet result = this.messagesDAO.getMessage(source, dest, timestamp);
                if (result.next()) {
                    Message message = new Message(result.getInt("id_from"), result.getInt("id_to"),
                            result.getString("message"), result.getInt("id"), result.getTimestamp("time"));
                    Logger.info("Received message with ID: " + result.getInt("id") + " from DB");
                    result.close();
                    return message;
                } else {
                    Logger.info("Message " + source.getNickName() + "->" + dest.getNickName() + " at " + timestamp + ") is absent in DB");
                    result.close();
                    return null;
                }
            } else {
                Logger.info("Attempt to get message wrong source or destination or null timestamp");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get message from DB (" + source.getNickName()
                    + "->" + dest.getNickName() + " at " + timestamp + ")");
            return null;
        }
    }

    //Метод возвращает сообщение по ID профилей собеседников и метке времени
    protected Message getMessage(Integer sourceId, Integer destId, Timestamp timestamp) {
        try {
            if (this.usersDAO.checkUserById(sourceId) && this.usersDAO.checkUserById(destId) && timestamp != null) {
                ResultSet result = this.messagesDAO.getMessage(sourceId, destId, timestamp);
                if (result.next()) {
                    Message message = new Message(result.getInt("id_from"), result.getInt("id_to"),
                            result.getString("message"), result.getInt("id"), result.getTimestamp("time"));
                    Logger.info("Received message with ID: " + result.getInt("id") + " from DB");
                    result.close();
                    return message;
                } else {
                    Logger.info("Message " + sourceId + "->" + destId + " at " + timestamp + ") is absent in DB");
                    result.close();
                    return null;
                }
            } else {
                Logger.info("Attempt to get message wrong source or destination or null timestamp");
                return null;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get message from DB (" + sourceId
                    + "->" + sourceId + " at " + timestamp + ")");
            return null;
        }
    }

    //Метод добавляет запись о сообщении source пользователя к dest в БД и возвращает id записи
    protected Integer addMessage(Message message) {
        try {
            Integer id = this.messagesDAO.addMessage(message);
            Logger.info("Message from " + message.getSourceID() + " to " + message.getDestID() + " was added to DB");
            return id;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to add message to DB (" + message.getSourceID() + "->" + message.getDestID() + ")");
            return null;
        }
    }

    //Метод удаляет сообщение из БД по ID и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteMessage(Integer id) {
        try {
            Integer result = this.messagesDAO.deleteMessage(id);
            if (result != 0) {
                Logger.info("Message ID: " + id + " was deleted from DB");
                return result;
            } else {
                Logger.info("Message ID: " + id + " is not present in DB");
                return 0;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete message ID: " + id);
            return 0;
        }
    }

    //Метод удаляет запись о сообщении source пользователя к dest из БД
    protected Integer deleteMessage(Message message) {
        try {
            Integer result = this.messagesDAO.deleteMessage(message);
            if (result != 0) {
                Logger.info("Message from " + message.getSourceID() + " to " + message.getDestID() + " was deleted from DB");
                return result;
            } else {
                Logger.info("Message " + message.getSourceID() + "->" + message.getDestID() + "at: "
                        + message.getTimestamp() + " is absent in DB");
                return 0;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete message (" + message.getSourceID() + "->"
                    + message.getDestID() + "at: " + message.getTimestamp() + ") from DB");
            return 0;
        }
    }

    //Метод позволяет изменить/обновить текст сообщения source пользователя к dest в БД, возвращает новое время записи
    protected Timestamp editMessage(Integer id, String newMessageBody) {
        try {
            Timestamp newTime = this.messagesDAO.editMessage(id, newMessageBody);
            Logger.info("Message ID: " + id + " was modified in DB");
            return newTime;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to modify message ID: " + id);
            return null;
        }
    }

    //Метод возвращает коллекцию сообщений между 2мя пользователями сортированную по времени (пока что не обрабатывается группировка по правой/левой стороне)
    protected List<Message> getChat(User source, User dest) {
        List<Message> chat = new ArrayList<>();
        try {
            ResultSet rawChat = this.messagesDAO.getChat(source, dest);
            while (rawChat.next()) {
                chat.add(new Message(rawChat.getInt("id_from"), rawChat.getInt("id_to"),
                        rawChat.getString("message"), rawChat.getInt("id"), rawChat.getTimestamp("time")));
            }
            rawChat.close();
            Logger.info("Received chat between " + source.getId() + " and " + dest.getId() + " from DB");
            return chat;
        } catch (SQLException e) {
            Logger.error("Got " + e.toString() + " when try to get chat between " + source.getId() + " and " + dest.getId() + " from DB");
            return null;
        }
    }

    //Метод возвращает коллекцию сообщений между 2мя пользователями сортированную по времени (пока что не обрабатывается группировка по правой/левой стороне)
    protected List<Message> getChat(Integer sourceId, Integer destId) {
        List<Message> chat = new ArrayList<>();
        try {
            ResultSet rawChat = this.messagesDAO.getChat(sourceId, destId);
            while (rawChat.next()) {
                chat.add(new Message(rawChat.getInt("id_from"), rawChat.getInt("id_to"),
                        rawChat.getString("message"), rawChat.getInt("id"), rawChat.getTimestamp("time")));
            }
            rawChat.close();
            Logger.info("Received chat between " + sourceId + " and " + destId + " from DB");
            return chat;
        } catch (SQLException e) {
            Logger.error("Got " + e.toString() + " when try to get chat between " + sourceId + " and " + destId + " from DB");
            return null;
        }
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями по профилям
    protected Integer countMessagesFromChat(User source,User dest) {
        try {
            Integer result = this.messagesDAO.countMessagesFromChat(source, dest);
            Logger.info("Received message q-ty between " + source.getId() + " and " + dest.getId());
            return result;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get message q-ty between " + source.getId() + " and " + dest.getId());
            return null;
        }
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями по ID
    protected Integer countMessagesFromChat(Integer sourceId, Integer destId) {
        try {
            Integer result = this.messagesDAO.countMessagesFromChat(sourceId, destId);
            Logger.info("Received message q-ty between " + sourceId + " and " + destId);
            return result;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get message q-ty between " + sourceId + " and " + destId);
            return null;
        }
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями по никнеймам
    protected Integer countMessagesFromChat(String sourceNickname, String destNickname) {
        try {
            Integer result = this.messagesDAO.countMessagesFromChat(sourceNickname, destNickname);
            Logger.info("Received message q-ty between " + sourceNickname + " and " + destNickname);
            return result;
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to get message q-ty between " + sourceNickname + " and " + destNickname);
            return null;
        }
    }
}
