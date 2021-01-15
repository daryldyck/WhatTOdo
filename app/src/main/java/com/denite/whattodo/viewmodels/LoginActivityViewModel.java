package com.denite.whattodo.viewmodels;

import android.app.Application;


import com.denite.whattodo.models.User;
import com.denite.whattodo.repositories.UserRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginActivityViewModel extends AndroidViewModel
{
    private final String TAG = "LoginActivityViewModel";
    private MutableLiveData<List<User>> userList;
    private UserRepository userRepository;
    private Application application;
    private boolean isRememberMe;
    private User currentUser;

    public LoginActivityViewModel(@NonNull Application application)
    {
        super(application);
        this.application = application;
    }

    // load user data from repositories
    public void init()
    {
        if (userList != null)
        {
            return;
        }
        userRepository = UserRepository.getInstance();
        userList = userRepository.getUserArrayList(application);
    }

    public LiveData<List<User>> getUsersList()
    {
        return userList;
    }

    public boolean isRememberMe()
    {
        return isRememberMe;
    }

    public void setRememberMe(boolean rememberMe)
    {
        isRememberMe = rememberMe;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }
}
