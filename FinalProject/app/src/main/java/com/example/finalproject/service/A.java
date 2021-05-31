package com.example.finalproject.service;


import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface A{
    @GET("/get_messages")
    Call<ArrayList<String>> get_messages();

    @POST("/put_message")
    Call<String> put_message(@Query("message") String message);

    @GET("/is_new_message")
    Call<Boolean> is_new_message(@Query("login") String login);

    @POST("/register")
    Call<String> register(@Query("login") String login, @Query("password") String password);

    @POST("/log_in")
    Call<String> log_in(@Query("login") String login, @Query("password") String password);

    @POST("/log_out")
    Call<String> log_out(@Query("login") String login);

    @GET("/is_registered")
    Call<Boolean> is_registered(@Query("login") String login);

    @GET("/is_logged_in")
    Call<Boolean> is_logged_in(@Query("login") String login);
}
