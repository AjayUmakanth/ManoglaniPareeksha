package com.example.manoglanipareeksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class SelectPatientActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG= "SelectPatientActivity";
    ProgressBar progressBar;
    EditText referenceId;
    Button newPatient, getPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_patient);
        progressBar = findViewById(R.id.progressBar);
        referenceId = findViewById(R.id.reference_id);
        newPatient = findViewById(R.id.newPatient);
        getPatient = findViewById(R.id.getPatient);

        getPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username_ip = referenceId.getText().toString();
                if(username_ip.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter Patient ID",Toast.LENGTH_SHORT).show();;
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                db.collection("patient")
                        .document(username_ip)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Intent intent = new Intent(SelectPatientActivity.this, PatientDetailsActivity.class);
                                intent.putExtra("id",document.getId());
                                intent.putExtra("name",document.get("name").toString());
                                intent.putExtra("dob",document.get("dob").toString());
                                intent.putExtra("phone",document.get("phone").toString());
                                intent.putExtra("email",document.get("email").toString());
                                intent.putExtra("gender",document.get("gender").toString());
                                intent.putExtra("consulting_doctor",document.get("consulting_doctor").toString());
                                intent.putExtra("medical_history",document.get("medical_history").toString());
                                startActivity(intent);
                            }
                            else
                            {
                                Log.d(TAG, "No such document");
                                Toast.makeText(getApplicationContext(), "Patient not present", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(getApplicationContext(), "Cannot connect, Try Again!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        newPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPatientActivity.this, RegisterPatient.class);
                startActivity(intent);
            }
        });
    }

}

