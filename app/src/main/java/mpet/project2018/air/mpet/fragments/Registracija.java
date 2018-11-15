package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Retrofit.DataPost.RegistracijaMethod;
import mpet.project2018.air.mpet.R;

public class Registracija extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.registracija, container, false);
        final View view = inflater.inflate(R.layout.registracija, container, false);
        Button buttonSpremi=(Button) getActivity().findViewById(R.id.btnRegistracijaSpremi);
        Button buttonOdustani=(Button) getActivity().findViewById(R.id.btnRegistracijaOdustani);

        buttonSpremi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText imeEdit = (EditText)view.findViewById(R.id.unosIme);
                String ime=imeEdit.getText().toString();
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
                EditText lozinkaEdit = (EditText)view.findViewById(R.id.unosLozinka);
                String lozinka=lozinkaEdit.getText().toString();
                if(TextUtils.isEmpty(ime)||TextUtils.isEmpty(prezime)||TextUtils.isEmpty(mail)||lozinka.length()<3||korIme.length()<3){
                    //Poruka("Provjerite polja!");
                }
                else{
                    RegistracijaMethod.Upload(ime, prezime, korIme, adresa, mail, mobitel, telefon, lozinka);
                    //Poruka("Registrirali ste se!");

                }

            }
        });
/*
        buttonOdustani.setOnClickListener(new View.OnClickListener() {

        });
        */
/*
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
