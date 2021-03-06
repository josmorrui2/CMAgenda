package com.example.aplicacion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorariosFragmento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorariosFragmento extends Fragment {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    DatabaseReference mDatabase;
    StorageReference storageReference;
    String userId;
    TextView nombreDelPerfil;
    ImageView mImageView;
    FirebaseStorage storageRef;

    TableLayout tableLayout;
    TableRow tableRow;
    EditText txtCell;
    TextView txtCell2;

    ImageButton btnAñadir, btnBorrar, btnGuarda, btnEdita;

    private static TableRow.LayoutParams tableParams;
    //Task<Void> nomb;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

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
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        cambioNombre();
        cargarDatos();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View principal =  inflater.inflate(R.layout.fragment_horarios_fragmento, container, false);
        tableLayout = principal.findViewById(R.id.tbHorario);
        btnAñadir = principal.findViewById(R.id.btnAddRow);
        btnBorrar = principal.findViewById(R.id.btnDeleteRow);
        btnGuarda = principal.findViewById(R.id.btnGuardaHorario);
        btnEdita = principal.findViewById(R.id.btnEditaHorario);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        AddRow();
        return principal;
    }



    public void cambioNombre(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).child("nombre").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                nombreDelPerfil = getActivity().findViewById(R.id.nomPerfil);
                nombreDelPerfil.setText(String.valueOf((task.getResult().getValue())));

                mImageView = getActivity().findViewById(R.id.imagenPerfil);
                ponerFotoPerfil(mImageView);
            }

        });

    }

    public void ponerFotoPerfil(ImageView image){

        storageRef = FirebaseStorage.getInstance();
        StorageReference fotoP = storageRef.getReference().child("fotos").child(userId).child("perfil.jpg");

        fotoP.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                //image.setImageBitmap(bitmap);
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(),
                        Bitmap.createScaledBitmap(bitmap, 100, 70, false));
                drawable.setCircular(true);
                image.setImageDrawable(drawable);
            }
        });
    }

    public void AddRow() {

        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableRow = new TableRow(getActivity());
                tableRow.setLayoutParams(tableParams);

                for (int j = 0; j < 6; j++)
                {
                    txtCell = new EditText(getActivity());
                    txtCell.setGravity(Gravity.CENTER);
                    txtCell.setLayoutParams(tableParams);
                    txtCell.setBackgroundResource(R.drawable.horario_style);
                    tableRow.addView(txtCell);
                }

                tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                tableParams.weight=1;
                tableLayout.addView(tableRow);
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer ultimo = tableLayout.getChildCount();
                if(ultimo != 1){
                    for(int j=ultimo;j<=ultimo;j++){
                        View parentRow = tableLayout.getChildAt(j-1);
                        String horaH = ((TextView) ((TableRow) parentRow).getChildAt(0)).getText().toString();
                        if(!horaH.equals("")){
                            mDatabase.child("Horario").child(userId).child(horaH).removeValue();
                        }
                        tableLayout.removeViewAt(ultimo-1);
                    }
                }
            }
        });

        btnGuarda.setOnClickListener(new View.OnClickListener() {
            Map<String,Object> mapHora = new HashMap<>();

            public void onClick(View v) {
                Integer ultimo = tableLayout.getChildCount();

                for (int i = 1; i < ultimo; i++) {
                    View parentRow = tableLayout.getChildAt(i);

                    String horaVacia = ((TextView) ((TableRow) parentRow).getChildAt(0)).getText().toString();
                    if(!horaVacia.equals("")){
                        String horaH = "";
                        Map<String,Object> mapAsig = new HashMap<>();
                        for(int j=0; j<7;j++){

                            switch (j){
                                case 0:
                                    horaH = ((TextView) ((TableRow) parentRow).getChildAt(j)).getText().toString();
                                    Log.d("Hora: ", horaH);
                                    break;
                                case 1:
                                    String text1 = ((TextView) ((TableRow) parentRow).getChildAt(j)).getText().toString();
                                    Log.d("text1: ", text1);
                                    mapAsig.put("1Lunes",text1);
                                    break;
                                case 2:
                                    String text2 = ((TextView) ((TableRow) parentRow).getChildAt(j)).getText().toString();
                                    Log.d("text2: ", text2);
                                    mapAsig.put("2Martes",text2);
                                    break;
                                case 3:
                                    String text3 = ((TextView) ((TableRow) parentRow).getChildAt(j)).getText().toString();
                                    Log.d("text3: ", text3);
                                    mapAsig.put("3Miercoles",text3);
                                    break;
                                case 4:
                                    String text4 = ((TextView) ((TableRow) parentRow).getChildAt(j)).getText().toString();
                                    Log.d("text4: ", text4);
                                    mapAsig.put("4Jueves",text4);
                                    break;
                                case 5:
                                    String text5 = ((TextView) ((TableRow) parentRow).getChildAt(j)).getText().toString();
                                    Log.d("text5: ", text5);
                                    mapAsig.put("5Viernes",text5);
                                    break;
                                default:
                                    mapHora.put(horaH,mapAsig);
                                    break;

                            }

                        }
                        Log.d("mapAsig: ", mapAsig.toString());
                        Log.d("mapHora: ", mapHora.toString());
                    }

                }

                mDatabase.child("Horario").child(userId).setValue(mapHora).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Se subieron los datos correctamente", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "No se pudieron subir los datos correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mapHora = new HashMap<>();
                btnBorrar.setVisibility(View.INVISIBLE);
                btnAñadir.setVisibility(View.INVISIBLE);
                btnGuarda.setVisibility(View.INVISIBLE);
                btnEdita.setVisibility(View.VISIBLE);

                borrarTabla();
                cargarDatos();
            }
        });

        btnEdita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBorrar.setVisibility(View.VISIBLE);
                btnAñadir.setVisibility(View.VISIBLE);
                btnGuarda.setVisibility(View.VISIBLE);
                btnEdita.setVisibility(View.INVISIBLE);
                borrarTabla();
                cargarDatosEdit();
            }
        });
    }

    public void cargarDatos(){
        mDatabase.child("Horario").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                task.getResult().getChildren().forEach(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {
                        tableRow = new TableRow(getActivity());
                        tableRow.setLayoutParams(tableParams);

                        txtCell2 = new TextView(getActivity());
                        txtCell2.setGravity(Gravity.CENTER);
                        txtCell2.setText(dataSnapshot.getKey());
                        txtCell2.setLayoutParams(tableParams);
                        txtCell2.setBackgroundResource(R.drawable.horario_style);
                        tableRow.addView(txtCell2);

                        dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                            @Override
                            public void accept(DataSnapshot dataSnapshot1) {
                                txtCell2 = new TextView(getActivity());
                                txtCell2.setGravity(Gravity.CENTER);
                                txtCell2.setText(dataSnapshot1.getValue().toString());
                                txtCell2.setLayoutParams(tableParams);
                                txtCell2.setBackgroundResource(R.drawable.horario_style);
                                tableRow.addView(txtCell2);
                            }

                        });

                        tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                        tableParams.weight=1;
                        if(tableLayout != null && tableRow != null){
                            tableLayout.addView(tableRow);
                        }

                    }
                });

            }
        });

    }

    public void cargarDatosEdit(){
        mDatabase.child("Horario").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                task.getResult().getChildren().forEach(new Consumer<DataSnapshot>() {
                    @Override
                    public void accept(DataSnapshot dataSnapshot) {
                        tableRow = new TableRow(getActivity());
                        tableRow.setLayoutParams(tableParams);

                        txtCell2 = new EditText(getActivity());
                        txtCell2.setGravity(Gravity.CENTER);
                        txtCell2.setText(dataSnapshot.getKey());
                        txtCell2.setLayoutParams(tableParams);
                        txtCell2.setBackgroundResource(R.drawable.horario_style);
                        tableRow.addView(txtCell2);

                        dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                            @Override
                            public void accept(DataSnapshot dataSnapshot1) {
                                txtCell2 = new EditText(getActivity());
                                txtCell2.setGravity(Gravity.CENTER);
                                txtCell2.setText(dataSnapshot1.getValue().toString());
                                txtCell2.setLayoutParams(tableParams);
                                txtCell2.setBackgroundResource(R.drawable.horario_style);
                                tableRow.addView(txtCell2);
                            }

                        });

                        tableParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                        tableParams.weight=1;
                        if(tableLayout != null && tableRow != null){
                            tableLayout.addView(tableRow);
                        }

                    }
                });

            }
        });

    }

    public void borrarTabla() {
        Integer ultimo = tableLayout.getChildCount();
        for(int j=ultimo-1;j>=1;j--){
            tableLayout.removeViewAt(j);
        }
    }
}


//nomb = mDatabase.child("Users").child(userId).child("nombre").setValue("nombre1"); ACTUALIZAR