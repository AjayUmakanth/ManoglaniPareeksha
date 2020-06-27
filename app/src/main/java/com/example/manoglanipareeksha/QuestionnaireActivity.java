package com.example.manoglanipareeksha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionnaireActivity extends AppCompatActivity {
    RadioGroup[] radioGroup = new RadioGroup[9];
    Button finish;
    String TAG="QuestionnaireActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questionnaire);
        radioGroup[0]=findViewById(R.id.options1);
        radioGroup[1]=findViewById(R.id.options2);
        radioGroup[2]=findViewById(R.id.options3);
        radioGroup[3]=findViewById(R.id.options4);
        radioGroup[4]=findViewById(R.id.options5);
        radioGroup[5]=findViewById(R.id.options6);
        radioGroup[6]=findViewById(R.id.options7);
        radioGroup[7]=findViewById(R.id.options8);
        radioGroup[8]=findViewById(R.id.options9);
        finish=findViewById(R.id.button4);
        Intent intent= getIntent();
        TextView ref=findViewById(R.id.reference_id);
        ref.setText("ID: "+intent.getStringExtra("id"));

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScore();
            }
        });
    }
    void getScore()
    {
        int score=0;
        for(int i=0;i<9;i++)
        {
            int selectedId = radioGroup[i].getCheckedRadioButtonId();
            if(selectedId==-1)
            {
                Toast.makeText(getApplicationContext(),"Please Select All Options",Toast.LENGTH_LONG).show();
                return;
            }
            score+=Integer.parseInt(((RadioButton)findViewById(selectedId)).getText().toString());
        }
        Log.v(TAG,"Score="+score);
        Intent intent = new Intent(QuestionnaireActivity.this, ResultActivity.class);
        intent.putExtra("score",Integer.toString(score));
        startActivity(intent);
    }
}
