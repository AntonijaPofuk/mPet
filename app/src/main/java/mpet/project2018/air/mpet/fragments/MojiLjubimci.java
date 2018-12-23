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

import Retrofit.DataPost.RegistracijaMethod;
import Retrofit.RemotePost.StatusListener;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;

import static android.app.Activity.RESULT_OK;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class MojiLjubimci extends Fragment {
    private OnFragmentInteractionListener mListener;

    public MojiLjubimci() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        return view;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }

}

