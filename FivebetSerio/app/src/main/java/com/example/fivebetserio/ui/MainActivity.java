package com.example.fivebetserio.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fivebetserio.R;
import com.example.fivebetserio.repository.user.IUserRepository;
import com.example.fivebetserio.ui.viewmodel.UserViewModel;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.apache.commons.validator.routines.EmailValidator;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName(); //una sorta di ID per identificare questa classe

    //val contextView = findViewById<View>(R.id.context_view); roba a caso, dovrebbe essere inutile, mentre cerco di capirlo lasciate cosi

    private TextInputEditText editTextEmail, editTextPassword;

    ////////////////////////
    /*

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;
    private UserViewModel userViewModel;

    */ //////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////////////////


        editTextEmail =  findViewById(R.id.login_email); // dichiarazione variabili contenenti login e password
        editTextPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_Button); // dichiaro una variabile collegata al bottone login
        Button registerButton = findViewById(R.id.register_Button);
        ImageButton loginGoogleButton = findViewById(R.id.loginGoogle_Button);

        loginButton.setOnClickListener(view -> { //quando viene premuto il tasto login fa quello che c'e dentro la graffa
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
            /*if (isEmailOk(editTextEmail.getText().toString())){  //verifica che la mail rispetti tutti i parametri
                if (isPasswordOk(editTextPassword.getText().toString())){  //stessa cosa della riga sopra ma per la password
                    // gli intent permettono di navigare fra le activity quindi fra le farie ""schermate"" dell'app
                    Intent intent = new Intent(this, MainPageActivity.class);
                    startActivity(intent);// startActivity richiama la riga sopra e quando viene premuto il tasto login va in MainActivity

                    //reminder solo per me (MK), minuto 36 esercitazione 7/11 spiega come inviare un messaggio predefinito usando un'app esterna
                }
                else
                    editTextPassword.setError("password is not correct"); //messaggion di errore
            }

            else
                // qui sotto c'e il codice della snackbar in caso di errore della mail, se preferite usiamo questa, ho usato .setError perche mi sembra leggermente piu chiaro
                //Snackbar.make(findViewById(R.id.main),"errore nei dati", Snackbar.LENGTH_SHORT).show();
                editTextEmail.setError("email is not correct");
*/
        });

        // se clicchi il bottone register ti manda alla schermata di registrazione
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });





    }

    private boolean isEmailOk(String email){
        return EmailValidator.getInstance().isValid(email);  //libreria esterna che fa da sola il controllo per la mail
    }

    private boolean isPasswordOk(String password){
        return password.length() > 7;  //controllo a caso giusto per provare, poi vedremo di fare qualche controllo serio tramite qualche libreria
    }
}