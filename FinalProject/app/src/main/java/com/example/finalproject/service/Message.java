package com.example.finalproject.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;

public class Message{
    @Expose
    @SerializedName("message")
    public String message;
    @Expose
    @SerializedName("user")
    public String user;
    @Expose
    @SerializedName("date")
    public Date date;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", user='" + user + '\'' +
                ", date=" + date +
                '}';
    }
}

