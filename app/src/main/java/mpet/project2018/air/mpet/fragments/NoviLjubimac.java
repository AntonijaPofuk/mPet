package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Retrofit.DataPost.LjubimacPodaciMethod;
import mpet.project2018.air.mpet.R;

import static android.app.Activity.RESULT_OK;

public class NoviLjubimac extends Fragment {
    private String ID_KORISNIKA;
    private OnFragmentInteractionListener mListener;
    /*upload slike*/
    private static int RESULT_LOAD_IMAGE = 1;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageButton imageButton;
    private Bitmap bit=null;
    private String slika=null;

    //public NoviLjubimac(){};

    public static NoviLjubimac newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("ID_KORISNIKA", id);

        NoviLjubimac fragment = new NoviLjubimac();
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
        //return inflater.inflate(R.layout.novi_ljubimac, container, false);
        final View view = inflater.inflate(R.layout.novi_ljubimac, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Novi ljubimac");
        }

        Button buttonSpremi=(Button) view.findViewById(R.id.btnNoviLjubimacSpremi);
        Button buttonOdustani=(Button) view.findViewById(R.id.btnNoviLjubimacOdustani);
        imageButton= (ImageButton) view.findViewById(R.id.btnChooseImage);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        buttonSpremi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText imeEdit = (EditText)view.findViewById(R.id.txtIme);
                String ime= imeEdit.getText().toString();
                EditText godinaEdit = (EditText)view.findViewById(R.id.txtGodina);
                String godina= godinaEdit.getText().toString();
                EditText masaEdit = (EditText)view.findViewById(R.id.txtMasa);
                String masa= masaEdit.getText().toString();
                EditText vrstaEdit = (EditText)view.findViewById(R.id.txtVrsta);
                String vrsta= vrstaEdit.getText().toString();

                Spinner spolSpin = (Spinner) view.findViewById(R.id.spinnerSpol);
                String spin=(String) spolSpin.getSelectedItem();
                String spol=null;
                if(spin.equals("mužjak")){
                    spol="m";
                }
                else {
                    spol="z";
                }

                EditText opisEdit = (EditText)view.findViewById(R.id.txtOpis);
                String opis= opisEdit.getText().toString();
                String kartica="DEFAULT";
                String vlasnik="DEFAULT";
                //String vlasnik=ID_KORISNIKA;

                /**/
                if(bit!=null){
                    slika = BitmapTOString(bit);
                }

                if(TextUtils.isEmpty(ime)){
                    Toast.makeText(getActivity(), "Potrebno je upisati bar ime!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    if(TextUtils.isEmpty(opis)){
                        opis="DEFAULT";
                    }
                    if(LjubimacPodaciMethod.Upload(ime, godina, masa, vrsta, spol, opis, "/", vlasnik, kartica, slika)!="greska"){
                        swapFragment();
                        Toast.makeText(getActivity(), "Upisali ste ljubimca :)",
                                Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        buttonOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageButton.setImageBitmap(bitmap);
                bit=bitmap;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String BitmapTOString(Bitmap bitmap) {

        Bitmap bm = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        return imgString;
    }


    private void swapFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new Pocetna());
        ft.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NoviLjubimac.OnFragmentInteractionListener) {
            mListener = (NoviLjubimac.OnFragmentInteractionListener) context;
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
