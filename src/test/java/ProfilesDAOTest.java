/*


import org.junit.jupiter.api.*;
import org.watermelon.Profile;
import org.watermelon.profiles.ProfilesDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfilesDAOTest {

    private ProfilesDAO profilesDAO = new ProfilesDAO("jdbc:postgresql://localhost:5432/testDB", "postgres", "pg123456");

    public ProfilesDAOTest() throws SQLException {
    }

    @Test
    @Order(1)
    public void getAllProfiles() throws SQLException {
        ResultSet testResult = profilesDAO.getAllProfiles();
        List<Profile> profiles = new ArrayList<>();
        while (testResult.next()) {
            profiles.add(new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo")));
        }
        //Проверяем, что коллекция не пустая
        assertThat(profiles.isEmpty()).isEqualTo(false);
        testResult.last();
        int row = testResult.getRow();
        int countDB = profilesDAO.countProfiles();
        //Выводим полученную коллекцию в консоль
        profiles.forEach(System.out::println);
        //Проверяем, что кол-во строк результата соответствует размеру коллекции
        assertThat(profiles.size()).isEqualTo(row);
        //Проверяем, что размер коллекции соответствует кол-ву записей в БД
        assertThat(profiles.size()).isEqualTo(countDB);
        testResult.close();
    }

    @Test
    @Order(2)
    public void getProfileById() throws SQLException {
        ResultSet testResult = profilesDAO.getProfileById(3);
        testResult.next();
        Profile sample = new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo"));
        testResult.last();
        int row = testResult.getRow();
        //Проверяем что метод возвращает только 1 результат
        assertThat(row).isEqualTo(1);
        //Проверяем что метод возвращает профиль с заданным ID и соответствующими полями
        assertThat(sample.getId()).isEqualTo(3);
        assertThat(sample.getNickName()).isEqualTo("HelloKitty");
        assertThat(sample.getPhotoSource()).isEqualTo("https://vjoy.cc/wp-content/uploads/2021/02/2f7147400505b7341a3d2f1b913f55b9747b06fe16cc3a14c05b0814a3c42b80.jpg");
        testResult.close();
    }

    @Test
    @Order(3)
    public void getProfileByNickname() throws SQLException {
        ResultSet testResult = profilesDAO.getProfileByNickname("HelloKitty");
        testResult.next();
        Profile sample = new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo"));
        testResult.last();
        int row = testResult.getRow();
        //Проверяем что метод возвращает только 1 результат
        assertThat(row).isEqualTo(1);
        //Проверяем что метод возвращает профиль с заданным ID и соответствующими полями
        assertThat(sample.getId()).isEqualTo(3);
        assertThat(sample.getNickName()).isEqualTo("HelloKitty");
        assertThat(sample.getPhotoSource()).isEqualTo("https://vjoy.cc/wp-content/uploads/2021/02/2f7147400505b7341a3d2f1b913f55b9747b06fe16cc3a14c05b0814a3c42b80.jpg");
        testResult.close();
    }

    @Test
    @Order(4)
    public void addProfile() throws SQLException {
        int countDBBefore = profilesDAO.countProfiles();
        Profile sample = new Profile("TitaNIC", "https://demotivation.ru/wp-content/uploads/2020/02/1e0c85d91fbf8fc6a4239a5a89d151b6.jpg");
        Integer ids1 = profilesDAO.addProfile(sample);
        int countDBAfter = profilesDAO.countProfiles();

        //Проверяем что кол-во записей в БД увеличилось
        assertThat(countDBAfter).isEqualTo(countDBBefore + 1);

        ResultSet testResult = profilesDAO.getProfileById(ids1);
        testResult.next();
        Profile addedSample = new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo"));

        //Проверяем что добавленная в БД запись идентична извлеченной под одним ID
        assertThat(addedSample.getNickName()).isEqualTo(sample.getNickName());
        assertThat(addedSample.getPhotoSource()).isEqualTo(sample.getPhotoSource());

        sample = new Profile(profilesDAO.getLastProfileId(), "Winter", "No photo");
        Integer ids2 = profilesDAO.addProfile(sample);

        //Проверяем что кол-во записей не изменилось, т.к. прошел апдейт существующей
        assertThat(profilesDAO.countProfiles()).isEqualTo(countDBAfter);

        //Проверяем что id записи не изменился и является последним на текущий момент
        assertThat(ids1).isEqualTo(ids2).isEqualTo(profilesDAO.getLastProfileId());

        //Проверяем, что запись действительно изменилась
        testResult = profilesDAO.getProfileById(ids1);
        testResult.next();
        Profile modifiedSample = new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo"));
        assertThat(sample).isEqualTo(modifiedSample);
        testResult.close();
    }

    @Test
    @Order(5)
    public void deleteProfile() throws SQLException {
        int countDBBefore = profilesDAO.countProfiles();
        ResultSet testResult = profilesDAO.getProfileByNickname("Winter");
        testResult.next();
        Profile sample = new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo"));
        profilesDAO.deleteProfile(sample);
        int countDBAfter = profilesDAO.countProfiles();

        //Проверяем что кол-во записей уменьшилось
        assertThat(countDBAfter).isLessThan(countDBBefore);

        //Проверяем что такая запись отсутствует в БД
        assertThat(profilesDAO.checkProfileByNickname(sample.getNickName())).isFalse();

        countDBBefore = profilesDAO.countProfiles();
        testResult = profilesDAO.getProfileById(profilesDAO.getLastProfileId());
        testResult.next();
        sample = new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo"));
        profilesDAO.deleteProfile(profilesDAO.getLastProfileId());
        countDBAfter = profilesDAO.countProfiles();

        //Проверяем что кол-во записей уменьшилось
        assertThat(countDBAfter).isLessThan(countDBBefore);

        //Проверяем что такая запись отсутствует в БД
        assertThat(profilesDAO.checkProfileById(sample.getId())).isFalse();

        testResult = profilesDAO.getProfileById(profilesDAO.getLastProfileId());
        testResult.next();
        sample = new Profile(testResult.getInt("id"), testResult.getString("nickname"), testResult.getString("photo"));
        testResult.close();
        countDBBefore = profilesDAO.countProfiles();
        profilesDAO.deleteProfile(sample.getNickName());
        countDBAfter = profilesDAO.countProfiles();

        //Проверяем что кол-во записей уменьшилось
        assertThat(countDBAfter).isLessThan(countDBBefore);

        //Проверяем что такая запись отсутствует в БД
        assertThat(profilesDAO.checkProfileByNickname(sample.getNickName())).isFalse();
    }

    @Test
    @Order(6)
    public void countProfiles() throws SQLException {
        int before = profilesDAO.countProfiles();
        System.out.println("Before test: " + before);
        Profile sample1 = new Profile("KrevedKo", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRO4g2uY6N-RW5y-9rEzuO7weBg9tuqGnmWdQ&usqp=CAU");
        Profile sample2 = new Profile("Ghost", "https://krasavica.info/uploads/posts/2022-03/1647860344_16-krasavica-info-p-devushka-s-belimi-volosami-devushka-krasiv-17.jpg");
        Profile sample3 = new Profile("UnknownBeauty", "https://i.pinimg.com/originals/bf/1e/b7/bf1eb795b5f269e3ca7a833f954d1c85.jpg");
        profilesDAO.addProfile(sample1);
        profilesDAO.addProfile(sample2);
        profilesDAO.addProfile(sample3);
        int after = profilesDAO.countProfiles();
        System.out.println("After addition: " + after);
        assertThat(after).isEqualTo(before + 3);
        profilesDAO.deleteProfile("KrevedKo");
        after = profilesDAO.countProfiles();
        assertThat(after).isEqualTo(before + 2);
    }
}
*/
