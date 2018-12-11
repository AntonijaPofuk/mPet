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

import mpet.project2018.air.mpet.R;

import static android.content.Context.MODE_PRIVATE;

public class Logout extends Fragment{

        private OnFragmentInteractionListener mListener;

        public Logout() {}


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.empty, container, false);

            if (mListener != null) {
                mListener.onFragmentInteraction("");
            }

            swapFragment();

            return view;
        }

    public void logOut(View view){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Login.MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        //------------------------
        editor.commit();

    }

    private void swapFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
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

            void onFragmentInteraction(String title);
        }
        private class ArticleFragment {
        }



    }




