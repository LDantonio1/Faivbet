package com.example.fivebetserio.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.fivebetserio.R;
import com.example.fivebetserio.repository.user.IUserRepository;
import com.example.fivebetserio.ui.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextDate;

    public static final String TAG = MainActivity.class.getName();

    private TextInputEditText editTextName, editTextSurname, editTextPassword, editTextEmail;

    int year,month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ti ho dichiarato tutti gli editText della pagina register cosi non devi farlo, in teoria sai gia cosa contengono
        editTextName = findViewById(R.id.register_name);
        editTextSurname = findViewById(R.id.register_surname);
        editTextPassword = findViewById(R.id.register_password);
        editTextEmail = findViewById(R.id.register_email);
        editTextDate = findViewById(R.id.register_date);

        ImageButton backButton = findViewById(R.id.back_button_register);
        Button registerButton = findViewById(R.id.register_button);

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            //animazione personalizzata
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Disabilita la tastiera (già fatto nell'XML)
        editTextDate.setFocusable(false);

        //gestisce la selezione della data di nascita
        editTextDate.setOnClickListener(v -> {
            // Ottieni la data corrente per impostarla come valore iniziale
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            // Apri il DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegisterActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        year = selectedYear;
                        month = selectedMonth + 1; // I mesi partono da 0, quindi aggiungiamo 1
                        day = selectedDay;
                        // Mostra la data selezionata nell'EditText
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editTextDate.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });



        //controlla se la password e la mail sono valide e controlla che l'utente sia maggiorenne
        registerButton.setOnClickListener(view -> {
            if (isEmailOk(editTextEmail.getText().toString())){
                if (isPasswordOk(editTextPassword.getText().toString())){
                    if (isAdult(year, month, day)){
                        Intent intent = new Intent(this, MainPageActivity.class);
                        startActivity(intent);
                    }
                    else
                        editTextDate.setError("non sei maggiorenne");
                }
                else
                    editTextPassword.setError("Password needs to have at least 7 characters");

            }
            else
                editTextEmail.setError("Email is not correct");
        });



    }

    //classe per controllare se l'utente è maggiorenne
    public boolean isAdult(int year, int month, int day) {
        // Ottieni la data attuale
        Calendar today = Calendar.getInstance();
        int currentYear = today.get(Calendar.YEAR);
        int currentMonth = today.get(Calendar.MONTH) + 1; // I mesi partono da 0
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        // Calcola la differenza degli anni
        int age = currentYear - year;

        // Verifica il mese e il giorno per aggiustare il calcolo
        if (currentMonth < month || (currentMonth == month && currentDay < day)) {
            age--; // L'utente non ha ancora compiuto gli anni
        }

        // Ritorna true se ha almeno 18 anni
        return age >= 18;
    }

    private boolean isEmailOk(String email){
        return EmailValidator.getInstance().isValid(email);  //libreria esterna che fa da sola il controllo per la mail
    }

    private boolean isPasswordOk(String password){
        return password.length() > 7;
    }

    //cercherò qualche libreria per fare in controllo dell'età, si potrebbe fare a mano ma non mi piace troppo
    private boolean isDateOk(){
        return false;
    }
}