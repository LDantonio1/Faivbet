package com.example.fivebetserio.repository.user;

import androidx.lifecycle.MutableLiveData;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.model.User;
import com.example.fivebetserio.source.user.BaseUserAuthenticationRemoteDataSource;
import com.example.fivebetserio.source.user.BaseUserDataRemoteDataSource;

public class UserRepository implements IUserRepository, UserResponseCallback{

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final MutableLiveData<Result> userMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource
                          ) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password); // Effettua il login se l'utente è registrato
        } else {
            signUp(email, password); // Registra un nuovo utente
        }
        return userMutableLiveData;
    }

    @Override // Effettua il logout
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override // Restituisce l'utente attualmente loggato
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override // Registra un nuovo utente
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override // Effettua il login
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override // Salva i dati dell'utente autenticato
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override // Notifica un errore di autenticazione
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override // Notifica il successo nel recupero dati utente
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserSuccess result = new Result.UserSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override // Notifica un errore nel recupero dati utente
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
       // Metodo vuoto, da implementare negli sviluppi futuri per notificare il logout
    }
}
