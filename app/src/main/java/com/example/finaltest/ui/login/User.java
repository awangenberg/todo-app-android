package com.example.finaltest.ui.login;

public class User {
    private String nickName;
    private String email;

    public User(String nickName, String email){
        this.nickName = nickName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
