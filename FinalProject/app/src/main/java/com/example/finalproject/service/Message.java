package com.example.finalproject.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message{
    @Expose
    @SerializedName("message")
    public String message;
    @Expose
    @SerializedName("user")
    public String user;
    @Expose
    @SerializedName("date")
    public long date;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", user='" + user + '\'' +
                ", date=" + date +
                '}';
    }
}
