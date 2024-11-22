package com.example.fivebetserio;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.validator.routines.EmailValidator;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName(); //una sorta di ID per identificare questa classe

    //val contextView = findViewById<View>(R.id.context_view); roba a caso, dovrebbe essere inutile, mentre cerco di capirlo lasciate cosi

    private TextInputEditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail =  findViewById(R.id.login_email); // dichiarazione variabili contenenti login e password
        editTextPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_Button); // dichiaro una variabile collegata al bottone login

        loginButton.setOnClickListener(view -> { //quando viene premuto il tasto login fa quello che c'e dentro la graffa
            if (isEmailOk(editTextEmail.getText().toString())){  //verifica che la mail rispetti tutti i parametri
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

        });

    }

    private boolean isEmailOk(String email){
        return EmailValidator.getInstance().isValid(email);  //libreria esterna che fa da sola il controllo per la mail
    }

    private boolean isPasswordOk(String password){
        return password.length() > 7;  //controllo a caso giusto per provare, poi vedremo di fare qualche controllo serio tramite qualche libreria
    }
}