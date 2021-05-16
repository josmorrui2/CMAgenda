package com.example.aplicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    DatabaseReference mDatabase;

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

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users").child(fAuth.getUid()).child("contraseña").setValue(contraseña);



                                Bundle extras = new Bundle();
                                extras.putString("EMAIL",email);
                                extras.putString("NOMBRE", "nombre");
                                Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                                intent.putExtras(extras);

                                //startActivity(new Intent(MainActivity.this, InicioActivity.class));
                                startActivity(intent);
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

        mEditTestEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().equals("") && !mEditTestContraseña.getText().toString().equals("")) {
                    mButtonlogin.setEnabled(true);
                } else {
                    mButtonlogin.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") && !mEditTestContraseña.getText().toString().equals("")) {
                    mButtonlogin.setEnabled(true);
                } else {
                    mButtonlogin.setEnabled(false);
                }
            }
        });

        mEditTestContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().equals("") && !mEditTestEmail.getText().toString().equals("")) {
                    mButtonlogin.setEnabled(true);
                } else {
                    mButtonlogin.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") && !mEditTestEmail.getText().toString().equals("")) {
                    mButtonlogin.setEnabled(true);
                } else {
                    mButtonlogin.setEnabled(false);
                }
            }
        });
    }

}