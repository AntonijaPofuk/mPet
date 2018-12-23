package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Retrofit.DataPost.PrijavaMethod;
import Retrofit.RemotePost.onLoginValidation;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;

import static android.content.Context.MODE_PRIVATE;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;


public class Prijava extends Fragment implements onLoginValidation {

    private OnFragmentInteractionListener mListener;

    public Prijava() {}
    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    Button btnPrijavaOdustani;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_login, container, false);
        if (mListener != null) {
            mListener.onFragmentInteraction("Prijava");
        }

        edtUsername = (EditText) view.findViewById(R.id.edtUsername);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnPrijavaOdustani = (Button) view.findViewById(R.id.btnPrijavaOdustani);

        sharedPreferences = this.getActivity().getSharedPreferences("MyPref", 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
        if (sharedPreferences.getString("ulogiraniKorisnikId", "").toString().equals("ulogiraniKorisnikId")) { //getString
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new PocetnaUlogirani());
            ft.addToBackStack(null);

            ft.commit();
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                //validate form
                if (validateLogin(username, password)) {
                    //do login
                    doLogin(username, password);
                }
            }
        });

        btnPrijavaOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new PocetnaNeulogirani());
                ft.addToBackStack(null);

                ft.commit();
                }
            }
        );

        return view;
    }

    private boolean validateLogin (String username, String password){
        if (username == null || username.trim().length() == 0) {
            Toast.makeText(getActivity(), "Potrebno je unijeti korisničko ime...", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            Toast.makeText(getActivity(), "Potrebno je unijeti lozinku...", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin ( final String username, final String password){
        String username1 = edtUsername.getText().toString();
        String password1 = edtPassword.getText().toString();

        PrijavaMethod postMetodaZaPrijavu = new PrijavaMethod(this);

        String id = "";

        String response = "";

        postMetodaZaPrijavu.Upload(username1, password1);
    }

    @Override
    public void onDataLoaded (String id){


        if (Integer.parseInt(id) != 0) {


            getActivity().getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(Config.ID_SHARED_PREF,id)
                    .apply();

            Toast.makeText(getActivity(), "Vas id je"+id, Toast.LENGTH_SHORT).show();



            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new PocetnaUlogirani());
            ft.addToBackStack(null);
            ft.commit();


        } else {
            Toast.makeText(getActivity(), "Korisnicko ime ili lozinka su netocni", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }

/*  PUT: getActivity().getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(Config.ID_SHARED_PREF,id)
                    .apply();
            //-------------------------------------------
            Toast.makeText(getActivity(), "Vas id je"+id, Toast.LENGTH_SHORT).show();


         GET:SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MyPref", 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
            String id1 = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString(); //getString
            Toast.makeText(getActivity(), "Vas id je"+id1, Toast.LENGTH_SHORT).show();

   */


}
