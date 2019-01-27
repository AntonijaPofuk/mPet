package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.petsAdapter.PetModel;
import mpet.project2018.air.mpet.petsAdapter.PetsAdapter;

import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class MyPets extends Fragment {
    private OnFragmentInteractionListener mListener;
    private String ID_KORISNIKA;

    public static MyPets newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("ID_KORISNIKA", id);

        MyPets fragment = new MyPets();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            ID_KORISNIKA = bundle.getString("ID_KORISNIKA");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        readBundle(bundle);

        final View view = inflater.inflate(R.layout.moji_ljubimci, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Moji ljubimci");
        }

        Button buttonNoviLjubimac=(Button) view.findViewById(R.id.btnNoviLjubimac);
        buttonNoviLjubimac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
                String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();
                /***/
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, NewPet.newInstance(idPrijavljeni));
                ft.addToBackStack(null);

                ft.commit();
            }
        });

        RecyclerView rvLjubimci = (RecyclerView) view.findViewById(R.id.main_recycler);

        // Initialize pets
        //List<PetModel> listaLjubimaca=new List<PetModel>();
        ArrayList<PetModel> listaLjubimaca=PetModel.createPetsList(Integer.parseInt(ID_KORISNIKA));

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        PetsAdapter adapter = new PetsAdapter(listaLjubimaca,ft);
        rvLjubimci.setAdapter(adapter);
        rvLjubimci.setLayoutManager(new LinearLayoutManager(getContext()));

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

