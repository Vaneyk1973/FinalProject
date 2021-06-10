package com.example.finalproject.service;

public class Message{

    public String message;

    public String user;

    public long date;

    public int gmt;

    public Message (){}

    public Message(String message, String user, long date, int gmt) {
        this.message = message;
        this.user = user;
        this.date = date;
        this.gmt = gmt;
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

    public int getGmt() {
        return gmt;
    }

    public void setGmt(int gmt) {
        this.gmt = gmt;
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

