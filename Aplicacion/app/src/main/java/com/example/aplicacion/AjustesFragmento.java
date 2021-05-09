package com.example.aplicacion;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjustesFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjustesFragmento extends Fragment {
    TextView datosDB;

    ImageButton mBtnActualizarNombre;
    ImageButton mBtnGuardarNombre;
    ImageButton mBtnActualizarApellidos;
    ImageButton mBtnGuardarApellidos;
    ImageButton mBtnActualizarEmail;
    ImageButton mBtnGuardarEmail;
    ImageButton mBtnActualizarFecha;
    ImageButton mBtnGuardarFecha;


    FirebaseUser user;
    FirebaseAuth fAuth;
    String userId;
    DatabaseReference mDatabase;

    EditText editar;
    TextView mantener;

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


        mDatabase = FirebaseDatabase.getInstance().getReference();
        cambiarDatosTextView();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate( R.layout.fragment_ajustes_fragmento, container, false );

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


}