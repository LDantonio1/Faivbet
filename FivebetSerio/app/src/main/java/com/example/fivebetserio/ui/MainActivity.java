package com.example.fivebetserio.ui;

import static com.example.fivebetserio.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.example.fivebetserio.util.Constants.INVALID_USER_ERROR;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.fivebetserio.ui.home.viewModel.user.UserViewModel;
import com.example.fivebetserio.ui.home.viewModel.user.UserViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.fivebetserio.R;
import com.example.fivebetserio.model.Result;
import com.example.fivebetserio.model.User;
import com.example.fivebetserio.repository.user.IUserRepository;
import com.example.fivebetserio.util.ServiceLocator;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fivebetserio.ui.home.MainPageActivity;
import androidx.lifecycle.ViewModelProvider;
import org.apache.commons.validator.routines.EmailValidator;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializza il repository e il ViewModel per la gestione utente
        IUserRepository userRepository = ServiceLocator.getInstance()
                .getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this,
                new UserViewModelFactory(userRepository)
        ).get(UserViewModel.class);

        // Collega i campi di input alle viste XML
        editTextEmail =  findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_Button);
        Button registerButton = findViewById(R.id.register_Button);

        loginButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (isEmailOk(email) && isPasswordOk(password)) {
                // Effettua il login e attende il risultato dell'autenticazione
                userViewModel.getUserMutableLiveData(email, password, true)
                        .observe(this, authenticationResult -> {
                            if (authenticationResult.isSuccess()) {
                                User user = ((Result.UserSuccess) authenticationResult).getData();
                                userViewModel.setAuthenticationError(false);
                                // Passa alla schermata principale dopo il login
                                Intent intent = new Intent(this, MainPageActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                userViewModel.setAuthenticationError(true);
                                // Mostra un messaggio di errore con Snackbar
                                Snackbar.make(findViewById(android.R.id.content),
                                        getErrorMessage(((Result.Error) authenticationResult).getMessage()),
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // Mostra un messaggio di errore se email/password non sono validi
                Snackbar.make(findViewById(android.R.id.content),
                        "Password e/o Email sbagliati",
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        // cliccando il bottone manda alla schermata di registrazione
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case INVALID_CREDENTIALS_ERROR:
                return this.getString(R.string.error_password_login); // Errore credenziali errate
            case INVALID_USER_ERROR:
                return this.getString(R.string.error_email_login);  // Errore email non valida
            default:
                return this.getString(R.string.error_unexpected);// Errore generico
        }
    }

    private boolean isEmailOk(String email){
        return EmailValidator.getInstance().isValid(email);  // libreria per controllo mail
    }

    private boolean isPasswordOk(String password){
        return password.length() > 7;  // controllo per password
    }

}