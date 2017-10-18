package com.example.admin.chatterbox.model.chat;

/**
 * Created by admin on 10/10/2017.
 */

public class Chat {
    String post;
    String owner;
    String ownerId;
    Long time;
    String ownerImg;

    public Chat() {
    }

    public Chat(String post, String owner, String id, Long time) {
        this.post = post;
        this.owner = owner;
        ownerId = id;
        this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
