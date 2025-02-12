package com.example.fivebetserio.util;

import android.app.Application;
import com.example.fivebetserio.R;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.repository.Repository;
import com.example.fivebetserio.repository.user.IUserRepository;
import com.example.fivebetserio.repository.user.UserRepository;
import com.example.fivebetserio.service.LeagueAPIService;
import com.example.fivebetserio.service.MatchAPIService;
import com.example.fivebetserio.source.league.BaseLeagueLocalDataSource;
import com.example.fivebetserio.source.league.BaseLeagueRemoteDataSource;
import com.example.fivebetserio.source.league.LocalLeagueDataSource;
import com.example.fivebetserio.source.league.MockLeagueDataSource;
import com.example.fivebetserio.source.league.RemoteLeagueDataSource;
import com.example.fivebetserio.source.match.BaseMatchLocalDataSource;
import com.example.fivebetserio.source.match.BaseMatchRemoteDataSource;
import com.example.fivebetserio.source.match.LocalMatchDataSource;
import com.example.fivebetserio.source.match.MockMatchDataSource;
import com.example.fivebetserio.source.match.RemoteMatchDataSource;
import com.example.fivebetserio.source.user.BaseUserAuthenticationRemoteDataSource;
import com.example.fivebetserio.source.user.BaseUserDataRemoteDataSource;
import com.example.fivebetserio.source.user.UserAuthenticationFirebaseDataSource;
import com.example.fivebetserio.source.user.UserFirebaseDataSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
/**
 * Service Locator per la gestione delle dipendenze dell'applicazione.
 *
 * Responsabilità principali:
 * - Fornire istanze singleton delle classi principali come Repository e Database.
 *
 * Questa classe segue il pattern Service Locator per centralizzare la creazione e il recupero delle istanze
 * necessarie all'applicazione.
 */

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

    public LeaguesRoomDatabase getMatchDB(Application application) {
        return LeaguesRoomDatabase.getInstance(application);
    }

    public Repository getRepository(Application application, boolean debugMode) {
        BaseLeagueRemoteDataSource remoteDataSource;
        BaseLeagueLocalDataSource localDataSource;
        BaseMatchRemoteDataSource matchRemoteDataSource;
        BaseMatchLocalDataSource matchLocalDataSource;
        SharedPreferencesUtils sharedPreferencesUtil = new SharedPreferencesUtils(application);

        if (debugMode) {
            JSONParserUtils jsonParserUtil = new JSONParserUtils(application.getAssets());
            remoteDataSource = new MockLeagueDataSource(jsonParserUtil);
            matchRemoteDataSource = new MockMatchDataSource(jsonParserUtil);
        } else {
            remoteDataSource = new RemoteLeagueDataSource(application.getString(R.string.api_key));
            matchRemoteDataSource = new RemoteMatchDataSource(application.getString(R.string.api_key));
        }

        localDataSource = new LocalLeagueDataSource(getLeaguesDB(application), sharedPreferencesUtil);
        matchLocalDataSource = new LocalMatchDataSource(getMatchDB(application));

        return new Repository(remoteDataSource, localDataSource, matchRemoteDataSource, matchLocalDataSource);
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
