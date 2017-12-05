package es.vcarmen.agendatelefonica;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by OSCAR on 18/10/2017.
 */

public class Fragmento2Empresas extends DialogFragment {

    private Button botonAlta;
    private Button botonBorrar;
    private EditText etNombreContacto;
    private EditText etApellidoContacto;
    private EditText etTelefonoContacto;
    private EditText etSexoContacto;
    private EditText etEmailContacto;
    private MultiAutoCompleteTextView macEstudiosContacto;
    private Spinner spProvinciaContacto;
    private SeekBar skEdadContacto;
    private TextView tvEdad;
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private ArrayList<Object> listaEmpresas;
    //Firebase
    private FirebaseAuth mAuth;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("empresaDAO", empresaDAO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            empresaDAO = (EmpresaDAO) savedInstanceState.getSerializable("empresaDAO");
        }
        accionesFirebase();
        return inflater.inflate(R.layout.layout_fragmento2, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    public Fragmento2Empresas(){}

    public Fragmento2Empresas(EmpresaDAO empresaDAO){
        this.empresaDAO = empresaDAO;
        this.listaEmpresas = empresaDAO.mostrarEmpresas();
    }

    private void initialize(){
        inicializarVariables();
        botonAlta = (Button)getView().findViewById(R.id.botonAlta);
        botonAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionBotonAlta();
                //((ActivityPrincipal)getActivity()).accionBotonAlta();
            }
        });
    }

    private void inicializarVariables(){
        etNombreContacto = getView().findViewById(R.id.nombreContacto);
        etApellidoContacto = getView().findViewById(R.id.apellidosContacto);
        etTelefonoContacto = getView().findViewById(R.id.telefonoContacto);
        etEmailContacto = getView().findViewById(R.id.emailContacto);
        macEstudiosContacto = getView().findViewById(R.id.macEstudios);
        rellenarMACTextViewEstudios();
        spProvinciaContacto = getView().findViewById(R.id.spinnerProvincia);
        rellenarSpinnerProvincias();
        skEdadContacto = getView().findViewById(R.id.seekbarEdad);
        accionSeekbarEdad();
    }

    private void accionBotonAlta(){
        String nombreContacto = etNombreContacto.getText().toString();
        String apellidoContacto = etApellidoContacto.getText().toString();
        String telefonoContacto = etTelefonoContacto.getText().toString();
        String sexoContacto = obtenerTextoBotonSeleccionado();
        String emailContacto = etEmailContacto.getText().toString();
        String estudios = obtenerInformacionMACTextViewEstudios();
        String provincia = obtenerInformacionSpinnerProvincia();
        int edad = obtenerInformacionSeekbarEdad();
        int foto = obtenerImagenContacto(sexoContacto);

        Log.v("FirebaseEmail","F2:accionBotonAlta():estadoLista:" + listaEmpresas);
        Empresa empresaAMeter = new Empresa(nombreContacto, apellidoContacto, telefonoContacto, sexoContacto, emailContacto, estudios, provincia, edad, foto);
        empresaDAO.addEmpresaFireBase(empresaAMeter, empresaDAO, (ActivityPrincipal) getActivity());
    }

    private String obtenerInformacionMACTextViewEstudios(){
        String estudios = macEstudiosContacto.getText().toString();
        return estudios.replace(",","").trim().replace(" ",",");
    }

    private String obtenerInformacionSpinnerProvincia(){
        return spProvinciaContacto.getSelectedItem().toString();
    }

    private int obtenerInformacionSeekbarEdad(){
        return skEdadContacto.getProgress();
    }

    private String obtenerTextoBotonSeleccionado(){
        RadioButton RBHombre = (RadioButton) getView().findViewById(R.id.radioButtonHombre);
        RadioButton RBMujer = (RadioButton) getView().findViewById(R.id.radioButtonMujer);
        RadioButton RBOtro = (RadioButton) getView().findViewById(R.id.radioButtonOtro);
        if( RBHombre.isChecked() ){
            return RBHombre.getText().toString();
        }
        if( RBMujer.isChecked() ) {
            return RBMujer.getText().toString();
        }
        if( RBOtro.isChecked() ) {
            return RBOtro.getText().toString();
        }
        else
            return "";
    }

    private void rellenarSpinnerProvincias(){
        String[] provincias = {"Jaén","Almería","Málaga","Sevilla","Granada","Huelva","Córdoba", "Almería"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, provincias);
        spProvinciaContacto.setAdapter(arrayAdapter);
    }

    private void rellenarMACTextViewEstudios(){
        String[] estudios = {"SMR","DAM","DAW","ASIR","Ingeniería técnica informática","Grado","Otros"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, estudios);
        macEstudiosContacto.setAdapter(arrayAdapter);
        macEstudiosContacto.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void accionSeekbarEdad(){
        tvEdad = (TextView) getView().findViewById(R.id.textViewEdad);
        skEdadContacto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvEdad.setText("Edad: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void cambiarDeFragmentoPasandoLista(EmpresaDAO empresaDAO){
        Log.v("lista::::",empresaDAO.mostrarEmpresas().toString());
        ((ActivityPrincipal)getActivity()).reemplazarFragmentoPrincipal(new Fragmento1Empresas(empresaDAO));
    }

    private int obtenerImagenContacto(String sexoContacto){
        int imagen = R.drawable.user_icon8;
        switch (sexoContacto){
            case "Hombre":
                imagen = R.drawable.user_icon6;
                break;
            case "Mujer":
                imagen = R.drawable.user_icon7;
                break;
            case "Otro":
                imagen = R.drawable.user_icon8;
                break;
            default:
                break;
        }
        return imagen;
    }

    private void accionesFirebase() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuario = mAuth.getCurrentUser();
        Log.d("FirebaseEmail", "Has entrado en Fragmento2:accionesFirebase():" + usuario.getEmail() + ":Id:" + usuario.getUid());
    }

}