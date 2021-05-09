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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjustesFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjustesFragmento extends Fragment {
    FirebaseUser user;
    FirebaseAuth fAuth;
    String userId;
    ImageButton mBtnActualizarNombre;
    DatabaseReference mDatabase;
    EditText nuevoNombre;

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
//        Log.d("tag1", mBtnActualizarNombre.getText().toString());

//        mBtnActualizarNombre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map<String,Object> personaMap = new HashMap<>();
//                mDatabase.child("Users").child(userId).setValue(user);
//                Log.d("valorActualizar", mDatabase.child("Users").child(userId).setValue(user).toString());
//                startActivity(new Intent(getActivity().getPackageManager().getLaunchIntentForPackage("com.example.aplication.InicioActivity")));
//                getActivity().finish();
//
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate( R.layout.fragment_ajustes_fragmento, container, false );
        nuevoNombre = rootView.findViewById(R.id.nombre);
        mBtnActualizarNombre = rootView.findViewById(R.id.botonEditarNombre);
        mBtnActualizarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Users").child(userId).child("nombre").setValue(nuevoNombre.getText().toString());
                TextView nombreDelPerfil = (TextView) getActivity().findViewById(R.id.nomPerfil);
                nombreDelPerfil.setText(nuevoNombre.getText().toString());

            }

        });


        return rootView;
    }


}