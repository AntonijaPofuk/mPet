package mpet.project2018.air.mpet.fragments;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Delete;

import mpet.project2018.air.core.InternetConnectionHandler;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.manualinput.ManualInputFragment;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.nfc.ScanningNFCFragment;

public class HomeLoggedIn extends Fragment {

    private OnFragmentInteractionListener mListener;
    public HomeLoggedIn() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_logged_in, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Početna");
        }
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
                                       if(InternetConnectionHandler.isOnline(getActivity())) swapFragment2();
                                       else Toast.makeText(getActivity(), mpet.project2018.air.core.R.string.internetNotAvailable, Toast.LENGTH_SHORT).show();
                                    }
                                }
        );
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

                        /*zamjena izbornika*/
                        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.activity_main_drawer_logged_out);
                        navigationView.getHeaderView(0);
                        navigationView.removeHeaderView(navigationView.getHeaderView(0));
                        navigationView.inflateHeaderView(R.layout.nav_header_logged_out);

                        deleteDatabase();

                        HomeLoggedOut frag;
                        frag = new HomeLoggedOut();
                        mListener.swapFragment(false,(HomeLoggedOut) frag);
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

    private void deleteDatabase(){
        Delete.table(Korisnik.class);
        Delete.table(Skeniranje.class);
        Delete.table(Ljubimac.class);
        Delete.table(Kartica.class);
    }



    //TODO: izbrisati swap returnRightCodeInputMethod
    private void swapFragment2(){

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, returnRightCodeInputMethod());
        ft.addToBackStack(null);
        ft.commit();

    }

    private Fragment returnRightCodeInputMethod()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        String defaultMethod=sharedPreferences.getString(Config.DEFAULT_INPUT_METHOD, "");
        if(defaultMethod.equals("nfc"))
        {
            return new ScanningNFCFragment();
        }

        else return new ManualInputFragment();
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


  }
