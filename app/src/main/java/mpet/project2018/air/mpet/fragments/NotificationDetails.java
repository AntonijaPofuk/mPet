package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.KorisnikDataLoader;
import Retrofit.DataPost.ObavijestiMethod;
import Retrofit.Model.Korisnik;
import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Ljubimac_Table;
import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.database.entities.Skeniranje_Table;
import mpet.project2018.air.mpet.R;

public class NotificationDetails extends Fragment implements OnMapReadyCallback, KorisnikDataLoadedListener {

    private OnFragmentInteractionListener mListener;

    GoogleMap gMap;
    MapView mView;
    View mainView;

    private String idSkeniranja;

    String idKartica = "";
    Integer idKorisnik = 0;
    Date datumSkena = null;
    String vrijemeSkena = "";
    String kordX = "";
    String kordY = "";
    String imeLjubimca = "";
    String kontaktKorisnik = "";
    String brojMobitela = "";
    String brojTelefona = "";
    String email = "";
    mpet.project2018.air.database.entities.Korisnik userWhoScan=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mListener != null) {
            mListener.onFragmentInteraction("Detalji o obavijesti");
        }


        View view = inflater.inflate(R.layout.notification_details, container, false);

        mainView = view;

        idSkeniranja = getArguments().getString("idSkena");

        mView = (MapView) view.findViewById(R.id.mapView);

        mView.onCreate(savedInstanceState);

        mView.onResume();

        mView.getMapAsync(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ObavijestiMethod.Upload(idSkeniranja,"1");

        view.setBackgroundColor(Color.parseColor("#ebebe4"));

        super.onViewCreated(view, savedInstanceState);


        Skeniranje skeniranje = new SQLite().select().from(Skeniranje.class).where(Skeniranje_Table.id_skeniranja.is(Integer.parseInt(idSkeniranja))).querySingle();


        try {
            idKartica = skeniranje.getKartica().getId_kartice();
        } catch (Exception e) {

        }

        try {
            idKorisnik = skeniranje.getKorisnik().getId_korisnika();
        } catch (Exception e) {

        }
        try {
            datumSkena = skeniranje.getDatum();
        } catch (Exception e) {

        }
        try {
            vrijemeSkena = skeniranje.getVrijeme();
        } catch (Exception e) {

        }
        try {
            kordX = skeniranje.getKoordinata_x();
        } catch (Exception e) {

        }
        try {
            kordY = skeniranje.getKoordinata_y();
        } catch (Exception e) {

        }
        try {
            imeLjubimca = getImeLjubimca(skeniranje.getKartica().getId_kartice());
        } catch (Exception e) {

        }
        try {
            kontaktKorisnik = skeniranje.getKontakt();
        } catch (Exception e) {
        }

        try{
            userWhoScan=new SQLite().select().from(mpet.project2018.air.database.entities.Korisnik.class).where(Korisnik_Table.id_korisnika.is(skeniranje.getKorisnik().getId_korisnika())).querySingle();
        }
        catch (Exception e){
            //
        }

        try {
            brojTelefona = userWhoScan.getBroj_telefona();
        } catch (Exception e) {
        }

        try {
            brojMobitela = userWhoScan.getBroj_mobitela();
        } catch (Exception e) {
        }


        try {
            email = userWhoScan.getEmail();
        } catch (Exception e) {
        }

        loadData();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;

/*
            final int LOCATION_PERMISSION_REQUEST_CODE = 1252;

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            }

            gMap.setMyLocationEnabled(true);

            gMap.getUiSettings().setMyLocationButtonEnabled(true); */


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


    public String getImeLjubimca(String idKartice) {
        String ime = "";

        Ljubimac ljubimac1upit = new SQLite().select().from(Ljubimac.class).where(Ljubimac_Table.kartica_id_kartice.is(idKartice)).querySingle();

        ime = ljubimac1upit.getIme();

        return ime;
    }

    public void loadData() {
        KorisnikDataLoader korisnikDataLoader = new KorisnikDataLoader(this);
        korisnikDataLoader.loadDataById(idKorisnik.toString());//Testni ID
    }

    @Override
    public void KorisnikOnDataLoaded(List<Korisnik> listaKorisnika) {


        TextView datumVrijemeSkena = this.mainView.findViewById(R.id.txtDatumVrijemeSkena);


        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");

        try {

            datumVrijemeSkena.setText(format.format(datumSkena) + ", " + vrijemeSkena);

        } catch (Exception e) {

        }


            TextView imeLjubimca1 = this.mainView.findViewById(R.id.txtImeLjubimca);

            imeLjubimca1.setText(imeLjubimca);

        if(kordX!="" && kordY!="" && !kordX.isEmpty() && !kordY.isEmpty()) {

            try {

                LatLng latLng = new LatLng(Double.parseDouble(kordX), Double.parseDouble(kordY));

                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));

                gMap.addMarker(new MarkerOptions().title(imeLjubimca + " je skeniran/a ovdje!").position(latLng)).showInfoWindow();


            } catch (Exception e) {
                //
            }

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {

                List<Address> adresa = geocoder.getFromLocation(Double.parseDouble(kordX), Double.parseDouble(kordY), 1);

                if (!adresa.isEmpty() && adresa != null) {

                    TextView mjestoSkena = mainView.findViewById(R.id.txtMjestoSkena);

                    String locality = "";
                    String subLocality = "";
                    String country = "";

                    subLocality = adresa.get(0).getSubLocality();
                    country = adresa.get(0).getCountryName();
                    locality = adresa.get(0).getLocality();

                    if (locality != null && !locality.isEmpty()) {
                        locality = locality + ",";
                    } else {
                        locality = "";
                    }
                    if (subLocality != null && !locality.isEmpty()) {
                        subLocality = subLocality + ",";
                    } else {
                        subLocality = "";
                    }
                    if (country != null && !country.isEmpty()) {
                        //ništa
                    } else {
                        country = "Nepoznata država";
                    }

                    mjestoSkena.setText(locality + subLocality + country);

                } else {
                    TextView mjestoSkena = mainView.findViewById(R.id.txtMjestoSkena);

                    mjestoSkena.setText("Mjesto nije prepoznato");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else {
            TextView mjestoSkena = mainView.findViewById(R.id.txtMjestoSkena);

            mjestoSkena.setText("Mjesto nije prepoznato");
        }

        TextView konaktTextView = mainView.findViewById(R.id.txtKontakt);

        if((kontaktKorisnik!="" || email!="" || brojTelefona!="" || brojMobitela!="") && (!brojMobitela.isEmpty() || !brojTelefona.isEmpty() || !email.isEmpty() || !kontaktKorisnik.isEmpty())){
            String wholeContactData="";
            if(brojMobitela!="" && !brojMobitela.isEmpty()){
                wholeContactData+="Broj mobitela: "+brojMobitela+"\n" ;
            }
            if(brojTelefona!="" && !brojTelefona.isEmpty()){
                wholeContactData+="Broj telefona: "+brojTelefona+"\n";
            }
            if(email!="" && !email.isEmpty()){
                wholeContactData+="Email: "+email+"\n";
            }
            if(kontaktKorisnik!="" && !kontaktKorisnik.isEmpty()){
                wholeContactData+="Ostavljena poruka: "+kontaktKorisnik;
            }

            konaktTextView.setText(wholeContactData);

        }
        else konaktTextView.setText("Nema kontakt podataka");

        TextView korisnikTextView = mainView.findViewById(R.id.txtSkenirao);

        if(idKorisnik!=null && idKorisnik.toString()!="" && idKorisnik!=0)korisnikTextView.setText(listaKorisnika.get(0).ime + " " + listaKorisnika.get(0).prezime + " (" + listaKorisnika.get(0).korisnicko_ime + ")");
        else korisnikTextView.setText("Nepoznat korisnik");

        Skeniranje skeniranje = new Skeniranje();
        skeniranje = new SQLite().select().from(Skeniranje.class).where(Skeniranje_Table.id_skeniranja.is(Integer.parseInt(idSkeniranja))).querySingle();
        skeniranje.setProcitano("1");
        skeniranje.save();


    }


}
