package com.example.fivebetserio;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextDate;

    public static final String TAG = MainActivity.class.getName();

    private TextInputEditText editTextName, editTextSurname, editTextPassword, editTextEmail;

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


        Button registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {
            if (isEmailOk(editTextEmail.getText().toString())){
                if (isPasswordOk(editTextPassword.getText().toString())){
                    Intent intent = new Intent(this, MainPageActivity.class);
                    startActivity(intent);
                }
                else
                    editTextPassword.setError("Password needs to have at least 7 characters");

            }
            else
                editTextEmail.setError("Email is not correct");
            //Intent intent = new Intent(this, MainPageActivity.class);
            //startActivity(intent);
        });




        // Disabilita la tastiera (già fatto nell'XML)
        editTextDate.setFocusable(false);

        editTextDate.setOnClickListener(v -> {
            // Ottieni la data corrente per impostarla come valore iniziale
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Apri il DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegisterActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Mostra la data selezionata nell'EditText
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editTextDate.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
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