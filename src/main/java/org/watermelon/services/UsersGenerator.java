package org.watermelon.services;

import org.watermelon.Profile;
import org.watermelon.User;
import org.watermelon.users.UsersController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersGenerator {
    List<Profile> users = new ArrayList<>();
    UsersController usersController = new UsersController();

    public UsersGenerator() throws SQLException {

    }

    public void initUsers() {
        this.users.add(new Profile(1,"UnknownBeauty", "https://i.pinimg.com/originals/bf/1e/b7/bf1eb795b5f269e3ca7a833f954d1c85.jpg"));
        this.users.add(new Profile(2, "Witch", "https://vjoy.cc/wp-content/uploads/2019/07/2-48.jpg"));
        this.users.add(new Profile(3, "HelloKitty", "https://vjoy.cc/wp-content/uploads/2021/02/2f7147400505b7341a3d2f1b913f55b9747b06fe16cc3a14c05b0814a3c42b80.jpg"));
        this.users.add(new Profile(4, "LifeDestroyer", "https://i.pinimg.com/736x/55/76/29/5576291b493260f343e96dabf11f41d4.jpg"));
        this.users.add(new Profile(5, "VelvetHell", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSm69gI1jAbvnvVHtH_y7P1cWc7J7rgJzLbqYwPqi1MN5kxf4VP9dTV5WQ9r5SdXQ280Hc&usqp=CAU.jpg"));
        this.users.add(new Profile(6, "FluffySurprize", "https://oir.mobi/uploads/posts/2020-01/1579144434_1-1.jpg"));
        this.users.add(new Profile(7, "LaDyCrUsH", "https://moi-nikopol.dp.ua/wp-content/uploads/2021/11/smeshnye-fotki-devushek-stavshih-zhertvami-gore-stilistov-foto_61a4a440bdff3.jpeg"));
        this.users.add(new Profile(8, "QUEEN", "https://cdn.trinixy.ru/pics5/20170825/beautiful_girls_33.jpg"));
        this.users.add(new Profile(9, "CandyGirl", "https://i.pinimg.com/originals/00/25/34/002534402d42421a6530b05b80ec31f6.jpg"));
        this.users.add(new Profile(10,"Angel", "http://cn15.nevsedoma.com.ua/photo/11/1186/1610_files/beautiful_girls_02.jpg"));


        users.forEach(u -> usersController.addUser(new User(u.getId(), u.getNickName(),
                u.getPhotoSource(), u.getNickName(), UUID.randomUUID().toString())));
    }
}
