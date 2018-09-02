package com.example.alvin.listview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alvin.listview.R;
import com.example.alvin.listview.list;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnCompleteListener(
                        MainActivity.this,
                        new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TOKEN", task.getResult().getToken());
                                }
                            }
                        });

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, list.class);
            startActivity(intent);
            finish();
        }

        email = findViewById(R.id.mainEmailAddress);
        password = findViewById(R.id.mainPassword);

        login = findViewById(R.id.mainButtonLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO : get all values
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                Pattern VALID_EMAIL_ADDRESS_REGEX =
                        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                                Pattern.CASE_INSENSITIVE);

                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailText);

                // TODO : cek input
                if (TextUtils.isEmpty(emailText)) {
                    // TODO : cek email
                    email.setError("Email tidak boleh kosong");
                    email.requestFocus();
                } else if (!matcher.find()) {
                    // TODO : cek email Regexp
                    email.setError("Format email salah");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(passwordText)) {
                    // TODO : cek password
                    password.setError("Password tidak boleh kosong");
                    password.requestFocus();
                } else {
                    // TODO : Login

                    auth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();

                                        if (user != null) {
                                            Intent intent = new Intent(MainActivity.this, list.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
