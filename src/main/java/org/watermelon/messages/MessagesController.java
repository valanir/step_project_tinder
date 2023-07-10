package org.watermelon.messages;

import org.watermelon.Message;
import org.watermelon.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class MessagesController {

    private final MessagesService messagesService = new MessagesService();

    public MessagesController() {
    }

    //Метод возвращает общее кол-во сообщений в БД
    public Integer countTotalMessages() {
        return this.messagesService.countTotalMessages();
    }

    //Метод возвращает общее кол-во сообщений от пользователя по профилю
    public Integer countMessagesFromUser(User user) {
        return this.messagesService.countMessagesFromUser(user);
    }

    //Метод возвращает общее кол-во сообщений от пользователя по ID
    public Integer countMessagesFromUser(Integer id) {
        return this.messagesService.countMessagesFromUser(id);
    }

    //Метод возвращает общее кол-во сообщений от пользователя по никнейму
    public Integer countMessagesFromUser(String nickName) {
        return this.messagesService.countMessagesFromUser(nickName);
    }

    //Метод возвращает логический результат наличия сообщения с заданным id в БД
    public boolean checkMessageDB(Integer id) {
        return this.messagesService.checkMessageDB(id);
    }

    //Метод возвращает сообщение по времени и адресату и получателю
    public Message getMessage(Integer messageId) {
        return this.messagesService.getMessage(messageId);
    }

    //Метод возвращает сообщение по профилям собеседников и метке времени
    public Message getMessage(User source, User dest, Timestamp timestamp) {
        return this.messagesService.getMessage(source, dest, timestamp);
    }

    //Метод возвращает сообщение по ID профилей собеседников и метке времени
    public Message getMessage(Integer sourceId, Integer destId, Timestamp timestamp) {
        return this.messagesService.getMessage(sourceId, destId, timestamp);
    }

    //Метод добавляет запись о сообщении source пользователя к dest в БД и возвращает id записи
    public Integer addMessage(Message message) {
        return this.messagesService.addMessage(message);
    }

    //Метод удаляет сообщение из БД по ID и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    public Integer deleteMessage(Integer id) {
        return this.messagesService.deleteMessage(id);
    }

    //Метод удаляет запись о сообщении source пользователя к dest из БД
    public Integer deleteMessage(Message message) {
        return this.messagesService.deleteMessage(message);
    }

    //Метод позволяет изменить/обновить текст сообщения source пользователя к dest в БД, возвращает новое время записи
    public Timestamp editMessage(Integer id, String newMessageBody) {
        return this.messagesService.editMessage(id, newMessageBody);
    }

    //Метод возвращает коллекцию сообщений между 2мя пользователями сортированную по времени (пока что не обрабатывается группировка по правой/левой стороне)
    public List<Message> getChat(User source, User dest) {
        return this.messagesService.getChat(source, dest);
    }

    //Метод возвращает коллекцию сообщений между 2мя пользователями сортированную по времени (пока что не обрабатывается группировка по правой/левой стороне)
    public List<Message> getChat(Integer sourceId, Integer destId) {
        return this.messagesService.getChat(sourceId, destId);
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями по профилям
    public Integer countMessagesFromChat(User source, User dest) {
        return this.messagesService.countMessagesFromChat(source, dest);
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями по ID
    public Integer countMessagesFromChat(Integer sourceId, Integer destId) {
        return this.messagesService.countMessagesFromChat(sourceId, destId);
    }

    //Метод возвращает общее кол-во сообщений в чате между 2мя пользователями по никнеймам
    public Integer countMessagesFromChat(String sourceNickname, String destNickname) {
        return this.messagesService.countMessagesFromChat(sourceNickname, destNickname);
    }
}
