package com.example.fivebetserio.repository.user;

import androidx.lifecycle.MutableLiveData;

import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.model.User;
public interface IUserRepository {

    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> logout();
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);

    void saveUserPreferences();

}
