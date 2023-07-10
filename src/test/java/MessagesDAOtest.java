/*

import org.junit.jupiter.api.*;
import org.watermelon.Message;
import org.watermelon.MessageException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessagesDAOtest {

    private MessagesDAO messagesDAO = new MessagesDAO("jdbc:postgresql://localhost:5432/testDB", "postgres", "pg123456");

    @Test
    @Order(1)
    public void addMessage() throws SQLException {
        Message sample1 = new Message(3, 2, "Дратуті!");
        Message sample2 = new Message(3, 2, "Не морозься!");
        Message sample3 = new Message(3, 3, "Воно знову забуло...");
        Integer countBefore = messagesDAO.countTotalMessages();
        Integer countBeforeFromUser = messagesDAO.countMessagesFromUser(sample1.getSourceID());
        Integer ids1 = messagesDAO.addMessage(sample1);
        Integer countAfter = messagesDAO.countTotalMessages();
        Integer countAfterFromUser = messagesDAO.countMessagesFromUser(sample1.getSourceID());

        //Проверяем что кол-во сообщений в БД увеличилось
        assertThat(countAfter).isEqualTo(countBefore + 1);
        assertThat(countAfterFromUser).isEqualTo(countBeforeFromUser + 1);

        //Проверяем что сообщение содержится в БД
        assertThat(messagesDAO.checkMessageDB(ids1)).isTrue();

        Integer ids2 = messagesDAO.addMessage(sample2);
        countAfter = messagesDAO.countTotalMessages();
        countAfterFromUser = messagesDAO.countMessagesFromUser(sample2.getSourceID());

        //Проверяем что кол-во сообщений в БД увеличилось
        assertThat(countAfter).isEqualTo(countBefore + 2);
        assertThat(countAfterFromUser).isEqualTo(countBeforeFromUser + 2);

        //Проверяем что сообщение содержится в БД
        assertThat(messagesDAO.checkMessageDB(ids2)).isTrue();

        boolean catchError = false;
        try {
            messagesDAO.addMessage(sample3);
        } catch (MessageException e) {
            catchError = true;
        }
        //Проверяем что кол-во сообщений в БД не увеличилось, т.к. метод должен бросить исключение
        assertThat(countAfter).isEqualTo(countBefore + 2);
        assertThat(countAfterFromUser).isEqualTo(countBeforeFromUser + 2);
        //Проверяем что исключение было брошено
        assertThat(catchError).isTrue();
        messagesDAO.deleteMessage(ids1);
        messagesDAO.deleteMessage(ids2);
    }

    @Test
    @Order(2)
    public void editMessage() throws SQLException {
        Message sample1 = new Message(2, 3, "Доброго дня");
        Message sample2 = new Message(2, 3, "Що знову трапилося?");
        Integer ids1 = messagesDAO.addMessage(sample1);
        Integer ids2 = messagesDAO.addMessage(sample2);
        ResultSet testResult = messagesDAO.getMessage(ids2);
        testResult.next();
        Timestamp time1 = testResult.getTimestamp("time");
        Timestamp time2 = messagesDAO.editMessage(ids2, "Що знову трапилося? Знову прибульці?");

        //Проверяем что сообщение изменилось и маркер времени также обновился
        assertThat(time2).isAfter(time1);
        testResult = messagesDAO.getMessage(ids2);
        testResult.next();
        assertThat(testResult.getString("message")).isNotEqualTo(sample2.getMessage());
        assertThat(testResult.getString("message")).isEqualTo("Що знову трапилося? Знову прибульці?");
        messagesDAO.deleteMessage(ids1);
        messagesDAO.deleteMessage(ids2);
        testResult.close();
    }

    @Test
    @Order(3)
    public void deleteMessage() throws SQLException {
        Message sample1 = new Message(2, 3, "Доброго дня");
        Message sample2 = new Message(2, 3, "Що знову трапилося? Знову прибульці?");
        Integer ids1 = messagesDAO.addMessage(sample1);
        Integer ids2 = messagesDAO.addMessage(sample2);
        Integer countBeforeDelete = messagesDAO.countTotalMessages();

        //Проверяем что вернулось верное значение удаленных записей
        assertThat(messagesDAO.deleteMessage(ids1)).isEqualTo(1);
        assertThat(messagesDAO.deleteMessage(ids2)).isEqualTo(1);
        assertThat(messagesDAO.deleteMessage(ids2)).isEqualTo(0);
        Integer countAfterDelete = messagesDAO.countTotalMessages();

        //Проверяем, что кол-во записей уменьшилось
        assertThat(countBeforeDelete).isGreaterThan(countAfterDelete);

        //Проверяем что такой записи больше нет
        assertThat(messagesDAO.checkMessageDB(ids1)).isFalse();
        assertThat(messagesDAO.checkMessageDB(ids1)).isFalse();
    }

    @Test
    @Order(4)
    public void getChat() throws SQLException {
        Message sample1 = new Message(3, 1, "Креведъ!");
        Message sample2 = new Message(3, 1, "Какие планы?");
        Message sample3 = new Message(1, 3, "Привіт");
        Message sample4 = new Message(1, 3, "Наближаємо перемогу!");
        Message sample5 = new Message(3, 1, "Боевые комары готовы? Голуби зарядились? Гуси сконфигурированы?");
        Message sample6 = new Message(1, 3, "Звісно! Ще залишилось ховрашків-навідників бавовною спорядити");
        Integer ids1 = messagesDAO.addMessage(sample1);
        Integer ids2 = messagesDAO.addMessage(sample2);
        Integer ids3 = messagesDAO.addMessage(sample3);
        Integer ids4 = messagesDAO.addMessage(sample4);
        Integer ids5 = messagesDAO.addMessage(sample5);
        Integer ids6 = messagesDAO.addMessage(sample6);
        ResultSet chatTest = messagesDAO.getChat(3, 1);
        List<Message> chat = new ArrayList<>();
        while (chatTest.next()) {
            chat.add(new Message(chatTest.getInt("id_from"), chatTest.getInt("id_to"), chatTest.getString("message")));
        }
        //Проверяем что итоговая коллекция не пустая, имеет ожидаемый размер, содержит все сообщения
        assertThat(chat).isNotEmpty();
        assertThat(chat.size()).isEqualTo(6);
        assertThat(chat).contains(sample1, sample2, sample3, sample4, sample5, sample6);

        //Очищаем тестовые сообщения из БД
        messagesDAO.deleteMessage(ids1);
        messagesDAO.deleteMessage(ids2);
        messagesDAO.deleteMessage(ids3);
        messagesDAO.deleteMessage(ids4);
        messagesDAO.deleteMessage(ids5);
        messagesDAO.deleteMessage(ids6);
        chatTest.close();
    }
}
*/
