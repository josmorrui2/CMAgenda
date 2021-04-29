package com.example.aplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTestEmail;
    private EditText mEditTestContraseña;
    private Button mButtonlogin;
    private Button mButtonRegister;
    //Variables
    private String email;
    private String contraseña;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTestEmail = (EditText) findViewById(R.id.email);
        mEditTestContraseña = (EditText) findViewById(R.id.password);
        mButtonlogin = (Button) findViewById(R.id.login);
        mButtonRegister = (Button) findViewById(R.id.signUpButton);

        mButtonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTestEmail.getText().toString();
                contraseña = mEditTestContraseña.getText().toString();

                if(!email.isEmpty() && !contraseña.isEmpty()){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                startActivity(new Intent(MainActivity.this, InicioActivity.class));
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
                finish();
            }
        });

    }

}