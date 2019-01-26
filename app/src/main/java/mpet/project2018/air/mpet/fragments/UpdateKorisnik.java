package mpet.project2018.air.mpet.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import Retrofit.DataPost.RegistracijaMethod;
import Retrofit.RemotePost.StatusListener;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;

import static android.app.Activity.RESULT_OK;

public class UpdateKorisnik extends Fragment implements StatusListener {
    private OnFragmentInteractionListener mListener;
    private String ID_KORISNIKA;

    /*upload slike*/
    private static int RESULT_LOAD_IMAGE = 1;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageButton imageButton;
    private Bitmap bit=null;
    private String slika=null;

    public String status=null;
    private RegistracijaMethod method=new RegistracijaMethod(this);

    private String globalIme;
    private String globalPrezime;
    private String globalKorIme;
    private String globalAdresa;
    private String globalMail;
    private String globalMobitel;
    private String globalTelefon;

    private Target loadtarget;

    private Korisnik uredivaniKorisnik;

    public static UpdateKorisnik newInstance(String idKor) {
        Bundle bundle = new Bundle();
        bundle.putString("ID_KORISNIKA", idKor);

        UpdateKorisnik fragment = new UpdateKorisnik();
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

        final View view = inflater.inflate(R.layout.update_user, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Podaci korisnika");
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
                                                EditText mobitelEdit = (EditText)view.findViewById(R.id.unosMobitel);
                                                String mobitel=mobitelEdit.getText().toString();

                                                String provjera=null;

                                                /**/
                                                if(bit!=null){
                                                    slika = BitmapTOString(bit);
                                                }

                                                /**/
                                                if(TextUtils.isEmpty(ime)||TextUtils.isEmpty(prezime)||TextUtils.isEmpty(mail)||korIme.length()<3){
                                                    alertingMessage("Niste unijeli sva potrebna polja.", R.drawable.exclamation_message);
                                                }
                                                else{
                                                    if (provjeraNedozvoljeniZnakovi(ime, prezime, korIme, adresa, mobitel, telefon))
                                                        alertingMessage("Koristili ste nedozvoljene znakove!", R.drawable.exclamation_message);
                                                    else if(provjeraMail(mail)){
                                                        alertingMessage("E-mail adresa nije u propisnom obliku.", R.drawable.exclamation_message);
                                                    }
                                                    else {
                                                        globalIme=ime;
                                                        globalPrezime=prezime;
                                                        globalAdresa=adresa;
                                                        globalMail=mail;
                                                        globalKorIme=korIme;
                                                        globalTelefon=telefon;
                                                        globalMobitel=mobitel;
                                                        method.Update(ID_KORISNIKA,ime,prezime,korIme,adresa,mail,mobitel,telefon,slika);
                                                    }
                                                }
                                            }
                                        }
        );

        buttonOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeLoggedIn frag;
                frag = new HomeLoggedIn();
                swapFragment(false,(HomeLoggedIn) frag);
                ;
            }
        });

        /*popunjavanje početnih podataka*/
        uredivaniKorisnik=new SQLite().select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt(ID_KORISNIKA))).querySingle();
        EditText imeEdit = (EditText)view.findViewById(R.id.unosIme);
        EditText prezimeEdit = (EditText)view.findViewById(R.id.unosPrezime);
        EditText adresaEdit = (EditText)view.findViewById(R.id.unosAdresa);
        EditText mailEdit = (EditText)view.findViewById(R.id.unosMail);
        EditText telefonEdit = (EditText)view.findViewById(R.id.unosTelefon);
        EditText mobitelEdit = (EditText)view.findViewById(R.id.unosMobitel);
        EditText korImeEdit = (EditText)view.findViewById(R.id.unosKorIme);

        imeEdit.setText(uredivaniKorisnik.getIme());
        prezimeEdit.setText(uredivaniKorisnik.getPrezime());
        adresaEdit.setText(uredivaniKorisnik.getAdresa());
        mailEdit.setText(uredivaniKorisnik.getEmail());
        telefonEdit.setText(uredivaniKorisnik.getBroj_telefona());
        mobitelEdit.setText(uredivaniKorisnik.getBroj_mobitela());
        korImeEdit.setText(uredivaniKorisnik.getKorisnicko_ime());

        if(!uredivaniKorisnik.getUrl_profilna().equals("default_profil.png")){
            imageButton.setImageBitmap(uredivaniKorisnik.getSlika());
            bit=uredivaniKorisnik.getSlika();
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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


    private void swapFragment(boolean addToBackstack, Fragment fragToShow){
        if(getActivity() == null)
            return;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, (Fragment) fragToShow);
        if(addToBackstack)
            ft.addToBackStack(null);
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

    private boolean provjeraNedozvoljeniZnakovi(String ime,String prezime,String korIme,String adresa,String mobitel,String telefon){
        String pattern = "[\\'|\\!|\\?|\\#|\\*|\\$|\\%|\\&|\\/]";
        Pattern p = Pattern.compile(pattern);
        boolean found=false;
        if(p.matcher(ime).find())
            return true;
        if(p.matcher(prezime).find())
            return true;
        if(p.matcher(korIme).find())
            return true;
        if(p.matcher(ime).find())
            return true;
        if(p.matcher(adresa).find())
            return true;
        if(p.matcher(mobitel).find())
            return true;
        if(p.matcher(telefon).find())
            return true;
        return found;
    }

    private boolean provjeraMail(String mail){
        String pattern = "^[A-z0-9][A-z0-9]*\\.?[A-z0-9]*@[A-z0-9]+\\.[A-z0-9]{2,}$";
        Pattern p = Pattern.compile(pattern);
        if(p.matcher(mail).matches())
            return false;
        else return true;
    }

    private void alertingMessage(String message, int imageIcon)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Upozorenje")
                .setMessage(message)
                .setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(imageIcon)
                .show();
    }

    @Override
    public void onStatusChanged(String s) {
        status=s;
        Activity a=getActivity();

        if(s.equals("duplikat")) {
            alertingMessage("Korisničko ime već postoji!", R.drawable.exclamation_message);
        }
        else if (s.equals("greska")) {
            alertingMessage("Ups, greška...", R.drawable.fail_message);
        }
        /*promjena podataka*/
        else if(!s.equals("uspjesno")&&!s.equals("duplikat")){
            Toast.makeText(getActivity(), "Ažurirali ste podatke :)",
                    Toast.LENGTH_LONG).show();
            uredivaniKorisnik.setKorisnicko_ime(globalKorIme);
            uredivaniKorisnik.setIme(globalIme);
            uredivaniKorisnik.setPrezime(globalPrezime);
            uredivaniKorisnik.setAdresa(globalAdresa);
            uredivaniKorisnik.setEmail(globalMail);
            uredivaniKorisnik.setBroj_telefona(globalTelefon);
            uredivaniKorisnik.setBroj_mobitela(globalMobitel);
            if(bit!=null){
                uredivaniKorisnik.setSlika(bit);
            }
            uredivaniKorisnik.update();
            ((MainActivity)getActivity()).changeHeaderData();
            /**/
            HomeLoggedIn frag;
            frag = new HomeLoggedIn();
            swapFragment(false,(HomeLoggedIn) frag);
        }

    }


}

