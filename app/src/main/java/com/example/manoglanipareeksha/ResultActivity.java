package com.example.manoglanipareeksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "ResultActivity",phq9_result,neurosky_result,patient_id;
    EditText neurosky;
    TextView phq9,id;
    Button mail,finish,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        neurosky = findViewById(R.id.neurosky);
        id = findViewById(R.id.id);
        phq9 = findViewById(R.id.phq9);
        mail=findViewById(R.id.mail);
        finish=findViewById(R.id.finish);
        cancel=findViewById(R.id.cancel);
        patient_id=prefs.getString("id",null);
        id.setText("ID: "+patient_id);
        Intent intent = getIntent();
        phq9_result=intent.getStringExtra("score");
        phq9.setText(phq9_result);
        mail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(neurosky.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Please enter neurosky result",Toast.LENGTH_SHORT).show();
                else {
                    storeResult();
                    sendMail();
                    mail.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.INVISIBLE);
                    finish.setVisibility(View.VISIBLE);
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ResultActivity.this, SelectPatientActivity.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ResultActivity.this, SelectPatientActivity.class);
                startActivity(intent);
            }
        });
    }
    public  void sendMail()
    {
        neurosky_result=neurosky.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String TO = prefs.getString("email",""), SUBJECT="Test Result", MESSAGE="PHQ9 Result: %s\nModel Result: %s";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, TO.split(","));
        email.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
        email.putExtra(Intent.EXTRA_TEXT, String.format(MESSAGE,phq9_result,neurosky_result));
        email.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
            Log.i(TAG, "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ResultActivity.this, "Sending mail failed", Toast.LENGTH_SHORT).show();
        }

    }
    public  void storeResult()
    {
        neurosky_result=neurosky.getText().toString();

        Map<String, Object> test = new HashMap<>();
        test.put("patient_id", patient_id);
        test.put("neurosky_result", neurosky_result);
        test.put("phq9_result", phq9_result);
        test.put("datetime", new Timestamp(new Date()));

        db.collection("tests")
                .add(test)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
