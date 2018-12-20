package mpet.project2018.air.mpet.fragments;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;

import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;


public class Pocetna_ulogirani extends Fragment {

    private OnFragmentInteractionListener mListener;
    public Pocetna_ulogirani() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_ulogirani, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Početna");
        }

        // Listeneri za btn
        // Button btn1= (Button) view.findViewById(R.id.frag1_btn1); btn1.setOnclickListener(...


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
        String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();
       Toast.makeText(getActivity(),"Vas id je"+idPrijavljeni, Toast.LENGTH_SHORT).show();


        Button btn1=(Button) view.findViewById(R.id.btnOdjava);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        logout();
                                    }
                                }
        );

        Button btn2=(Button) view.findViewById(R.id.btnScanUlogirani);
        btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "Skeniranje....",
                                                Toast.LENGTH_LONG).show();
                                        swapFragment2();
                                    }
                                }
        );




        //----------------------------------------------------
        //Fetching email from shared preferences
        /*SharedPreferences sp = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String id = sp.getString(Config.ID_SHARED_PREF,"");
        //Showing the current logged in email to textview
        */
               //-------------------------------------------


        //TextView textView = (TextView)view.findViewById(R.id.textView1);
        //textView.setText("Prijavljeni korisnik: " + id);


        return view;
    }


    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Sigurno se želite odjaviti?");
        alertDialogBuilder.setPositiveButton("Da",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences preferences = getActivity().getSharedPreferences
                                (Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.remove("ulogiraniKorisnikId");

                        editor.commit();


                        swapFragment();
                    }
                });

        alertDialogBuilder.setNegativeButton("Ne",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void swapFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
        ft.addToBackStack(null);

        ft.commit();
    }

    private void swapFragment2(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new SkeniranjeNFCKartice());
        ft.addToBackStack(null);

        ft.commit();
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
        // Uri -> String
        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }
}
