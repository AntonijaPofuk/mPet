package mpet.project2018.air.mpet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import Retrofit.DataPost.RegistracijaMethod;
import Retrofit.RemotePost.StatusListener;
import mpet.project2018.air.mpet.R;

import Retrofit.DataPost.RegistracijaMethod;
import mpet.project2018.air.mpet.R;

import static android.app.Activity.RESULT_OK;

public class Registracija extends Fragment implements StatusListener {
    private OnFragmentInteractionListener mListener;
    private static int RESULT_LOAD_IMAGE = 1;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageButton imageButton;
    private Bitmap bit=null;
    private String slika=null;

    public String status=null;
    private RegistracijaMethod method=new RegistracijaMethod(this);

    public Registracija() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.registracija, container, false);
        final View view = inflater.inflate(R.layout.registracija, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Registracija korisnika");
        }

        Button buttonSpremi=(Button) view.findViewById(R.id.btnRegistracijaSpremi);
        Button buttonOdustani=(Button) view.findViewById(R.id.btnRegistracijaOdustani);
        imageButton= (ImageButton) view.findViewById(R.id.btnChooseImage);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i=new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        buttonSpremi.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                EditText imeEdit = (EditText)view.findViewById(R.id.unosIme);
                                                String ime= imeEdit.getText().toString();
                                                EditText prezimeEdit = (EditText)view.findViewById(R.id.unosPrezime);
                                                String prezime=prezimeEdit.getText().toString();
                                                EditText korisnickoEdit = (EditText)view.findViewById(R.id.unosKorIme);
                                                String korIme=korisnickoEdit.getText().toString();
                                                EditText adresaEdit = (EditText)view.findViewById(R.id.unosAdresa);
                                                String adresa=adresaEdit.getText().toString();

                                                EditText mailEdit = (EditText)view.findViewById(R.id.unosMail);
                                                String mail=mailEdit.getText().toString();
                                                EditText telefonEdit = (EditText)view.findViewById(R.id.unosTelefon);
                                                String telefon=telefonEdit.getText().toString();
                                                if(TextUtils.isEmpty(telefon)){
                                                    telefon="DEFAULT";
                                                }
                                                EditText mobitelEdit = (EditText)view.findViewById(R.id.unosMobitel);
                                                String mobitel=mobitelEdit.getText().toString();
                                                if(TextUtils.isEmpty(mobitel)){
                                                    mobitel="DEFAULT";
                                                }
                                                EditText lozinkaEdit = (EditText)view.findViewById(R.id.unosLozinka);
                                                String lozinka=lozinkaEdit.getText().toString();

                                                String provjera=null;

                                                /**/
                                                if(bit!=null){
                                                    slika = BitmapTOString(bit);
                                                }
                                                //String slika = BitmapTOString(bit);

                                                /**/
                                                if(TextUtils.isEmpty(ime)||TextUtils.isEmpty(prezime)||TextUtils.isEmpty(mail)||lozinka.length()<3||korIme.length()<3){
                                                    Toast.makeText(getActivity(), "Provjerite polja!",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                                else{

                                                    method.Upload(ime, prezime, korIme, adresa, mail, mobitel, telefon, lozinka, slika);
                                                    swapFragment();
                                                }
                                            }
                                        }
        );

        buttonOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });

       /* Button buttonSpremi=(Button) getActivity().findViewById(R.id.btnRegistracijaSpremi);
        Button buttonOdustani=(Button) getActivity().findViewById(R.id.btnRegistracijaOdustani);

        private void Poruka(String tekst){
            Context context = getApplicationContext();
            CharSequence text = tekst;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        */

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


    @Override
    public void onStatusChanged(String s) {
        status=s;
        Activity a=getActivity();
        /*
        if(s.equals("duplikat")) {
            Toast.makeText(a, "Korisničko ime postoji",
                    Toast.LENGTH_LONG).show();
        }
        else if (s.equals("greska")) {
               swapFragment();
                Toast.makeText(a, "Ups, greška :(",
                        Toast.LENGTH_LONG).show();
            }

        else {
           swapFragment();
            Toast.makeText(a, "Registrirali ste se :)",
                    Toast.LENGTH_LONG).show();
        }
*/
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }

}

