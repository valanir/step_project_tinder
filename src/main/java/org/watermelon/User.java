package org.watermelon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class User extends Profile {

    private String login;

    private String password;

    private Map<Integer, List<Message>> chats; //ключом является ид адресата

    private List<Like> likes = new ArrayList<>();

    private List<String> cookies = new ArrayList<>();

    public User(String nickName, String photoSource, String login, String password) {
        super(nickName, photoSource);
        this.login = login;
        this.password = password;
    }
    public User(Integer id, String nickName, String photoSource, String login, String password) {
        super(id, nickName, photoSource);
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<Integer, List<Message>> getChats() {
        return chats;
    }

    public void setChats(Map<Integer, List<Message>> chats) {
        this.chats = chats;
    }

    public void addChat(Integer id, List<Message> chat) {
        this.chats.put(id, chat);
    }

    public void addLike(Integer id) {
        this.likes.add(new Like(this.getId(),id));
    }

    public List<Like> getLikes () {
        return this.likes;
    }

    public List<String> getCookies() {
        return cookies;
    }

    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }

    public void addCookie(String cookie) {
        this.cookies.add(cookie);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;
        return login.equals(user.login) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password);
    }
}
