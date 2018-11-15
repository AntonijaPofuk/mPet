package mpet.project2018.air.mpet.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import mpet.project2018.air.mpet.R;

public class Pocetna extends Fragment {

    private OnFragmentInteractionListener mListener;
    public Pocetna() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_ulogirani, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivityIzbornik
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Pocetna");
        }
        // Listeneri za btn
        // Button btn1= (Button) view.findViewById(R.id.frag1_btn1); btn1.setOnclickListener(...
        Button btn1=(Button) view.findViewById(R.id.btnOdjava);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "Odjavljujete se....",
                                                Toast.LENGTH_LONG).show();
                                        swapFragment();
                                    }
                                }
        );
        return view;
    }
    private void swapFragment(){
        Registracija mDiscountListFragment = new Registracija();
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, mDiscountListFragment);
        mFragmentTransaction.commit();
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
