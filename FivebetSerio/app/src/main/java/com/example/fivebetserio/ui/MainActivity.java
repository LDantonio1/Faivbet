package com.example.fivebetserio.ui;

import static com.example.fivebetserio.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.example.fivebetserio.util.Constants.INVALID_USER_ERROR;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.fivebetserio.R;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.model.User;
import com.example.fivebetserio.repository.user.IUserRepository;
// import com.unimib.worldnews.ui.home.HomeActivity;
import com.example.fivebetserio.ui.viewmodel.UserViewModel;
import com.example.fivebetserio.ui.viewmodel.UserViewModelFactory;
import com.example.fivebetserio.service.ServiceLocator;
import com.example.fivebetserio.util.SharedPreferencesUtils;




import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private UserViewModel userViewModel;

    /* Se l'utente ha giá fatto l'accesso precedentemente non richiede nuovamente il login
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(intent);
            finish();
        }
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializza il ViewModel
        IUserRepository userRepository = ServiceLocator.getInstance()
                .getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)
        ).get(UserViewModel.class);

        editTextEmail =  findViewById(R.id.login_email); // dichiarazione variabili contenenti login e password
        editTextPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_Button); // dichiaro una variabile collegata al bottone login
        Button registerButton = findViewById(R.id.register_Button);
        ImageButton loginGoogleButton = findViewById(R.id.loginGoogle_Button);

        loginButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (isEmailOk(email) && isPasswordOk(password)) {
                // Tentativo di login con utente registrato
                userViewModel.getUserMutableLiveData(email, password, true)
                        .observe(this, authenticationResult -> {
                            if (authenticationResult.isSuccess()) {
                                User user = ((Result.UserSuccess) authenticationResult).getData();
                                Log.i("Login", "Logged in as: " + user.getEmail());
                                userViewModel.setAuthenticationError(false);
                                // retrieveUserInformationAndStartActivity(user); va aggiornato con la parte del puma
                                Intent intent = new Intent(this, MainPageActivity.class); // Imposta la tua activity successiva
                                startActivity(intent); // Avvia l'activity successiva
                                finish(); // Se vuoi chiudere l'activity corrente (opzionale)

                            } else {
                                userViewModel.setAuthenticationError(true);
                                // Se il login fallisce, mostra un errore
                                Snackbar.make(findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) authenticationResult).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Password e/o Email sbagliati",
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        // se clicchi il bottone register ti manda alla schermata di registrazione
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });


    }

    /*

    serve per recuperare i preferiti solo che mancano i metodi quindi lo faremo
    private void retrieveUserInformationAndStartActivity(User user) {
        userViewModel.getUserPreferences(user.getIdToken()).observe(
                this, userPreferences -> {
                    Intent intent = new Intent(this, MainPageActivity.class);
                    startActivity(intent);
                    // finish();  // Se vuoi chiudere l'activity attuale
                }
        );
    }

     */


    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case INVALID_CREDENTIALS_ERROR:
                return this.getString(R.string.error_password_login);
            case INVALID_USER_ERROR:
                return this.getString(R.string.error_email_login);
            default:
                return this.getString(R.string.error_unexpected);
        }
    }

    private boolean isEmailOk(String email){
        return EmailValidator.getInstance().isValid(email);  //libreria esterna che fa da sola il controllo per la mail
    }

    private boolean isPasswordOk(String password){
        return password.length() > 7;  //controllo a caso giusto per provare, poi vedremo di fare qualche controllo serio tramite qualche libreria
    }

    // Metodo per avviare la HomeActivity dopo il login
    private void startNextActivity(User user) {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.putExtra("USER_EMAIL", user.getEmail());
        startActivity(intent);
        finish(); // Chiude l'Activity attuale
    }
}