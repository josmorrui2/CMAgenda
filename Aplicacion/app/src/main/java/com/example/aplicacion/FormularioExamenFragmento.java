package com.example.aplicacion;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormularioExamenFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormularioExamenFragmento extends Fragment {
    private Spinner mSpinner;
    private ArrayAdapter<String> mAdapter;
    private List<String> listData = new ArrayList<String>();
    String userId;
    DatabaseReference mDatabase;

    String[] arrayPaises = {"peru", "mexico", "Brasil", "venezuela"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormularioExamenFragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormularioExamenFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static FormularioExamenFragmento newInstance(String param1, String param2) {
        FormularioExamenFragmento fragment = new FormularioExamenFragmento();
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
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_formulario_examen_fragmento, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Horario").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            Set<String> Asignaturas = new ArraySet<>();
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                task.getResult().getChildren().forEach(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {

                        dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                            @Override
                            public void accept(DataSnapshot dataSnapshot1) {
                                if(!dataSnapshot1.getValue().toString().equals("")){
                                    Asignaturas.add(dataSnapshot1.getValue().toString());

                                }
                            }
                        });

                    }
                });
                mSpinner = rootView.findViewById(R.id.spAsignatura);
                List<String> listAs = new ArrayList<String>();
                listAs.addAll(Asignaturas);
                mSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,listAs));
                Log.d("Hora: ", Asignaturas.toString());
            }

        });

        return rootView;
    }
}