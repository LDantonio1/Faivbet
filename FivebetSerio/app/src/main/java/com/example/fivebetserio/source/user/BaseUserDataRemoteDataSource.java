package com.example.fivebetserio.source.user;

import com.example.fivebetserio.model.User;
import com.example.fivebetserio.repository.user.UserResponseCallback;

public abstract class BaseUserDataRemoteDataSource {

    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);

}
