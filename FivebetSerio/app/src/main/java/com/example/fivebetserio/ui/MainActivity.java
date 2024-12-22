package com.example.fivebetserio.ui;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fivebetserio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName(); //una sorta di ID per identificare questa classe

    //val contextView = findViewById<View>(R.id.context_view); roba a caso, dovrebbe essere inutile, mentre cerco di capirlo lasciate cosi

    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        intent = new Intent(this, MainPageActivity.class);

        editTextEmail = findViewById(R.id.login_email);// declaration of variables containing login and password
        editTextPassword = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_Button);// I declare a variable linked to the login button
        Button registerButton = findViewById(R.id.register_Button);
        ImageButton loginGoogleButton = findViewById(R.id.loginGoogle_Button);//when the login button is pressed you are logged in

        //when the login button is pressed you are logged in
        loginButton.setOnClickListener(view -> {
            signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString());
        });

        // if you click the register button it sends you to the registration screen
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        loginGoogleButton.setOnClickListener(view -> {

        });

    }

    private void reload() { }

    //the whole login process (copied from firebase site, no idea what's in there)
    private void signInWithEmailAndPassword(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void updateUI(FirebaseUser user) {

    }
}