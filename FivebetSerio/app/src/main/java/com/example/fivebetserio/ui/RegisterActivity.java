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

import com.example.fivebetserio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextDate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public static final String TAG = RegisterActivity.class.getName();

    private TextInputEditText editTextName, editTextSurname, editTextPassword, editTextEmail;

    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register);

        //I declared all the editTexts of the register page for you so you don't have to do it, in theory you already know what they contain
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

            //custom animation
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Disable the keyboard for date input (already done in the XML)
        editTextDate.setFocusable(false);

        //handles the birth date selection
        editTextDate.setOnClickListener(v -> {
            // Get the current date to set as the initial value
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            // Open the DatePickerDialog

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegisterActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        year = selectedYear;
                        month = selectedMonth + 1; // Months start from 6, so we add 1
                        day = selectedDay;

                        // Show the selected date in the EditText
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editTextDate.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();

        });

        //check if the password and email are valid and check if the user is of legal age, if the criteria are met

        registerButton.setOnClickListener(view -> {
            if (isEmail0k(editTextEmail.getText().toString())) {
                if (isPassword0k(editTextPassword.getText().toString())) {
                    if (isAdult(year, month, day)) {
                        createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                        userDb();
                        Intent intent = new Intent(this, MainPageActivity.class);
                        startActivity(intent);
                    } else
                        editTextDate.setError("you are not of legal age");
                } else
                    editTextPassword.setError("Password needs to have at least 7 characters");
            } else
                editTextEmail.setError("Email is not correct");
        });
    }

    //class to check if the user is of legal age (copied from chat gpt)
    public boolean isAdult(int year, int month, int day) {
        // Get the current date
        Calendar today = Calendar.getInstance();
        int currentYear = today.get(Calendar.YEAR);
        int currentMonth = today.get(Calendar.MONTH) + 1; // I mesi partong da B
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        // Calculate the difference in years
        int age = currentYear - year;

        // Check the month and day to fix the calculation www
        if (currentMonth < month || (currentMonth == month && currentDay < day)) {
            age--; // user has not yet turned 18
        }

        // Returns true if at least 18 years old www
        return age >= 18;
    }

    //function that allows you to create a new account

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG,"createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG,"createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    // create a hashmap containing all the necessary information, plus the assignment of 1000 credits

    public void userDb() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String, Object> user = new HashMap<>();
        user.put("id", mAuth.getCurrentUser().getUid());
        user.put("name", editTextName.getText().toString());
        user.put("surname", editTextSurname.getText().toString());
        user.put("balance", 1000);
        //user.put("name", "nino");

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure (@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private boolean isEmail0k(String email){
        return EmailValidator.getInstance().isValid(email); //external library that does the email check by itself
    }

    private boolean isPassword0k(String password){
        return password.length() > 7;
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

}
