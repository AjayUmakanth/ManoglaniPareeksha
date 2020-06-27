package com.example.manoglanipareeksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterPatient extends AppCompatActivity
{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "RegisterPatient";
    EditText name,phone,email,medicalHistory;
    TextView dob;
    ProgressBar progressBar;
    Spinner doctors;
    RadioGroup gender;
    Button register,selectDob;
    Calendar calender;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        medicalHistory = findViewById(R.id.medicalHistory);
        gender = findViewById(R.id.gender);
        progressBar = findViewById(R.id.progressBar3);
        dob= findViewById(R.id.dob);
        doctors = findViewById(R.id.consultant_doctor);
        selectDob = findViewById(R.id.selectDob);
        calender = Calendar.getInstance();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String doctorsList=prefs.getString("doctors",null);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,  Arrays.asList(doctorsList.split(",")));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctors.setAdapter(dataAdapter);
        selectDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = calender.get(Calendar.DAY_OF_MONTH);
                int month = calender.get(Calendar.MONTH);
                int year = calender.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(RegisterPatient.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        register = findViewById(R.id.button3);
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validateInput())
                    registerPatient();
                else
                    Toast.makeText(getApplicationContext(),"Please enter all fields",Toast.LENGTH_LONG).show();
            }
        });
    }
    boolean validateInput()
    {
        boolean valid=true;
        if(name.getText().toString().equals(""))
            valid = false;
        if(dob.getText().toString().equals(""))
            valid=false;
        if(phone.getText().toString().equals(""))
            valid=false;
        if(email.getText().toString().equals(""))
            valid=false;
        if(medicalHistory.getText().toString().equals(""))
            valid=false;
        if(gender.getCheckedRadioButtonId()==-1)
            valid=false;
        return valid;
    }
    void registerPatient()
    {

        final String name_ip,dob_ip,phone_ip,email_ip,medicalHistory_ip,doctor_ip,gender_ip;
        name_ip=name.getText().toString();
        dob_ip=dob.getText().toString();
        phone_ip=phone.getText().toString();
        email_ip=email.getText().toString();
        medicalHistory_ip=medicalHistory.getText().toString();
        doctor_ip=doctors.getSelectedItem().toString();
        gender_ip=((RadioButton)findViewById(gender.getCheckedRadioButtonId())).getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Map<String, String> patient = new HashMap<>();
        patient.put("name", name_ip);
        patient.put("dob", dob_ip);
        patient.put("phone", phone_ip);
        patient.put("email", email_ip);
        patient.put("gender", gender_ip);
        patient.put("consulting_doctor", doctor_ip);
        patient.put("hospital_id", prefs.getString("hospital_id",null));
        patient.put("medical_history", medicalHistory_ip);
        progressBar.setVisibility(View.VISIBLE);
        db.collection("patient")
                .add(patient)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        progressBar.setVisibility(View.INVISIBLE);

                        Intent intent = new Intent(RegisterPatient.this,PatientDetailsActivity.class);
                        intent.putExtra("id",documentReference.getId());
                        intent.putExtra("name", name_ip);
                        intent.putExtra("dob", dob_ip);
                        intent.putExtra("phone", phone_ip);
                        intent.putExtra("email", email_ip);
                        intent.putExtra("gender", gender_ip);
                        intent.putExtra("consulting_doctor ", doctor_ip);
                        intent.putExtra("medical_history", medicalHistory_ip);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}