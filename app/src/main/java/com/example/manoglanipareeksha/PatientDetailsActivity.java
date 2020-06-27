package com.example.manoglanipareeksha;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PatientDetailsActivity extends AppCompatActivity {
    TextView id,name,age,phone,email,medicalHistory,consultant_doctor,gender;
    String TAG="PatientDetailsActivity";
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        Intent intent = getIntent();
        final String id_value = intent.getStringExtra("id");
        String name_value = intent.getStringExtra("name");
        String dob = intent.getStringExtra("dob");
        String phone_value = intent.getStringExtra("phone");
        String email_value = intent.getStringExtra("email");
        String gender_value = intent.getStringExtra("gender");
        String consultant_doctor_value = intent.getStringExtra("consulting_doctor");
        String medical_history_value = intent.getStringExtra("medical_history");

        id = findViewById(R.id.ref);
        id.setText(id_value);

        name = findViewById(R.id.name);
        name.setText(name_value);

        age = findViewById(R.id.age);
        age.setText(getAge(dob));

        gender = findViewById(R.id.gender);
        gender.setText(gender_value);

        phone = findViewById(R.id.phone);
        phone.setText(phone_value);

        email = findViewById(R.id.email);
        email.setText(email_value);

        consultant_doctor = findViewById(R.id.consultant_doctor);
        consultant_doctor.setText(consultant_doctor_value);

        medicalHistory = findViewById(R.id.medicalHistory);
        medicalHistory.setText(medical_history_value);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().putString("email", email_value).commit();
        prefs.edit().putString("id", id_value).commit();

        start=findViewById(R.id.button2);
        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PatientDetailsActivity.this,QuestionnaireActivity.class);
                intent.putExtra("id",id_value);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientDetailsActivity.this,QuestionnaireActivity.class);
        startActivity(intent);
    }
    public String getAge(String userDob) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(Integer.valueOf(userDob.substring(6)), Integer.valueOf(userDob.substring(3,5))-1, Integer.valueOf(userDob.substring(0,2)));

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Log.v(TAG,Integer.toString(age));
        return Integer.toString(age);
    }
}
