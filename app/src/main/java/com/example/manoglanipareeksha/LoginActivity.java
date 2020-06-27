package com.example.manoglanipareeksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "LoginActivity";
    ProgressBar progressBar;
    EditText password,username;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                if(username.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_LONG).show();
                else if(password.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_LONG).show();
                else
                    login();
            }
        });
    }
    public void login()
    {
        // TODO Auto-generated method stub
        String username_ip = username.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        db.collection("admin")
                .whereEqualTo("username", username_ip)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if(task.getResult().size()==0)
                                Toast.makeText(getApplicationContext(),"Invalid Username",Toast.LENGTH_LONG).show();
                            else
                            {
                                String password_ip = password.getText().toString();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    if(password_ip.equals(document.get("password").toString()))
                                    {
                                        ArrayList<String> doctors = (ArrayList<String>) document.get("doctors");
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        prefs.edit().putString("hospital_id", document.getId()).commit();
                                        prefs.edit().putString("doctors", TextUtils.join(",", doctors)).commit();
                                        Intent intent = new Intent(LoginActivity.this,SelectPatientActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(getApplicationContext(),"Error connecting!! Try again!!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
