package com.example.aplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    private EditText mEditTestNombre;
    private EditText mEditTestApellidos;
    private EditText mEditTestEmail;
    private EditText mEditTestContraseña;
    private EditText mEditTestRepiteContraseña;
    private EditText mEditTestFechaNacimiento;
    private Button mButtonGo;

    //Variables
    private String nombre;
    private String apellidos;
    private String email;
    private String contraseña;
    private String repiteContraseña;
    private String fechaNacimiento;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        mEditTestNombre = (EditText) findViewById(R.id.nombre);
        mEditTestApellidos = (EditText) findViewById(R.id.apellidos);
        mEditTestEmail = (EditText) findViewById(R.id.email);
        mEditTestContraseña = (EditText) findViewById(R.id.password);
        mEditTestRepiteContraseña = (EditText) findViewById(R.id.passwordConfirmacion);
        mEditTestFechaNacimiento = (EditText) findViewById(R.id.fechaNacimiento);
        mButtonGo = (Button) findViewById(R.id.Registrarme);
        //mButtonGo.setEnabled(false);
        mEditTestFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mes, int dia) {
                        Calendar calendarResultado = Calendar.getInstance();
                        calendarResultado.set(Calendar.YEAR,year);
                        calendarResultado.set(Calendar.MONTH,mes);
                        calendarResultado.set(Calendar.DAY_OF_MONTH,dia);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = calendarResultado.getTime();
                        fechaNacimiento = simpleDateFormat.format(date);
                        mEditTestFechaNacimiento.setText(fechaNacimiento);

                    }
                },calendar.get(Calendar.YEAR)-18,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        mButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = mEditTestNombre.getText().toString();
                apellidos = mEditTestApellidos.getText().toString();
                email = mEditTestEmail.getText().toString();
                contraseña = mEditTestContraseña.getText().toString();
                repiteContraseña = mEditTestRepiteContraseña.getText().toString();
                if(!nombre.isEmpty() && !apellidos.isEmpty() && !email.isEmpty() &&
                        !contraseña.isEmpty() && !repiteContraseña.isEmpty() && !fechaNacimiento.isEmpty()){


                    if (contraseña.length() >= 6){
                        if(contraseña.equals(repiteContraseña)){
                            registroUsuario();
                        }else{
                            Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegistroActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                }




            }
        });


    }

    private void registroUsuario(){
        mAuth.createUserWithEmailAndPassword(email,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("apellidos", apellidos);
                    map.put("email", email);
                    map.put("contraseña", contraseña);
                    map.put("fecha de nacimiento", fechaNacimiento);

                    String id = mAuth.getCurrentUser().getUid();
                    databaseReference.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                startActivity(new Intent(RegistroActivity.this, MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(RegistroActivity.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Toast.makeText(RegistroActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}