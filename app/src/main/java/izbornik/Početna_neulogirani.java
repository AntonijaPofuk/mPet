package izbornik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mpet.project2018.air.mpet.R;
import prijava.LoginActivity;


public class Početna_neulogirani extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Početna_neulogirani() {}


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

        Button btn1=(Button) view.findViewById(R.id.btnPrijava);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {  //od tud je nova aktivnost
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);

                                        intent.putExtra("some", "some data"); //ako želiš, probaj izbrisati
                                        startActivity(intent);
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
