package com.example.watcher.data;

public class User {
    private String userName;
    private String passwd;
    private String token;
    private static User instance;

    public User(){
        userName = "";
        passwd = "";
        token = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }
}
