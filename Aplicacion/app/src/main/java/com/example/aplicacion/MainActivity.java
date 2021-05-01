package com.example.aplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTestEmail;
    private EditText mEditTestContraseña;
    private Button mButtonlogin;
    private Button mButtonRegister;
    TextView forgotTextLink;
    //Variables
    private String email;
    private String contraseña;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTestEmail = (EditText) findViewById(R.id.email);
        mEditTestContraseña = (EditText) findViewById(R.id.password);
        mButtonlogin = (Button) findViewById(R.id.login);
        mButtonRegister = (Button) findViewById(R.id.signUpButton);
        forgotTextLink = findViewById(R.id.restableceContraseña);
        fAuth = FirebaseAuth.getInstance();

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

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("¿Quieres cambiar la contraseña ?");
                passwordResetDialog.setMessage("Ingrese su correo electrónico para recibir el enlace de restablecimiento.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Restablece la contraseña con el link que le ha " +
                                        "llegado al correo.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "\n" +
                                        "¡Error! No se pudo enviar el enlace de restablecimiento" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }

}