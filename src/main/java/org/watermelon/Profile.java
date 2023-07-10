package org.watermelon;

import java.nio.file.Path;
import java.util.Objects;

public class Profile {

    private Integer id;
    private String nickName;
    private String photoSource;

    private String password;

    public Profile (String nickName, String photoSource){
        this.nickName = nickName;
        this.photoSource = photoSource;
    }

    public Profile (Integer id, String nickName, String photoSource) {
        this(nickName, photoSource);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhotoSource() {
        return photoSource;
    }

    public void setPhotoSource(String photoSource) {
        this.photoSource = photoSource;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", photoSource='" + photoSource + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile profile)) return false;
        return id.equals(profile.id) && nickName.equals(profile.nickName) && photoSource.equals(profile.photoSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickName, photoSource);
    }



}
