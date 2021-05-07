package com.example.aplicacion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.aplicacion.PerfilFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragmento extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;
    String userId;
    TextView frag;
    TextView nombre;

    TextView cambiarDato;

    private TextView mTextViewData;
    private DatabaseReference mDatabase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.aplicacion.PerfilFragmento newInstance(String param1, String param2) {
        com.example.aplicacion.PerfilFragmento fragment = new com.example.aplicacion.PerfilFragmento();
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
        nombre = (TextView) getActivity().findViewById(R.id.nombrePerfil);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        cambiarDato = getActivity().findViewById(R.id.cambiarDatos);

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        Log.d("tag", userId);
        consulta("nombre");
        mDatabase.child("Users").child(userId).child("apellidos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                frag = (TextView) getActivity().findViewById(R.id.apellidosPerfil);
                Log.d("valor1", String.valueOf(task.getResult().getValue()));
                frag.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        mDatabase.child("Users").child(userId).child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                frag = (TextView) getActivity().findViewById(R.id.emailPerfil);
                Log.d("valor1", String.valueOf(task.getResult().getValue()));
                frag.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        mDatabase.child("Users").child(userId).child("fecha de nacimiento").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                frag = (TextView) getActivity().findViewById(R.id.fechaNacimientoPerfil);
                Log.d("valor1", String.valueOf(task.getResult().getValue()));
                frag.setText(String.valueOf(task.getResult().getValue()));
            }
        });
        


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_fragmento, container, false);
    }

    public void consulta(String clave){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).child(clave).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                frag = (TextView) getActivity().findViewById(R.id.nombrePerfil);
                Log.d("valor1", String.valueOf(task.getResult().getValue()));
                frag.setText(String.valueOf(task.getResult().getValue()));

                frag = (TextView) getActivity().findViewById(R.id.emailPerfil);
                Log.d("valor1", String.valueOf(task.getResult().getValue()));
                frag.setText(String.valueOf(task.getResult().getValue()));

                frag = (TextView) getActivity().findViewById(R.id.fechaNacimientoPerfil);
                Log.d("valor1", String.valueOf(task.getResult().getValue()));
                frag.setText(String.valueOf(task.getResult().getValue()));


            }

        });


    }
}