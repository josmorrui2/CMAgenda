package com.example.aplicacion;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.aplicacion.ExamenesFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamenesFragmento extends Fragment {

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> listData = new ArrayList<String>();

    FirebaseAuth fAuth;
    String userId;
    TextView nombreDelPerfil;
    DatabaseReference mDatabase;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExamenesFragmento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExamenesFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.aplicacion.ExamenesFragmento newInstance(String param1, String param2) {
        com.example.aplicacion.ExamenesFragmento fragment = new com.example.aplicacion.ExamenesFragmento();
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
        userId = fAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_examenes_fragmento, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mListView = rootView.findViewById(R.id.lista);
        FloatingActionButton agregar = rootView.findViewById(R.id.btnAnadirExamen);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout f1 = (LinearLayout) getActivity().findViewById(R.id.fragPerExam);
                f1.removeAllViews();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.fragPerExam, new FormularioExamenFragmento());
                transaction.addToBackStack(null);
                TextView textTitle = getActivity().findViewById(R.id.textTitle);
                textTitle.setText("Examenes");
                transaction.commit();

            }
        });
        List<String> listaaux = new ArrayList<>();
        listaaux.add("fecha");listaaux.add("hora");
        mDatabase.child("examenes").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                List<String> listaMat = new ArrayList<>();
                task.getResult().getChildren().forEach(new Consumer<DataSnapshot>() {

                    public void accept(DataSnapshot dataSnapshot) {
                        listaMat.add(dataSnapshot.getKey());

                    }
                });
                funcion1(listaMat,listaaux);

            }
        });

        return rootView;
    }

    public void funcion1(List<String> listaMat, List<String> listaaux){

        for(int j=0;j<listaMat.size();j++) {
            final String[] exa = {"Examen de " + listaMat.get(j) + " el día "};
            for (int i = 0; i < listaaux.size(); i++) {
                int finalI = i;

                mDatabase.child("examenes").child(userId).child(listaMat.get(j)).child(listaaux.get(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().getValue() != null){

                            exa[0] += task.getResult().getValue().toString();
                            if (finalI == 0) {
                                exa[0] += " a las ";
                            } else {
                                listData.add(exa[0]);
                                mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, listData);
                                mListView.setAdapter(mAdapter);
                            }
                            Log.d("RESULTADO: ", task.getResult().getValue().toString());

                        }

                    }

                });
            }
        }

    }

}