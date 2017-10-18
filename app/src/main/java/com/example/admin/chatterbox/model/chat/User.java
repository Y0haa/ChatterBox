package com.example.admin.chatterbox.model.chat;

import java.util.List;

/**
 * Created by admin on 10/10/2017.
 */

public class User {
    String id;
    String name, username, email, phoneNumber;
    List<Group> createdGroups, joinedGroups;
    List<User> contacts;
    String userImage;

    public User() {
    }

    public User(String id, String name, String username, String email, String phoneNumber,
                List<Group> createdGroups, List<Group> joinedGroups, List<User> contacts, String userImage) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdGroups = createdGroups;
        this.joinedGroups = joinedGroups;
        this.contacts = contacts;
        this.userImage = userImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Group> getCreatedGroups() {
        return createdGroups;
    }

    public void setCreatedGroups(List<Group> createdGroups) {
        this.createdGroups = createdGroups;
    }

    public List<Group> getJoinedGroups() {
        return joinedGroups;
    }

    public void setJoinedGroups(List<Group> joinedGroups) {
        this.joinedGroups = joinedGroups;
    }

    public List<User> getContacts() {
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
