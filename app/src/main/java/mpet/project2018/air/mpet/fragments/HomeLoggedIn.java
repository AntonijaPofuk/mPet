package mpet.project2018.air.mpet.fragments;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Delete;

import java.util.Objects;

import mpet.project2018.air.core.InternetConnectionHandler;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.manualinput.ManualInputFragment;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.MainActivity;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_logged_in, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Početna");
        }
        Button btn1=view.findViewById(R.id.btnOdjava);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        logout();
                                    }
                                }
        );

        Button btn2=view.findViewById(R.id.btnScanUlogirani);

        btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       if(InternetConnectionHandler.isOnline(Objects.requireNonNull(getActivity()))) mListener.openModuleFragment(returnRightCodeInputMethod());
                                       else Toast.makeText(getActivity(), mpet.project2018.air.core.R.string.internetNotAvailable, Toast.LENGTH_SHORT).show();
                                    }
                                }
        );
            return view;
    }


    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilder.setMessage("Sigurno se želite odjaviti?");
        alertDialogBuilder.setPositiveButton("Da",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences
                                (Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.remove("ulogiraniKorisnikId");

                        editor.apply();

                        /*zamjena izbornika*/
                        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.activity_main_drawer_logged_out);
                        navigationView.getHeaderView(0);
                        navigationView.removeHeaderView(navigationView.getHeaderView(0));
                        navigationView.inflateHeaderView(R.layout.nav_header_logged_out);

                        deleteDatabase();
                        ((MainActivity)getActivity()).stopService();


                        if ( getFragmentManager() != null ) {
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        HomeLoggedOut frag;
                        frag = new HomeLoggedOut();
                        mListener.swapFragment(false,frag);
                    }
                });

        alertDialogBuilder.setNegativeButton("Ne",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteDatabase(){
        Delete.table(Korisnik.class);
        Delete.table(Skeniranje.class);
        Delete.table(Ljubimac.class);
        Delete.table(Kartica.class);
    }

    private String returnRightCodeInputMethod()
    {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        return sharedPreferences.getString(Config.DEFAULT_INPUT_METHOD, "");

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
