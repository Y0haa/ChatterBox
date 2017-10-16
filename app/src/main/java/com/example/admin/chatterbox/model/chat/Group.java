package com.example.admin.chatterbox.model.chat;

import java.util.List;

/**
 * Created by admin on 10/10/2017.
 */

public class Group {
    List<User> users;
    List<Chat> posts;
    String title;
    String image;
    User admin;
    String id;

    public Group() {
    }

    public Group(List<User> users, List<Chat> posts, String title, String image, User admin) {
        this.users = users;
        this.posts = posts;
        this.title = title;
        this.image = image;
        this.admin = admin;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Chat> getPosts() {
        return posts;
    }

    public void setPosts(List<Chat> posts) {
        this.posts = posts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String  getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }
}
