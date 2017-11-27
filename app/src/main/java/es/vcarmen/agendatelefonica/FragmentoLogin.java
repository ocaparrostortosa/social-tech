package es.vcarmen.agendatelefonica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by OSCAR on 18/10/2017.
 */

public class FragmentoLogin extends Fragment {

    FloatingActionButton boton;
    private Activity activity;
    private PersonaDAO personaDAO = new PersonaDAO();
    private ArrayList<Persona> listaPersonas;
    private AlertDialog.Builder dialogo;
    private View view;
    private Button botonLogin;
    private CardView cardView;
    private ProgressBar barraProgreso;

    public FragmentoLogin(){}

    public FragmentoLogin(PersonaDAO personaDAO){
        this.personaDAO = personaDAO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_login, container, false);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize(){
        TextView textoLogin = view.findViewById(R.id.textoLogin);
        Typeface tpf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Login.ttf");
        textoLogin.setTypeface(tpf);

        barraProgreso = view.findViewById(R.id.progressBar);
        cardView = view.findViewById(R.id.cardViewLogin);
        botonLogin = view.findViewById(R.id.botonLogin);
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accionBotonLogin();
            }
        });
    }

    private void accionBotonLogin(){
        barraProgreso.setVisibility(View.VISIBLE);
        cardView.setEnabled(false);

        //((ActivityPrincipal)getActivity()).reemplazarFragmentoPrincipal(new Fragmento1());
    }

}
