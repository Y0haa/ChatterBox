package com.example.admin.chatterbox.model.chat;

import java.util.List;

/**
 * Created by admin on 10/10/2017.
 */

public class Chat {
    int id;
    String name, username, email, phoneNumber;
    List<Group> createdGroups, joinedGroups;
    List<User> contacts;
}
