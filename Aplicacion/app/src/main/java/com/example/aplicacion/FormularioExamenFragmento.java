package com.example.aplicacion;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    String userId;
    DatabaseReference mDatabase;
    Button btnCancelar, btnGuardar;
    EditText nombreTitulo;

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
                EditText txtFecha = rootView.findViewById(R.id.editTextDate);
                txtFecha.setOnClickListener(new View.OnClickListener() {
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
                                String fechaExamen = simpleDateFormat.format(date);
                                txtFecha.setText(fechaExamen);

                            }
                        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

                if (CalendarioFragmento.dia != null && CalendarioFragmento.mes != null && CalendarioFragmento.anio != null) {
                    txtFecha.setText(CalendarioFragmento.dia+"/"+CalendarioFragmento.mes+"/"+CalendarioFragmento.anio);
                }
                else if(CalendarioFragmento.fecha != null) {
                    txtFecha.setText(CalendarioFragmento.fecha);
                }

                btnCancelar = rootView.findViewById(R.id.btnCancel);
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout f1 = getActivity().findViewById(R.id.fragFormExam);
                        f1.removeAllViews();
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragFormExam, new ExamenesFragmento());
                        transaction.addToBackStack(null);
                        TextView textTitle = getActivity().findViewById(R.id.textTitle);
                        textTitle.setText("Examenes");
                        transaction.commit();
                    }
                });



            }

        });
        btnGuardar = (Button) rootView.findViewById(R.id.btnSave);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreTitulo = (EditText) getActivity().findViewById(R.id.editTextTextPersonName);
                String ntit = nombreTitulo.getText().toString();

                Spinner asignatura = (Spinner) getActivity().findViewById(R.id.spAsignatura);
                String asig = asignatura.getSelectedItem().toString();

                EditText fechaEx = (EditText) getActivity().findViewById(R.id.editTextDate);
                String fech = fechaEx.getText().toString();

                EditText horaEx = (EditText) getActivity().findViewById(R.id.editTextTime);
                String hour = horaEx.getText().toString();

                EditText descripcionEx = (EditText) getActivity().findViewById(R.id.editTextTextMultiLine);
                String descr = descripcionEx.getText().toString();

                if(!ntit.isEmpty() && !asig.isEmpty() && !fech.isEmpty() && !hour.isEmpty() && !descr.isEmpty()){
                    registroExamen(ntit,asig,fech,hour,descr);
                }

            }
        });

        return rootView;
    }

    public void registroExamen(String ntit, String asig,String fech,String hour,String descr){
        Map<String,Object> map = new HashMap<>();
        map.put("asignatura",asig);
        map.put("fecha",fech);
        map.put("hora",hour);
        map.put("descripcion",descr);

        mDatabase.child("Exams").child(userId).child(ntit).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    LinearLayout f1 = getActivity().findViewById(R.id.fragFormExam);
                    f1.removeAllViews();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragFormExam, new ExamenesFragmento());
                    transaction.addToBackStack(null);
                    TextView textTitle = getActivity().findViewById(R.id.textTitle);
                    textTitle.setText("Examenes");
                    transaction.commit();

                }else{
                    Toast.makeText(getActivity(), "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}