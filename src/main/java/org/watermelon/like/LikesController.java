package org.watermelon.like;

import org.watermelon.Like;
import org.watermelon.User;

import java.sql.SQLException;
import java.util.List;

public class LikesController {

    private final LikesService likesService = new LikesService();

    public LikesController(){
    }

    //Метод возвращает общее кол-во лайков в БД
    public Integer countTotalLikes() {
        return this.likesService.countTotalLikes();
    }

    //Метод возвращает общее кол-во лайков от пользователя по профилю
    public Integer countLikesFromUser(User user) {
        return this.likesService.countLikesFromUser(user);
    }

    //Метод возвращает общее кол-во лайков от пользователя по id
    public Integer countLikesFromUser(Integer id) {
        return this.likesService.countLikesFromUser(id);
    }

    //Метод возвращает общее кол-во лайков от пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    public Integer countLikesFromUser(String nickName) {
        return this.likesService.countLikesFromUser(nickName);
    }

    //Метод возвращает общее кол-во лайков пользователю по профилю
    public Integer countLikesToUser(User user) { return this.likesService.countLikesToUser(user); }

    //Метод возвращает общее кол-во лайков от пользователя по id
    public Integer countLikesToUser(Integer id) { return this.likesService.countLikesToUser(id); }

    //Метод возвращает общее кол-во лайков от пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    public Integer countLikesToUser(String nickName) { return this.likesService.countLikesToUser(nickName); }

    //Метод возвращает логический результат наличия лайка в БД
    public boolean checkLikeDB(Like like) {
        return this.likesService.checkLikeDB(like);
    }

    //Метод возвращает коллекцию лайков поставленных пользователем по его id
    public List<Like> getLikesFromUser(Integer id) {
        return this.likesService.getLikesFromUser(id);
    }

    //Метод возвращает коллекцию лайков поставленных пользователем по его профилю
    public List<Like> getLikesFromUser(User user) {
        return this.likesService.getLikesFromUser(user);
    }

    //Метод возвращает коллекцию лайков поставленных пользователю по его id
    public List<Like> getLikesToUser(Integer id) {
        return this.likesService.getLikesToUser(id);
    }

    //Метод возвращает коллекцию лайков поставленных пользователю по его id
    public List<Like> getLikesToUser(User user) {
        return this.likesService.getLikesToUser(user);
    }

    //Метод добавляет запись о лайке source пользователя к dest в БД, бросает исключения если id отправителя и адресата совпадают
    public void addLike(Like like) {
        this.likesService.addLike(like);
    }

    //Метод позволяет изменить получателя лайка
    public void changeLike(Like like, Integer newTargetId) {
        this.likesService.changeLike(like, newTargetId);
    }

    //Метод удаляет лайк из БД по ID и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    public Integer deleteLike(Like like) {
        return this.likesService.deleteLike(like);
    }
}
