package com.example.fivebetserio.util;

import android.app.Application;

import com.example.fivebetserio.repository.user.IUserRepository;
import com.example.fivebetserio.repository.user.UserRepository;
import com.example.fivebetserio.source.user.BaseUserAuthenticationRemoteDataSource;
import com.example.fivebetserio.source.user.BaseUserDataRemoteDataSource;
import com.example.fivebetserio.source.user.UserAuthenticationFirebaseDataSource;
import com.example.fivebetserio.source.user.UserFirebaseDataSource;
import com.google.android.gms.common.util.SharedPreferencesUtils;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    /**
     * Returns an instance of ServiceLocator class.
     * @return An instance of ServiceLocator.
     */
    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

}
