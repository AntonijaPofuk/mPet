package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mpet.project2018.air.core.ModuleInterface;
import mpet.project2018.air.manualinput.ManualInputFragment;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.nfc.ScanningNFCFragment;

import static android.content.Context.MODE_PRIVATE;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class CheckNFCOptions extends Fragment{
    private OnFragmentInteractionListener mListener;

    public CheckNFCOptions() {}

    public List<ModuleInterface> listaModula;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_modul, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Odabir modula");
        }

        moduleSetup();

        moduleNavigationSetup(view);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void moduleSetup()
    {

        listaModula=new ArrayList<ModuleInterface>();
        ModuleInterface nfcModule=new ScanningNFCFragment();
        listaModula.add(nfcModule);
        ModuleInterface manualModule=new ManualInputFragment();
        listaModula.add(manualModule);

    }

    private void moduleNavigationSetup(View view)
    {
        ViewGroup insertPoint = (ViewGroup) view.findViewById(R.id.modulViewGroup);

        for(int i=0;i<listaModula.size();i++)
        {

            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.navigation_modul_item,null);

            TextView textView = (TextView) v.findViewById(R.id.nazivModula);
            textView.setText(listaModula.get(i).getModuleName());

            Button openFragment=(Button) v.findViewById(R.id.otvoriModul) ;
            openFragment.setId(i);
            openFragment.setText("Pokreni");
            openFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(Config.DEFAULT_INPUT_METHOD, String.valueOf(v.getId()))
                            .apply();
                    showModuleFragmnet(listaModula.get(v.getId()));
                }
            });

            insertPoint.addView(v);
        }
    }

    private void showModuleFragmnet(ModuleInterface module)
    {
        mListener.swapFragment(true, (Fragment) module);
    }

/*
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
        TextView textView = view.findViewById(R.id.textView15);
        RadioButton radioButton = view.findViewById(R.id.radioButton);
        RadioButton radioButton2 = view.findViewById(R.id.radioButton2);
        textView.setText(R.string.odabir_unosa_koda_ogrlice);

        SharedPreferences sharedPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("MyPref", 0); //u fragmentu dodaj this.getActivity..... jer nema CONTEXA
        String defaultMethod=sharedPreferences.getString(Config.DEFAULT_INPUT_METHOD, "");
        if( defaultMethod != null && defaultMethod.equals("nfc") ) {
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
*/
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

