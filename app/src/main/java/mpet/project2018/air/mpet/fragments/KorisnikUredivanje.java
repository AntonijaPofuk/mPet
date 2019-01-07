package mpet.project2018.air.mpet.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
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

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.regex.Pattern;

import Retrofit.DataPost.RegistracijaMethod;
import Retrofit.Model.Korisnik;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Ljubimac_Table;
import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.database.entities.Skeniranje_Table;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;

import static mpet.project2018.air.database.entities.Korisnik_Table.korisnicko_ime;
import static mpet.project2018.air.mpet.Config.ID_SHARED_PREF;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class KorisnikUredivanje extends Fragment {
    String imeKorisnika="";
    Integer idKorisnik=0;


    private KorisnikUredivanje.OnFragmentInteractionListener mListener;


    //TODO: private UpdateKorMethod method=new UpdateKorMethod(this);

    public KorisnikUredivanje() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.update, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Korisnički podaci");
        }

        Button buttonSpremi=(Button) view.findViewById(R.id.btnUpdateSpremi);
        Button buttonOdustani=(Button) view.findViewById(R.id.btnUpdateOdustani);


        //buttonSpremi.setOnClickListener(new View.OnClickListener() {






        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
        String id=sharedPreferences.getString(Config.ID_SHARED_PREF, "");
        Toast.makeText(getActivity(),"Vas id je"+id, Toast.LENGTH_SHORT).show();





        // EditText korImeS = (EditText) view.findViewById(R.id.unosKorImeU);
        // korImeS.setText(PRIJAVLJENI_ID);


        //String  korImeU= korImeS.getText().toString();

       /* buttonSpremi.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                EditText imeEdit = (EditText)view.findViewById(R.id.unosImeU);
                                                String ime= imeEdit.getText().toString();
                                                EditText prezimeEdit = (EditText)view.findViewById(R.id.unosPrezimeU);
                                                String prezime=prezimeEdit.getText().toString();
                                                EditText korisnickoEdit = (EditText)view.findViewById(R.id.unosKorImeU);
                                                String korIme=korisnickoEdit.getText().toString();
                                                EditText adresaEdit = (EditText)view.findViewById(R.id.unosAdresaU);
                                                String adresa=adresaEdit.getText().toString();

                                                EditText mailEdit = (EditText)view.findViewById(R.id.unosMailU);
                                                String mail=mailEdit.getText().toString();
                                                EditText telefonEdit = (EditText)view.findViewById(R.id.unosTelefonU);
                                                String telefon=telefonEdit.getText().toString();
                                                if(TextUtils.isEmpty(telefon)){
                                                    telefon="DEFAULT";
                                                }
                                                EditText mobitelEdit = (EditText)view.findViewById(R.id.unosMobitelU);
                                                String mobitel=mobitelEdit.getText().toString();
                                                if(TextUtils.isEmpty(mobitel)){
                                                    mobitel="DEFAULT";
                                                }
                                                EditText lozinkaEdit = (EditText)view.findViewById(R.id.unosLozinka);
                                                String lozinka=lozinkaEdit.getText().toString();

                                                String provjera=null;

                                                if(TextUtils.isEmpty(ime)||TextUtils.isEmpty(prezime)||TextUtils.isEmpty(mail)||lozinka.length()<3||korIme.length()<3){
                                                    alertingMessage("Niste unijeli sva potrebna polja.", R.drawable.exclamation_message);
                                                }
                                                else{
                                                    if (provjeraNedozvoljeniZnakovi(ime, prezime, korIme, adresa, mobitel, telefon))
                                                        alertingMessage("Koristili ste nedozvoljene znakove!", R.drawable.exclamation_message);
                                                    else if(provjeraMail(mail)){
                                                        alertingMessage("E-mail adresa nije u propisnom obliku.", R.drawable.exclamation_message);
                                                    }
                                                    else//preimenuj methodu

                                                        method.Upload(ime, prezime, korIme, adresa, mail, mobitel, telefon, lozinka, slika);
                                                    //swapFragment();
                                                }
                                            }
                                        }
        ); */


        buttonOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });
        return view;
    }

    private void swapFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new PocetnaUlogirani());
        ft.commit();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof KorisnikUredivanje.OnFragmentInteractionListener) {
            mListener = (KorisnikUredivanje.OnFragmentInteractionListener) context;
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







