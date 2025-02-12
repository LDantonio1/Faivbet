package com.example.fivebetserio.source.user;

import static com.example.fivebetserio.util.Constants.*;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.example.fivebetserio.model.User;

public class UserAuthenticationFirebaseDataSource extends BaseUserAuthenticationRemoteDataSource{

    private final FirebaseAuth firebaseAuth; // Inizializza FirebaseAuth

    public UserAuthenticationFirebaseDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public User getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); // Ottieni l'utente loggato
        if (firebaseUser == null) { // Se non c'è nessun utente loggato, ritorna null
            return null;
        } else {
            // Ritorna i dati dell'utente
            return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid());
        }
    }

    @Override
    public void logout() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this); // Rimuovi il listener dopo il logout
                    userResponseCallback.onSuccessLogout(); // Notifica il successo del logout
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener); // Aggiungi il listener per il cambiamento di stato di autenticazione
        firebaseAuth.signOut(); // Effettua il logout
    }

    @Override
    public void signUp(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    userResponseCallback.onSuccessFromAuthentication( // Notifica il successo con i dati utente
                            new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid())
                    );
                } else {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException())); // Gestisce errore
                }
            } else {
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException())); // Gestisce errore
            }
        });
    }

    @Override
    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    userResponseCallback.onSuccessFromAuthentication( // Notifica il successo con i dati utente
                            new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid())
                    );
                } else {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException())); // Gestisce errore
                }
            } else {
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException())); // Gestisce errore
            }
        });
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return WEAK_PASSWORD_ERROR; // Errore di password debole
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return INVALID_CREDENTIALS_ERROR; // Errore di credenziali non valide
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return INVALID_USER_ERROR; // Errore di utente non valido
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return USER_COLLISION_ERROR; // Errore di collisione utente
        }
        return UNEXPECTED_ERROR; // Errore generico
    }
}
