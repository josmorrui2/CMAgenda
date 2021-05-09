package com.example.aplicacion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorariosFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorariosFragmento extends Fragment {
    private static String PARAMETRO = "";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    DatabaseReference mDatabase;
    StorageReference storageReference;
    String userId;
    String res;
    TextView nombreDelPerfil;

    //Task<Void> nomb;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HorariosFragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HorariosFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.aplicacion.HorariosFragmento newInstance(String param1, String param2) {
        com.example.aplicacion.HorariosFragmento fragment = new com.example.aplicacion.HorariosFragmento();
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
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        //res = consulta("nombre");
        DocumentReference documentReference = fStore.collection("Users").document(userId);
        //consulta("nombre");
        //String res = consulta("nombre");
        //nomb = mDatabase.child("Users").child(userId).child("nombre").setValue("pepe");
        //Log.d("valor2", frag.getText().toString());

        cambioNombre();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View principal =  inflater.inflate(R.layout.fragment_horarios_fragmento, container, false);
        return principal;
    }



    public void cambioNombre(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).child("nombre").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                nombreDelPerfil = (TextView) getActivity().findViewById(R.id.nomPerfil);
                nombreDelPerfil.setText(String.valueOf((task.getResult().getValue())));
            }

        });

    }

}


//nomb = mDatabase.child("Users").child(userId).child("nombre").setValue("nombre1"); ACTUALIZAR