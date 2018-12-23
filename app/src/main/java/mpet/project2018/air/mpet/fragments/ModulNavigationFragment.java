package mpet.project2018.air.mpet.fragments;

import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mpet.project2018.air.core.ModuleImplementationMethods;
import mpet.project2018.air.manualinput.ManualInputFragment;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.CodeHelper.CodeHandlerHelper;
import mpet.project2018.air.nfc.SkeniranjeNFCFragment;

public class ModulNavigationFragment extends Fragment implements View.OnClickListener
{
    private ModulNavigationFragment.OnFragmentInteractionListener mListener;

    private List<ModuleImplementationMethods> listaModula;
    private CodeHandlerHelper codeHandlerInstance;

    public ModulNavigationFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_modul, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Skeniranje");
        }

        moduleSetup();

        moduleNavigationSetup(view);

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void moduleSetup()
    {
        codeHandlerInstance=new CodeHandlerHelper();
        listaModula=new ArrayList<ModuleImplementationMethods>();
        ModuleImplementationMethods nfcModule=new SkeniranjeNFCFragment(codeHandlerInstance, getActivity());
        listaModula.add(nfcModule);
        ModuleImplementationMethods manualModule=new ManualInputFragment(codeHandlerInstance, getActivity());
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
                    showModuleFragmnet(listaModula.get(v.getId()));
                }
            });

            insertPoint.addView(v);
        }
    }

    private void showModuleFragmnet(ModuleImplementationMethods module)
    {
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.mainFrame, module.getModuleFragment());
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PocetnaNeulogirani.OnFragmentInteractionListener) {
            mListener = (ModulNavigationFragment.OnFragmentInteractionListener) context;
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
