package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.R;

import static android.content.Context.MODE_PRIVATE;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class CheckNFCOptions extends Fragment{
    private OnFragmentInteractionListener mListener;

    public CheckNFCOptions() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.options, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Opcije");
        }
        TextView textView = (TextView) view.findViewById(R.id.textView15);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.radioButton2);
        textView.setText("Odaberite željeni način unosa koda ogrlice u nastavku: ");

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MyPref", 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
        String defaultMethod=sharedPreferences.getString(Config.DEFAULT_INPUT_METHOD, "");
        if(defaultMethod.equals("nfc")) {
            radioButton.setChecked(true);
        }
            else {

            radioButton2.setChecked(true);
        }
        radioButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
                                                       .edit()
                                                       .putString(Config.DEFAULT_INPUT_METHOD, "nfc")
                                                       .apply();
                                           }
                                       }
        );
        radioButton2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
                                                        .edit()
                                                        .putString(Config.DEFAULT_INPUT_METHOD, "manual")
                                                        .apply();


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
}

