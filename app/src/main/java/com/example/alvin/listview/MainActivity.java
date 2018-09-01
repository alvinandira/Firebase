package com.example.alvin.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.mainEmail);
        password = findViewById(R.id.mainPassword);
        login = findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                if (TextUtils.isEmpty(emailText)){
                    email.setError("Email Harus Diisi");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(passwordText)){
                    password.setError("Password tidak boleh kosong");
                    password.requestFocus();
                } else {
                    auth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(MainActivity.this, (task) -> {
                                if (task.isSuccessful()){
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user !=null) {
                                        Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText( MainActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                                    }
                                } else Toast.makeText(MainActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

    }
}
