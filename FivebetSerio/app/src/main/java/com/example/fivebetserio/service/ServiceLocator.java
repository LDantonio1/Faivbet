package com.example.fivebetserio.service;

import android.app.Application;

import com.example.fivebetserio.R;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.repository.user.IUserRepository;
import com.example.fivebetserio.repository.user.UserRepository;
import com.example.fivebetserio.source.user.BaseUserAuthenticationRemoteDataSource;
import com.example.fivebetserio.source.user.BaseUserDataRemoteDataSource;
import com.example.fivebetserio.source.user.UserAuthenticationFirebaseDataSource;
import com.example.fivebetserio.source.user.UserFirebaseDataSource;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.SharedPreferencesUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {
    }

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .build();
                return chain.proceed(request);
            })
            .build();

    public LeagueAPIService getLeagueAPIService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Mostra body della richiesta e risposta

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging) // Aggiunge il logging al client
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(LeagueAPIService.class);
    }

    public MatchAPIService getMatchAPIService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Mostra body della richiesta e risposta

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging) // Aggiunge il logging al client
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(MatchAPIService.class);
    }

    public LeaguesRoomDatabase getLeaguesDB(Application application) {
        return LeaguesRoomDatabase.getInstance(application);
    }


    public IUserRepository getUserRepository(Application application) {
        SharedPreferencesUtils sharedPreferencesUtil = new SharedPreferencesUtils(application);

        BaseUserAuthenticationRemoteDataSource userRemoteAuthenticationDataSource =
                new UserAuthenticationFirebaseDataSource();

        BaseUserDataRemoteDataSource userDataRemoteDataSource =
                new UserFirebaseDataSource(sharedPreferencesUtil);

        return new UserRepository(userRemoteAuthenticationDataSource,
                userDataRemoteDataSource);
    }

}
