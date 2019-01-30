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
import android.widget.ProgressBar;
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

    private String scanID;

    String petCardID = "";
    Integer userID = 0;
    Date scanDate = null;
    String scanTime = "";
    String coordX = "";
    String coordY = "";
    String petName = "";
    String userContact = "";
    String userMobilePhoneNumber = "";
    String userPhoneNumber = "";
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

        scanID = getArguments().getString("idSkena");

        mView = (MapView) view.findViewById(R.id.mapView);

        mView.onCreate(savedInstanceState);

        mView.onResume();

        mView.getMapAsync(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ObavijestiMethod.Upload(scanID,"1");

        view.setBackgroundColor(Color.parseColor("#ebebe4"));

        super.onViewCreated(view, savedInstanceState);


        Skeniranje localScanList = new SQLite().select().from(Skeniranje.class).where(Skeniranje_Table.id_skeniranja.is(Integer.parseInt(scanID))).querySingle();


        try {
            petCardID = localScanList.getKartica().getId_kartice();
        } catch (Exception e) {

        }

        try {
            userID = localScanList.getKorisnik().getId_korisnika();
        } catch (Exception e) {

        }
        try {
            scanDate = localScanList.getDatum();
        } catch (Exception e) {

        }
        try {
            scanTime = localScanList.getVrijeme();
        } catch (Exception e) {

        }
        try {
            coordX = localScanList.getKoordinata_x();
        } catch (Exception e) {

        }
        try {
            coordY = localScanList.getKoordinata_y();
        } catch (Exception e) {

        }
        try {
            petName = getImeLjubimca(localScanList.getKartica().getId_kartice());
        } catch (Exception e) {

        }
        try {
            userContact = localScanList.getKontakt();
        } catch (Exception e) {
        }

        try{
            userWhoScan=new SQLite().select().from(mpet.project2018.air.database.entities.Korisnik.class).where(Korisnik_Table.id_korisnika.is(localScanList.getKorisnik().getId_korisnika())).querySingle();
        }
        catch (Exception e){
            //
        }

        try {
            userPhoneNumber = userWhoScan.getBroj_telefona();
        } catch (Exception e) {
        }

        try {
            userMobilePhoneNumber = userWhoScan.getBroj_mobitela();
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


    public String getImeLjubimca(String petCardID) {
        String petName1 = "";

        Ljubimac ljubimac1upit = new SQLite().select().from(Ljubimac.class).where(Ljubimac_Table.kartica_id_kartice.is(petCardID)).querySingle();

        petName1 = ljubimac1upit.getIme();

        return petName1;
    }

    public void loadData() {
        KorisnikDataLoader userDataLoader = new KorisnikDataLoader(this);
        userDataLoader.loadDataById(userID.toString());//Testni ID
    }

    @Override
    public void KorisnikOnDataLoaded(List<Korisnik> listaKorisnika) {


        ProgressBar progressBar=this.mainView.findViewById(R.id.progressBarNotificationDetail);

        progressBar.setVisibility(View.GONE);

        TextView scanDateTimeTextView = this.mainView.findViewById(R.id.txtScanDateTime);


        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");

        try {

            scanDateTimeTextView.setText(format.format(scanDate) + ", " + scanTime);

        } catch (Exception e) {

        }


            TextView petNameTextView = this.mainView.findViewById(R.id.txtPetName);

            petNameTextView.setText(petName);

        if(coordX !="" && coordY !="" && !coordX.isEmpty() && !coordY.isEmpty()) {

            try {

                LatLng latLng = new LatLng(Double.parseDouble(coordX), Double.parseDouble(coordY));

                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));

                gMap.addMarker(new MarkerOptions().title(petName + " je skeniran/a ovdje!").position(latLng)).showInfoWindow();


            } catch (Exception e) {
                //
            }

            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            try {

                List<Address> userAdress = geocoder.getFromLocation(Double.parseDouble(coordX), Double.parseDouble(coordY), 1);

                if (!userAdress.isEmpty() && userAdress != null) {

                    TextView scanPlaceTextView = mainView.findViewById(R.id.txtScanPlace);

                    String locality = "";
                    String subLocality = "";
                    String country = "";

                    subLocality = userAdress.get(0).getSubLocality();
                    country = userAdress.get(0).getCountryName();
                    locality = userAdress.get(0).getLocality();

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

                    scanPlaceTextView.setText(locality + subLocality + country);

                } else {
                    TextView scanPlaceTextView = mainView.findViewById(R.id.txtScanPlace);

                    scanPlaceTextView.setText("Mjesto nije prepoznato");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else {
            TextView scanPlaceTextView = mainView.findViewById(R.id.txtScanPlace);

            scanPlaceTextView.setText("Mjesto nije prepoznato");
        }

        TextView userContactTextView = mainView.findViewById(R.id.txtUserContact);

        if((userContact !="" || email!="" || userPhoneNumber !="" || userMobilePhoneNumber !="") && (!userMobilePhoneNumber.isEmpty() || !userPhoneNumber.isEmpty() || !email.isEmpty() || !userContact.isEmpty())){
            String wholeContactData="";
            if(userMobilePhoneNumber !="" && !userMobilePhoneNumber.isEmpty()){
                wholeContactData+="Broj mobitela: "+ userMobilePhoneNumber +"\n" ;
            }
            if(userPhoneNumber !="" && !userPhoneNumber.isEmpty()){
                wholeContactData+="Broj telefona: "+ userPhoneNumber +"\n";
            }
            if(email!="" && !email.isEmpty()){
                wholeContactData+="Email: "+email+"\n";
            }
            if(userContact !="" && !userContact.isEmpty()){
                wholeContactData+="Ostavljena poruka: "+ userContact;
            }

            userContactTextView.setText(wholeContactData);

        }
        else userContactTextView.setText("Nema kontakt podataka");

        TextView userTextView = mainView.findViewById(R.id.txtUserWhoScan);

        if(userID !=null && userID.toString()!="" && userID !=0)userTextView.setText(listaKorisnika.get(0).ime + " " + listaKorisnika.get(0).prezime + " (" + listaKorisnika.get(0).korisnicko_ime + ")");
        else userTextView.setText("Nepoznat korisnik");

        Skeniranje localScanList = new Skeniranje();
        localScanList = new SQLite().select().from(Skeniranje.class).where(Skeniranje_Table.id_skeniranja.is(Integer.parseInt(scanID))).querySingle();
        localScanList.setProcitano("1");
        localScanList.save();


    }


}
