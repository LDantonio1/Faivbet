package com.example.fivebetserio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    private TextInputEditText editTextName, editTextSurname, editTextPassword, editTextEmail, editTextDate;

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
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
        });

    }
}