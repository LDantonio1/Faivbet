package com.example.fivebetserio.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fivebetserio.R;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName(); //una sorta di ID per identificare questa classe

    //val contextView = findViewById<View>(R.id.context_view); roba a caso, dovrebbe essere inutile, mentre cerco di capirlo lasciate cosi

    private TextInputEditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;


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
    } *///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail =  findViewById(R.id.login_email); // dichiarazione variabili contenenti login e password
        editTextPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_Button); // dichiaro una variabile collegata al bottone login
        Button registerButton = findViewById(R.id.register_Button);
        ImageButton loginGoogleButton = findViewById(R.id.loginGoogle_Button);

        loginButton.setOnClickListener(view -> {
            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());//quando viene premuto il tasto login fa quello che c'e dentro la graffa
            if (isEmailOk(email)){  //verifica che la mail rispetti tutti i parametri
                if (isPasswordOk(password)){  //stessa cosa della riga sopra ma per la password

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Login Successfull",
                                                Toast.LENGTH_SHORT).show();
                                        // Passa alla pagina principale
                                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    editTextPassword.setError("password is not correct"); //messaggion di errore
            }
            else
                // qui sotto c'e il codice della snackbar in caso di errore della mail, se preferite usiamo questa, ho usato .setError perche mi sembra leggermente piu chiaro
                //Snackbar.make(findViewById(R.id.main),"errore nei dati", Snackbar.LENGTH_SHORT).show();
                editTextEmail.setError("email is not correct");

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