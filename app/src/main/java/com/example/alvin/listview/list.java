package com.example.alvin.listview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alvin.listview.MainActivity;
import com.example.alvin.listview.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class list extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private Button button, signout;
    private List<String> listString;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        auth = FirebaseAuth.getInstance();

        String uid = null;

        if (auth.getCurrentUser() != null){
            uid = auth.getCurrentUser().getUid();

            listString = new ArrayList<>();

            listView = findViewById(R.id.listview);
            editText = findViewById(R.id.edittext);
            button = findViewById(R.id.tambah);
            signout = findViewById(R.id.signout);

            final ArrayAdapter arrayAdapter = new ArrayAdapter<>(list.this,
                    android.R.layout.simple_list_item_1, listString);
            listView.setAdapter(arrayAdapter);

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            final DatabaseReference usersRef = database.getReference("users");
            final DatabaseReference uidRef = usersRef.child(uid);
            final DatabaseReference message = uidRef.child("message");
            signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(list.this)
                            .setCancelable(true)
                            .setMessage("Are you sure to logout?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    auth.signOut();
                                    startActivity(new Intent(list.this, MainActivity.class));
                                    finish();
                                }
                            }).show();
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO : Tambahkan isi dari editText ke database
                    message.push().child("task").setValue(editText.getText().toString());
                    editText.setText("");
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(arrayAdapter.getCount() - 1);
                        }
                    });
                }
            });
            message.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // TODO : Hapus isi list
                    listString.clear();

                    // TODO : iterasi dataSnapshot untuk mengambil isi dari child yang key-nya random
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        // TODO : /smk-coding/message/- - - - -/task
                        // TODO : ubah objek yang didapat menjadi String
                        listString.add((String) ds.child("task").getValue());

                        // TODO : update adapter listView
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            startActivity(new Intent(list.this, MainActivity.class));
            finish();
        }


    }
}

