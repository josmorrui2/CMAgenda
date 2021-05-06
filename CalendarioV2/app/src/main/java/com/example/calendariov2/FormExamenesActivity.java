package com.example.calendariov2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FormExamenesActivity extends AppCompatActivity {

    Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_examenes);

        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent main = new Intent(getApplicationContext(), MainActivity.class);*/
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent main = new Intent(getApplicationContext(), MainActivity.class);*/
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}