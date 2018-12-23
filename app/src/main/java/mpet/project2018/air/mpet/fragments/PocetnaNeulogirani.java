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
import android.widget.Toast;

import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;

import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;


public class PocetnaNeulogirani extends Fragment {

    private OnFragmentInteractionListener mListener;

    public PocetnaNeulogirani() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_ne_ulogirani, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Početna");
        }





        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
        String idNeprijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();
       Toast.makeText(getActivity(),"Vas id je"+idNeprijavljeni, Toast.LENGTH_SHORT).show();


        Button btn1=(Button) view.findViewById(R.id.btnPrijava);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //startActivity(new Intent(getContext(), LoginActivity.class));
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.mainFrame, new Prijava());
                                        ft.addToBackStack(null);

                                        ft.commit();
                                    }
                                }
        );


        Button btn2=(Button) view.findViewById(R.id.btnReg);
        btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.mainFrame, new Registracija());
                                        ft.addToBackStack(null);

                                        ft.commit();
                                    }
                                }
        );

        Button btn3=(Button) view.findViewById(R.id.btnScanNeUlogirani);
        btn3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                                        ft.replace(R.id.mainFrame, new ModulNavigationFragment());
                                        ft.addToBackStack(null);
                                        ft.commit();
                                    }
                                }
        );



        return view;
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



}
