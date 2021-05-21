package com.example.finalproject.service;

import java.util.Objects;

public class User {
    private String login;
    private int password;
    private boolean logged_in;

    public User(String login, String password) {
        this.login = login;
        this.password = password.hashCode();
        logged_in = false;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    public boolean try_password(String password) {
        if (password.hashCode() == this.password)
            return true;
        return false;
    }

    public void log_in() {
        logged_in = true;
    }

    public void log_out() {
        logged_in = false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getLogin(), user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin());
    }

    public boolean isLogged_in() {
        return logged_in;
    }
}
