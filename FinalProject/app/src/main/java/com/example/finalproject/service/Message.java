package com.example.finalproject.service;

public class Message{

    public String message;

    public String user;

    public long date;

    public Message (){}

    public Message(String message, String user, long date) {
        this.message = message;
        this.user = user;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", user='" + user + '\'' +
                ", date=" + date +
                '}';
    }
}

