package com.denite.whattodo.models;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;

    public User()
    {
    }

    public User(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "UserName: " + userName + "\n" +
                "Password: " + password;
    }
}
