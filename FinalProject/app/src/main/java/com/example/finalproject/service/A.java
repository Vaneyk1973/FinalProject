package com.example.finalproject.service;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface A{
    @GET("/get_messages")
    Call<ArrayList<Message>> get_messages();

    @POST("/put_message")
    Call<String> put_message(@Query("message") String message);

    @POST("/register")
    Call<String> register(@Query("login") String login, @Query("password") String password);

    @POST("/log_in")
    Call<String> log_in(@Query("login") String login, @Query("password") String password);

    @POST("/log_out")
    Call<String> log_out();
}
