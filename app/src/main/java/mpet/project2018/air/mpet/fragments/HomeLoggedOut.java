package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

import mpet.project2018.air.core.InternetConnectionHandler;
import mpet.project2018.air.manualinput.ManualInputFragment;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.nfc.ScanningNFCFragment;


public class HomeLoggedOut extends Fragment {

    private OnFragmentInteractionListener mListener;

    public HomeLoggedOut() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_logged_out, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Početna");
        }
        Button btn1=view.findViewById(R.id.btnPrijava);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Login frag;
                                        frag = new Login();
                                        mListener.swapFragment(true,frag);
                                    }
                                }
        );

        Button btn2=view.findViewById(R.id.btnReg);
        btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Registration frag;
                                        frag = new Registration();
                                        mListener.swapFragment(true,frag);
                                    }
                                }
        );

        Button btn3=view.findViewById(R.id.btnScanNeUlogirani);
        btn3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(InternetConnectionHandler.isOnline(Objects.requireNonNull(getActivity()))) mListener.openModuleFragment(returnRightCodeInputMethod());

                                        else Toast.makeText(getActivity(), mpet.project2018.air.core.R.string.internetNotAvailable, Toast.LENGTH_SHORT).show();
                                    }
                                }
        );
       return view;
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
