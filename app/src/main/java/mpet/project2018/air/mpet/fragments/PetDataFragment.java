package mpet.project2018.air.mpet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.KorisnikDataLoader;
import Retrofit.DataPost.SkeniranjeMethod;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;
import Retrofit.RemotePost.SkeniranjeOnDataPostedListener;

import static android.content.Context.LOCATION_SERVICE;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class PetDataFragment extends Fragment implements KorisnikDataLoadedListener, SkeniranjeOnDataPostedListener {

    //View elementi u koje se upisuju kontakt informacije i informacije o ljubimcu
    private ImageView petPic;
    private TextView petDescp;
    private TextView petName;
    private TextView owner;
    private TextView ownerAdress;
    private TextView ownerEmail;
    private TextView ownerPhone;
    private TextView ownerCell;
    private TextView petYears;
    private TextView petSpec;
    //Skenirani ljubimac
    private Ljubimac downloadedPet = null;
    //Informacije o lokaciji
    private String longitude="";
    private String latitude="";
    // Kontakt podaci upisani od strane neprijavljenog korisnika
    private String kontakt="";
    // ID prijavljenog korisnika
    private String prijavljeniKorisnik;
    // Zastavica koja onemogućuje ponovno zapisivanje zapisa o skeniranju
    private boolean alreadySentFlag=false;
    // Request kod za traženje dopuštenja korištenja lokacije
    private Integer MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    // trenutna aktivnost
    OnFragmentInteractionListener listenerActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listenerActivity.onFragmentInteraction("Skenirani ljubimac");

        View view = inflater.inflate(R.layout.prikaz_podataka_skeniranog_ljubimca, container, false);
        petPic=view.findViewById(R.id.imageView2);
        petDescp = view.findViewById(R.id.PetOpisValue);
        petName = view.findViewById(R.id.PetNameValue);
        owner = view.findViewById(R.id.KontaktVlasnikValue);
        ownerAdress = view.findViewById(R.id.KontaktAdresaValue);
        ownerEmail = view.findViewById(R.id.KontaktEmailValue);
        ownerPhone = view.findViewById(R.id.KontaktTelefonValue);
        ownerCell = view.findViewById(R.id.KontaktMobitelValue);
        petSpec=view.findViewById(R.id.PetSpInput);
        petYears=view.findViewById(R.id.PetYInput);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            downloadedPet = (Ljubimac) bundle.getSerializable("downloadPet");
            if(downloadedPet!=null) {
                popuniPetPoljaSaPodacima(downloadedPet);
                loadKorisnikData(downloadedPet.vlasnik);
                Picasso.get().load("https://airprojekt.000webhostapp.com/slike_ljubimaca/" + downloadedPet.url_slike).into(petPic);
            }
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
        prijavljeniKorisnik = sharedPreferences.getString(Config.ID_SHARED_PREF, "DEFAULT");

        locationCheck();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Provjera stanja mogućnosti dohvata lokacije uređaja
     */
    private void locationCheck()
    {
        String[] LOCATION_PERMS={Manifest.permission.ACCESS_FINE_LOCATION};
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(LOCATION_PERMS, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
                if (prijavljeniKorisnik.equals("DEFAULT")) getUserContacts();
            } else {
                if (!prijavljeniKorisnik.equals("DEFAULT")) POSTdata();
                if (prijavljeniKorisnik.equals("DEFAULT")) getUserContacts();
            }
        }
    }

    /**
     * Dohvat podataka o korisniku koji je vlasnik skeniranog ljubimca
     * @param userID ID vlasnika
     */
    private void loadKorisnikData(String userID) {
        KorisnikDataLoader userLoad = new KorisnikDataLoader(this);
        userLoad.loadDataById(userID);
    }

    /**
     * Punjenje view elemenata podacima o ljubimcu
     * @param downLjubimac skenirani ljubimac
     */
    private void popuniPetPoljaSaPodacima(Ljubimac downLjubimac) {

        if(emptyFieldCheck(downLjubimac.opis)) petDescp.setText(downLjubimac.opis);
        else petDescp.setText(mpet.project2018.air.core.R.string.noOpis);

        if(emptyFieldCheck(downLjubimac.ime)) petName.setText(downLjubimac.ime);
        else petName.setText(mpet.project2018.air.core.R.string.noIme);

        if(emptyFieldCheck(downLjubimac.vrsta)) petSpec.setText(downLjubimac.vrsta);
        else petSpec.setText(mpet.project2018.air.core.R.string.noVrsta);

        if(emptyFieldCheck(downLjubimac.godina))
            petYears.setText( String.valueOf(Calendar.getInstance().get(Calendar.YEAR)-Integer.parseInt(downLjubimac.godina)));
        else petYears.setText(mpet.project2018.air.core.R.string.noGodine);
    }

    /**
     * Popunjavanje view elemenata podacima o vlasniku
     * @param downKorisnik vlasnik ljubimca
     */
    private void popuniUserPoljaSaPodacima(Korisnik downKorisnik)
    {
        if(emptyFieldCheck(downKorisnik.ime)) owner.setText(downKorisnik.ime+" "+downKorisnik.prezime);
        else petDescp.setText(mpet.project2018.air.core.R.string.noData);
        if(emptyFieldCheck(downKorisnik.adresa)) ownerAdress.setText(downKorisnik.adresa);
        else ownerAdress.setText(mpet.project2018.air.core.R.string.noData);
        if(emptyFieldCheck(downKorisnik.email)) ownerEmail.setText(downKorisnik.email);
        else ownerEmail.setText(mpet.project2018.air.core.R.string.noData);
        if(emptyFieldCheck(downKorisnik.broj_telefona)) ownerPhone.setText(downKorisnik.broj_telefona);
        else ownerPhone.setText(mpet.project2018.air.core.R.string.noData);
        if(emptyFieldCheck(downKorisnik.broj_mobitela)) ownerCell.setText(downKorisnik.broj_mobitela);
        else ownerCell.setText(mpet.project2018.air.core.R.string.noData);
    }

    /**
     * Metoda koja se okida kada se preuzmu podaci o vlasniku ljubimca
     * @param listaKorisnika vlasnik ljubimca kao prvi elelment liste
     */
    @Override
    public void KorisnikOnDataLoaded(List<Korisnik> listaKorisnika) {
        if(listaKorisnika.size()!=0) popuniUserPoljaSaPodacima(listaKorisnika.get(0));
        else Toast.makeText(getActivity(), mpet.project2018.air.core.R.string.tryAgainOwner, Toast.LENGTH_SHORT).show();
    }

    /**
     * Metoda kojom se provjerava prazni atribut objekta
     * @param field atribut kojeg se provjerava
     * @return vraća stanje atributa, prazno, popunjeno
     */
    private boolean emptyFieldCheck(String field)
    {
        return field != null && !field.equals("");
    }

    /**
     * Metoda za POST zapisa o skeniranju
     */
    private void POSTdata()
    {
        SkeniranjeMethod instancaSkeniranjaPOST=new SkeniranjeMethod(this);
        instancaSkeniranjaPOST.Upload(getDate(),getTime(),kontakt,"0",latitude,longitude,prijavljeniKorisnik,downloadedPet.kartica);
    }

    /**
     * Metoda koja se okida nakon zapisa skeniranja u remote bazu
     * @param idSkeniranja ID zapisa skeniranja
     */
    @Override
    public void onDataPosted(String idSkeniranja) {
    }

    /**
     * Metoda za dohvat trenutnog datuma
     * @return vraća datum u definiranome formatu
     */
    private String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Metoda za dohvat vremena skeniranja
     * @return vraća trenutno vrijeme
     */
    private String getTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Dohvat lokacije
     */
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            longitude= String.valueOf(location.getLongitude());
            latitude= String.valueOf(location.getLatitude());
            if(!prijavljeniKorisnik.equals("DEFAULT") && !alreadySentFlag)
            {
                POSTdata();
                alreadySentFlag=true;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * Dohvat kontakt podataka kada korisnik nije ulogiran
     */
    private void getUserContacts()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Poruka vlasniku");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.neulogirani_unos_kontakta, (ViewGroup) getView(), false);
            final EditText input = viewInflated.findViewById(R.id.input);
            builder.setView(viewInflated);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    kontakt = input.getText().toString();
                    POSTdata();
                }
            });
            builder.setNegativeButton(R.string.odustani, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    POSTdata();
                }
            });
            builder.show();
        }

    /**
     * Okida se nakon što korisnik reagira na traženje dopuštenja korištenja lokacije uređaja
      * @param requestCode kod zahtjeva
     * @param permissions dozvole
     * @param grantResults dozvole
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                        {
                            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
                            if(prijavljeniKorisnik.equals("DEFAULT")) getUserContacts();
                        }
                        else
                            {
                            if (!prijavljeniKorisnik.equals("DEFAULT")) POSTdata();
                            else getUserContacts();
                        }
                    }
                }
                else {
                    if(!prijavljeniKorisnik.equals("DEFAULT")) POSTdata();
                    else getUserContacts();
                }
            }
        }
    }

    /**
     * Dohvat aktivnosti
     * @param context kontekst u kojem se dohvaća aktivnost
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listenerActivity = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }
}
