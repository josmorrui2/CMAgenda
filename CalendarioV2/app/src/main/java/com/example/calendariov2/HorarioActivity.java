package com.example.calendariov2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class HorarioActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TableRow tableRow;
    EditText txtCell;
    ImageButton btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        tableLayout = findViewById(R.id.tbHorario);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        AddRow(tableLayout);
    }

    public void AddRow(View view) {

        TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow = new TableRow(this);
        tableRow.setLayoutParams(tableParams);

        for (int j = 0; j < 6; j++)
        {
            txtCell = new EditText(this);
            txtCell.setGravity(Gravity.CENTER);
            tableParams.weight = 1;
            txtCell.setLayoutParams(tableParams);
            txtCell.setBackgroundResource(R.drawable.horario_style);
            tableRow.addView(txtCell);
        }
        btnEliminar = new ImageButton(this);
        btnEliminar.setImageResource(R.drawable.ic_outline_delete_24);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer ultimo = tableLayout.getChildCount();
                tableLayout.removeView((View) v.getParent());
            }
        });
        tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        tableParams.weight=1;
        btnEliminar.setBackgroundResource(R.drawable.horario_style);
        btnEliminar.setLayoutParams(tableParams);
        tableRow.addView(btnEliminar);
        tableLayout.addView(tableRow);
    }
}