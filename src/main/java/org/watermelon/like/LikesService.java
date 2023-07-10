package org.watermelon.like;

import org.watermelon.*;
import org.watermelon.users.UsersDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LikesService {

    private final LikesDAO likesDAO = new LikesDAO("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");

    private final UsersDAO usersDAO = new UsersDAO("jdbc:postgresql://ep-yellow-glade-106284.eu-central-1.aws.neon.tech/webstep", "valanir.work", "DiwkFCI2Jo9N");

    protected LikesService() {
    }

    //Метод возвращает общее кол-во лайков в БД
    protected Integer countTotalLikes() {
        try {
            Integer count = this.likesDAO.countTotalLikes();
            Logger.info("DB contains " + count + " like records");
            return count;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get total amount of likes from DB");
            return null;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по профилю
    protected Integer countLikesFromUser(User user) {
        try {
            Integer count = this.likesDAO.countLikesFromUser(user);
            Logger.info("DB contains " + count + " like records from user ID: " + user.getId());
            return count;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get total amount of likes from user ID: " + user.getId());
            return null;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по id
    protected Integer countLikesFromUser(Integer id) {
        try {
            Integer count = this.likesDAO.countLikesFromUser(id);
            Logger.info("DB contains " + count + " like records from user ID: " + id);
            return count;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get total amount of likes from user ID: " + id);
            return null;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    protected Integer countLikesFromUser(String nickName) {
        try {
            Integer count = this.likesDAO.countLikesFromUser(nickName);
            Logger.info("DB contains " + count + " like records from user: " + nickName);
            return count;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get total amount of likes from user: " + nickName);
            return null;
        }
    }

    //Метод возвращает общее кол-во лайков пользователю по профилю
    protected Integer countLikesToUser(User user) {
        try {
            Integer count = this.likesDAO.countLikesToUser(user);
            Logger.info("DB contains " + count + " like records from user ID: " + user.getId());
            return count;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get total amount of likes from user ID: " + user.getId());
            return null;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по id
    protected Integer countLikesToUser(Integer id) {
        try {
            Integer count = this.likesDAO.countLikesToUser(id);
            Logger.info("DB contains " + count + " like records from user ID: " + id);
            return count;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get total amount of likes from user ID: " + id);
            return null;
        }
    }

    //Метод возвращает общее кол-во лайков от пользователя по никнейму (нужно гарантировать уникальность никнейма!)
    protected Integer countLikesToUser(String nickName) {
        try {
            Integer count = this.likesDAO.countLikesToUser(nickName);
            Logger.info("DB contains " + count + " like records from user: " + nickName);
            return count;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get total amount of likes from user: " + nickName);
            return null;
        }
    }

    //Метод возвращает логический результат наличия лайка в БД
    protected boolean checkLikeDB(Like like) {
        try {
            boolean checkResult = this.likesDAO.checkLikeDB(like);
            Logger.info("Presence of like (" + like.getIdSource() + "->" + like.getIdTarget() + ") checked in DB, result: " + checkResult);
            return checkResult;
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to check like (" + like.getIdSource() + "->" + like.getIdTarget() + ") in DB");
            return false;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователем по его id
    protected List<Like> getLikesFromUser(Integer id) {
        List<Like> result = new ArrayList<>();
        try {
            if (this.usersDAO.checkUserById(id)) {
                ResultSet likes = this.likesDAO.getLikesFromUser(id);
                while (likes.next()) {
                    result.add(new Like(likes.getInt("id_from"), likes.getInt("id_to")));
                }
                Logger.info("Received likes records of user ID: " + id);
                return result;
            } else {
                Logger.info("Attempt to get likes from no existed user ID: " + id);
                return null;
            }
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get likes from user ID: " + id);
            return null;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователем по его профилю
    protected List<Like> getLikesFromUser(User user) {
        List<Like> result = new ArrayList<>();
        try {
            if (this.usersDAO.checkUserById(user.getId())) {
                ResultSet likes = this.likesDAO.getLikesFromUser(user.getId());
                while (likes.next()) {
                    result.add(new Like(likes.getInt("id_from"), likes.getInt("id_to")));
                }
                Logger.info("Received likes records of user ID: " + user.getId());
                return result;
            } else {
                Logger.info("Attempt to get likes from no existed user ID: " + user.getId());
                return null;
            }
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get likes from user ID: " + user.getId());
            return null;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователю по его id
    protected List<Like> getLikesToUser(Integer id) {
        List<Like> result = new ArrayList<>();
        try {
            if (this.usersDAO.checkUserById(id)) {
                ResultSet likes = this.likesDAO.getLikesToUser(id);
                while (likes.next()) {
                    result.add(new Like(likes.getInt("id_from"), likes.getInt("id_to")));
                }
                Logger.info("Received likes records of user ID: " + id);
                return result;
            } else {
                Logger.info("Attempt to get likes from no existed user ID: " + id);
                return null;
            }
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get likes from user ID: " + id);
            return null;
        }
    }

    //Метод возвращает коллекцию лайков поставленных пользователю по его id
    protected List<Like> getLikesToUser(User user) {
        List<Like> result = new ArrayList<>();
        try {
            if (this.usersDAO.checkUserById(user.getId())) {
                ResultSet likes = this.likesDAO.getLikesFromUser(user.getId());
                while (likes.next()) {
                    result.add(new Like(likes.getInt("id_from"), likes.getInt("id_to")));
                }
                Logger.info("Received likes records of user ID: " + user.getId());
                return result;
            } else {
                Logger.info("Attempt to get likes from no existed user ID: " + user.getId());
                return null;
            }
        } catch (Exception e) {
            Logger.info("Got " + e.toString() + " when try to get likes from user ID: " + user.getId());
            return null;
        }
    }

    //Метод добавляет запись о лайке source пользователя к dest в БД, бросает исключения если id отправителя и адресата совпадают
    protected void addLike(Like like) {
        try {
            this.likesDAO.addLike(like);
            Logger.info("Record about like(" + like.getIdSource() + "->" + like.getIdTarget() + ") added to DB");
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to add like (" + like.getIdSource() + "->" + like.getIdTarget() + ")");
        }
    }

    //Метод позволяет изменить получателя лайка
    protected void changeLike(Like like, Integer newTargetId) {
        try {
            this.likesDAO.changeLike(like, newTargetId);
            Logger.info("Record like(" + like.getIdSource() + "->" + like.getIdTarget() + ") changed in DB");
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to change like (" + like.getIdSource() + "->" + like.getIdTarget() + ")");
        }
    }

    //Метод удаляет лайк из БД по ID и возвращает кол-во удаленных записей (можно использовать для логических индикаторов)
    protected Integer deleteLike(Like like) {
        try {
            if (this.likesDAO.checkLikeDB(like)) {
                Integer count = this.likesDAO.deleteLike(like);
                Logger.info("Record like(" + like.getIdSource() + "->" + like.getIdTarget() + ") changed in DB");
                return count;
            } else {
                Logger.info("Attempt to delete no existed like (" + like.getIdSource() + "->" + like.getIdTarget() + ")");
                return 0;
            }
        } catch (Exception e) {
            Logger.error("Got " + e.toString() + " when try to delete like (" + like.getIdSource() + "->" + like.getIdTarget() + ")");
            return 0;
        }
    }
}
