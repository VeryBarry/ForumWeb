package com.company;

/**
 * Created by VeryBarry on 10/11/16.
 */
public class User {
    int id;
    String userName;
    String passWord;

    public User(int id, String userName, String passWord) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
    }

    public User(String username, String password) {
        this.userName = username;
        this.passWord = password;
    }
}
