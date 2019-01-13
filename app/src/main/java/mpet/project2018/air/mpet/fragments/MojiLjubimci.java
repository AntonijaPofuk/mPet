package mpet.project2018.air.mpet.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Retrofit.DataPost.RegistracijaMethod;
import Retrofit.RemotePost.StatusListener;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.petsAdapter.PetModel;
import mpet.project2018.air.mpet.petsAdapter.PetsAdapter;

import static android.app.Activity.RESULT_OK;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class MojiLjubimci extends Fragment {
    private OnFragmentInteractionListener mListener;
    private String ID_KORISNIKA;

    //public MojiLjubimci() {}

    public static MojiLjubimci newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("ID_KORISNIKA", id);

        MojiLjubimci fragment = new MojiLjubimci();
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

        //return inflater.inflate(R.layout.registracija, container, false);
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
                ft.replace(R.id.mainFrame, NoviLjubimac.newInstance(idPrijavljeni));
                ft.addToBackStack(null);

                ft.commit();
            }
        });

        RecyclerView rvLjubimci = (RecyclerView) view.findViewById(R.id.main_recycler);

        // Initialize pets
        //List<PetModel> listaLjubimaca=new List<PetModel>();
        ArrayList<PetModel> listaLjubimaca=PetModel.createPetsList(Integer.parseInt(ID_KORISNIKA));

        PetsAdapter adapter = new PetsAdapter(listaLjubimaca);
        rvLjubimci.setAdapter(adapter);
        rvLjubimci.setLayoutManager(new LinearLayoutManager(getContext()));

/*
        String tekst="";
        if(listaLjubimaca.isEmpty()){
            tekst="prazno";
        }
        else {
            tekst="puno";
        }
        tekst=listaLjubimaca.get(1).getName();
        Toast.makeText(getActivity(),tekst, Toast.LENGTH_SHORT).show();
*/
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NoviLjubimac.OnFragmentInteractionListener) {
            mListener = (MojiLjubimci.OnFragmentInteractionListener) context;
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

