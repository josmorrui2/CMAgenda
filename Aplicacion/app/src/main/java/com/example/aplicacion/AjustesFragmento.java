package com.example.aplicacion;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjustesFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjustesFragmento extends Fragment {
    TextView datosDB;

    Button mBtnCargarImagen;

    ImageButton mBtnActualizarNombre;
    ImageButton mBtnGuardarNombre;
    ImageButton mBtnActualizarApellidos;
    ImageButton mBtnGuardarApellidos;
    ImageButton mBtnActualizarEmail;
    ImageButton mBtnGuardarEmail;
    ImageButton mBtnActualizarFecha;
    ImageButton mBtnGuardarFecha;
    TextView mBtnBorrarCuenta;

    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;

    FirebaseUser user;
    FirebaseAuth fAuth;
    String userId;
    DatabaseReference mDatabase;

    EditText editar;
    TextView mantener;

    private ImageView mImageView;
    private ProgressDialog mprogressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AjustesFragmento() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjustesFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static AjustesFragmento newInstance(String param1, String param2) {
        AjustesFragmento fragment = new AjustesFragmento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        cambiarDatosTextView();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate( R.layout.fragment_ajustes_fragmento, container, false );

        View layoutView = inflater.inflate(R.layout.layout_navigation_header,container,false);
        mImageView = layoutView.findViewById(R.id.imagenPerfil);
        mprogressDialog = new ProgressDialog(getActivity());
//        ACTUALIZAR NOMBRE

        mBtnActualizarNombre = rootView.findViewById(R.id.botonEditarNombre);
        mBtnActualizarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarNombre);
                mantener = rootView.findViewById(R.id.mantenerNombre);

                mantener.setVisibility(View.INVISIBLE);
                editar.setVisibility(View.VISIBLE);
                mBtnActualizarNombre.setVisibility(View.INVISIBLE);
                mBtnGuardarNombre.setVisibility(View.VISIBLE);


            }

        });
        mBtnGuardarNombre = rootView.findViewById(R.id.botonGuardarNombre);
        mBtnGuardarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarNombre);
                mantener = rootView.findViewById(R.id.mantenerNombre);

                String nuevoNombreAux = editar.getText().toString();
                mDatabase.child("Users").child(userId).child("nombre").setValue(nuevoNombreAux);
                TextView nombreDelPerfil = (TextView) getActivity().findViewById(R.id.nomPerfil);
                nombreDelPerfil.setText(nuevoNombreAux);

                mantener.setText(nuevoNombreAux);
                mantener.setVisibility(View.VISIBLE);
                editar.setVisibility(View.INVISIBLE);
                mBtnActualizarNombre.setVisibility(View.VISIBLE);
                mBtnGuardarNombre.setVisibility(View.INVISIBLE);

            }
        });

//        ACTUALIZAR APELLIDOS

        mBtnActualizarApellidos = rootView.findViewById(R.id.botonEditarApellidos);
        mBtnActualizarApellidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarApellidos);
                mantener = rootView.findViewById(R.id.mantenerApellidos);

                mantener.setVisibility(View.INVISIBLE);
                editar.setVisibility(View.VISIBLE);
                mBtnActualizarApellidos.setVisibility(View.INVISIBLE);
                mBtnGuardarApellidos.setVisibility(View.VISIBLE);


            }

        });
        mBtnGuardarApellidos = rootView.findViewById(R.id.botonGuardarApellidos);
        mBtnGuardarApellidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarApellidos);
                mantener = rootView.findViewById(R.id.mantenerApellidos);

                String nuevoNombreAux = editar.getText().toString();
                mDatabase.child("Users").child(userId).child("Apellidos").removeValue();
                mDatabase.child("Users").child(userId).child("apellidos").setValue(nuevoNombreAux);

                mantener.setText(nuevoNombreAux);
                mantener.setVisibility(View.VISIBLE);
                editar.setVisibility(View.INVISIBLE);
                mBtnActualizarApellidos.setVisibility(View.VISIBLE);
                mBtnGuardarApellidos.setVisibility(View.INVISIBLE);

            }
        });

//        ACTUALIZAR EMAIL

        EditText mEditTestFechaNacimiento = rootView.findViewById(R.id.editarFechaNacimiento);
        mEditTestFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mes, int dia) {
                        Calendar calendarResultado = Calendar.getInstance();
                        calendarResultado.set(Calendar.YEAR,year);
                        calendarResultado.set(Calendar.MONTH,mes);
                        calendarResultado.set(Calendar.DAY_OF_MONTH,dia);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date date = calendarResultado.getTime();
                        String fechaNacimiento = simpleDateFormat.format(date);
                        mEditTestFechaNacimiento.setText(fechaNacimiento);

                    }
                },calendar.get(Calendar.YEAR)-18,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        mBtnActualizarEmail = rootView.findViewById(R.id.botonEditarEmail);
        mBtnActualizarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarEmail);
                mantener = rootView.findViewById(R.id.mantenerEmail);

                mantener.setVisibility(View.INVISIBLE);
                editar.setVisibility(View.VISIBLE);
                mBtnActualizarEmail.setVisibility(View.INVISIBLE);
                mBtnGuardarEmail.setVisibility(View.VISIBLE);


            }

        });
        mBtnGuardarEmail = rootView.findViewById(R.id.botonGuardarEmail);
        mBtnGuardarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarEmail);
                mantener = rootView.findViewById(R.id.mantenerEmail);

                String nuevoNombreAux = editar.getText().toString();
                user.updateEmail(nuevoNombreAux);
                mDatabase.child("Users").child(userId).child("email").setValue(nuevoNombreAux);

                mantener.setText(nuevoNombreAux);
                mantener.setVisibility(View.VISIBLE);
                editar.setVisibility(View.INVISIBLE);
                mBtnActualizarEmail.setVisibility(View.VISIBLE);
                mBtnGuardarEmail.setVisibility(View.INVISIBLE);

            }
        });

//      ACTUALIZAR FECHA DE NACIMIENTO

        mBtnActualizarFecha = rootView.findViewById(R.id.botonEditarFecha);
        mBtnActualizarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarFechaNacimiento);
                mantener = rootView.findViewById(R.id.mantenerFechaNacimiento);

                mantener.setVisibility(View.INVISIBLE);
                editar.setVisibility(View.VISIBLE);
                mBtnActualizarFecha.setVisibility(View.INVISIBLE);
                mBtnGuardarFecha.setVisibility(View.VISIBLE);


            }

        });
        mBtnGuardarFecha = rootView.findViewById(R.id.botonGuardarFecha);
        mBtnGuardarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = rootView.findViewById(R.id.editarFechaNacimiento);
                mantener = rootView.findViewById(R.id.mantenerFechaNacimiento);

                String nuevoNombreAux = editar.getText().toString();
                mDatabase.child("Users").child(userId).child("fecha de nacimiento").setValue(nuevoNombreAux);

                mantener.setText(nuevoNombreAux);
                mantener.setVisibility(View.VISIBLE);
                editar.setVisibility(View.INVISIBLE);
                mBtnActualizarFecha.setVisibility(View.VISIBLE);
                mBtnGuardarFecha.setVisibility(View.INVISIBLE);

            }
        });

        mBtnBorrarCuenta = rootView.findViewById(R.id.borrarPerfil);
        mBtnBorrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder advertencia = new AlertDialog.Builder(v.getContext());
                datosDB = (TextView) getActivity().findViewById(R.id.mantenerNombre);
                advertencia.setTitle(datosDB.getText().toString());
                advertencia.setMessage("¿Seguro que quiere borrar el perfil?");
                advertencia.setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child("Users").child(userId).removeValue();
                        user.delete();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                });

                advertencia.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                advertencia.create().show();



            }
        });

        mBtnCargarImagen = rootView.findViewById(R.id.cargaImagen);
        mBtnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });


        return rootView;
    }

    public void cambiarDatosTextView(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        List<String> lista = new ArrayList<>();
        lista.add("nombre");lista.add("apellidos");lista.add("email");lista.add("fecha de nacimiento");

        for (int i=0;i<lista.size();i++){
            mDatabase.child("Users").child(userId).child(lista.get(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    switch (task.getResult().getKey()){
                        case "nombre":
                            datosDB = (TextView) getActivity().findViewById(R.id.mantenerNombre);
                            datosDB.setText(String.valueOf((task.getResult().getValue())));
                            break;
                        case "apellidos":
                            datosDB = (TextView) getActivity().findViewById(R.id.mantenerApellidos);
                            datosDB.setText(String.valueOf((task.getResult().getValue())));
                            break;
                        case "email":
                            datosDB = (TextView) getActivity().findViewById(R.id.mantenerEmail);
                            datosDB.setText(String.valueOf((task.getResult().getValue())));
                            break;
                        case "fecha de nacimiento":
                            datosDB = (TextView) getActivity().findViewById(R.id.mantenerFechaNacimiento);
                            datosDB.setText(String.valueOf((task.getResult().getValue())));
                            break;
                        default:
                            break;
                    }


                }

            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK){

            mprogressDialog.setTitle("Subiendo...");
            mprogressDialog.setMessage("Subiendo foto a Firebase");
            mprogressDialog.setCancelable(false);
            mprogressDialog.show();

            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("fotos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mprogressDialog.dismiss();
                    String descargarFoto = filePath.getDownloadUrl().toString();
                    Glide.with(getContext()).load(descargarFoto).fitCenter().centerCrop().into(mImageView);

                    Toast.makeText(getActivity(),"Se subió exitosamente la foto.",Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}