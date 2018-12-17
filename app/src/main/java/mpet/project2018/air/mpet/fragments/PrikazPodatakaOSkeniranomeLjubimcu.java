package mpet.project2018.air.mpet.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.KorisnikDataLoader;
import Retrofit.DataPost.SkeniranjeMethod;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;
import Retrofit.RemotePost.SkeniranjeOnDataPostedListener;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.nfc.NFCManager;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class PrikazPodatakaOSkeniranomeLjubimcu extends Fragment implements View.OnClickListener, KorisnikDataLoadedListener, SkeniranjeOnDataPostedListener {

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

    private Ljubimac downloadedPet = null;

    private String longitude="";
    private String latitude="";

    private String kontakt="";
    private String prijavljeniKorisnik;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prikaz_podataka_skeniranog_ljubimca, container, false);

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
            popuniPetPoljaSaPodacima(downloadedPet);
            loadKorisnikData(downloadedPet.vlasnik);
        }

        SharedPreferences mSettings = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        prijavljeniKorisnik=mSettings.getString("ulogiraniKorisnikId","DEFAULT");

        String[] LOCATION_PERMS={Manifest.permission.ACCESS_FINE_LOCATION};

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(prijavljeniKorisnik!="DEFAULT") POSTdata();
            requestPermissions(LOCATION_PERMS, 1340);

        }
        else
        {
            LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
            }
            else
            {
                if(prijavljeniKorisnik!="DEFAULT") POSTdata();
            }

        }

        if(prijavljeniKorisnik=="DEFAULT")
        {
            getUserContacts();
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
    }

    private void loadKorisnikData(String userID) {
        KorisnikDataLoader userLoad = new KorisnikDataLoader(this);
        userLoad.loadDataById(userID);
    }

    private void popuniPetPoljaSaPodacima(Ljubimac downLjubimac) {


        if(emptyFieldCheck(downLjubimac.opis)) petDescp.setText(downLjubimac.opis);
        else petDescp.setText("Opis nije unesen");

        if(emptyFieldCheck(downLjubimac.ime)) petName.setText(downLjubimac.ime);
        else petName.setText("Ime nije uneseno");

        if(emptyFieldCheck(downLjubimac.vrsta)) petSpec.setText(downLjubimac.vrsta);
        else petSpec.setText("Vrsta nije unesena");

        if(emptyFieldCheck(downLjubimac.godina)) petYears.setText(downLjubimac.godina);
        else petYears.setText("Godine nisu unesene");


    }

    private void popuniUserPoljaSaPodacima(Korisnik downKorisnik)
    {
        if(emptyFieldCheck(downKorisnik.ime)) owner.setText(downKorisnik.ime+" "+downKorisnik.prezime);
        else petDescp.setText("Podaci nisu uneseni");
        if(emptyFieldCheck(downKorisnik.adresa)) ownerAdress.setText(downKorisnik.adresa);
        else ownerAdress.setText("Podaci nisu uneseni");
        if(emptyFieldCheck(downKorisnik.email)) ownerEmail.setText(downKorisnik.email);
        else ownerEmail.setText("Podaci nisu uneseni");
        if(emptyFieldCheck(downKorisnik.broj_telefona)) ownerPhone.setText(downKorisnik.broj_telefona);
        else ownerPhone.setText("Podaci nisu uneseni");
        if(emptyFieldCheck(downKorisnik.broj_mobitela)) ownerCell.setText(downKorisnik.broj_mobitela);
        else ownerCell.setText("Podaci nisu uneseni");
    }

    @Override
    public void KorisnikOnDataLoaded(List<Korisnik> listaKorisnika) {
        popuniUserPoljaSaPodacima(listaKorisnika.get(0));
    }

    private boolean emptyFieldCheck(String field)
    {
        if(field==null || field=="") return  false;
        else return  true;
    }

    private void POSTdata()
    {

        SkeniranjeMethod instancaSkeniranjaPOST=new SkeniranjeMethod(this);
        instancaSkeniranjaPOST.Upload(getDate(),getTime(),kontakt,"0",latitude,longitude,prijavljeniKorisnik,downloadedPet.kartica);

    }

    @Override
    public void onDataPosted(String idSkeniranja) {
        Toast.makeText(getActivity(), idSkeniranja, Toast.LENGTH_SHORT).show();
    }

    private String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        Date date = new Date();
        return dateFormat.format(date);

    }

    private String getTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            longitude= String.valueOf(location.getLongitude());
            latitude= String.valueOf(location.getLatitude());
            Toast.makeText(getActivity(), "Stiglo", Toast.LENGTH_SHORT).show();
            if(prijavljeniKorisnik!="DEFAULT")
            {
                POSTdata();
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

        private void getUserContacts()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Poruka vlasniku");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.neulogirani_unos_kontakta, (ViewGroup) getView(), false);

            final EditText input = (EditText) viewInflated.findViewById(R.id.input);

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

}
