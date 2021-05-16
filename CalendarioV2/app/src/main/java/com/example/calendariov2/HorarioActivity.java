package com.example.calendariov2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class HorarioActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TableRow tableRow;
    EditText txtCell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        tableLayout = findViewById(R.id.tbHorario);

        AddRow(tableLayout);
    }

    public void AddRow(View view) {

        TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableRow = new TableRow(this);
        tableRow.setLayoutParams(tableParams);
        tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        for (int j = 0; j < 5; j++)
        {
            txtCell = new EditText(this);
            txtCell.setGravity(Gravity.CENTER);
            txtCell.setLayoutParams(tableParams);
            txtCell.setHint("" + j);
            tableRow.addView(txtCell);
        }
        tableLayout.addView(tableRow);
    }
}