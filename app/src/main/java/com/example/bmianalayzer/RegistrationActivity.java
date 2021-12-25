package com.example.bmianalayzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    TextView login;
    EditText userName, password, repassword, mail;
    Button create;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.userName);
        repassword = findViewById(R.id.repassword);
        mail = findViewById(R.id.mail);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        create = findViewById(R.id.create);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "add name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mail.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "add email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().equals("")) {
                    password.setError("password");
                    return;
                }

                if (!password.getText().toString().equals(repassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "password is wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = mail.getText().toString();
                String pass = password.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference reference = firebaseFirestore.collection("User")
                                            .document(firebaseAuth.getUid());
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Name", userName.getText().toString());
                                    user.put("E-mail", mail.getText().toString());
                                    user.put("Password", password.getText().toString());
                                    reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(getApplicationContext(), complete.class);
                                            intent.putExtra("userId",firebaseAuth.getUid());
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegistrationActivity.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}