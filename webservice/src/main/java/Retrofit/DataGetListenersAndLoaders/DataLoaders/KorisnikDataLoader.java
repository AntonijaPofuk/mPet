package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Korisnik;
import mpet.project2018.air.database.entities.Korisnik_Table;

public class KorisnikDataLoader {

    protected KorisnikDataLoadedListener mKorisnikDataLoadedListener;

    private boolean usersArrived= false;

    public KorisnikDataLoader(KorisnikDataLoadedListener korisnikDataLoadedListener)
    {
        this.mKorisnikDataLoadedListener = korisnikDataLoadedListener;
    }

    public void loadDataById(String userId) {

        KorisnikData userWS = new KorisnikData(userHandler);

        userWS.DownloadByUserId(userId);

    }

    public void loadUsersByUserId(String userId) {

        KorisnikData userWS = new KorisnikData(userHandler);

        userWS.DownloadUsersByUserId(userId);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler userHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                List<Korisnik> listaKorisnika = (List<Korisnik>) result;
                //saveUserInLocalDatabase(listaKorisnika);
                usersArrived = true;
                checkDataArrival(listaKorisnika);
            }
        }
    };


    private void checkDataArrival(List<Korisnik> korisnikList){
        if(usersArrived){
            mKorisnikDataLoadedListener.KorisnikOnDataLoaded(korisnikList);
        }
    }

    private void saveUserInLocalDatabase(List<Korisnik> listaKorisnika)
    {
        for (Korisnik korisnik : listaKorisnika)
        {
            mpet.project2018.air.database.entities.Korisnik newKorisnik=new mpet.project2018.air.database.entities.Korisnik(Integer.parseInt(korisnik.id),
                    korisnik.ime,korisnik.prezime,korisnik.korisnicko_ime,null,korisnik.email,korisnik.adresa,korisnik.broj_mobitela,
                    korisnik.broj_telefona,korisnik.url_profilna);
            /*Slika*/
            loadBitmap("https://airprojekt.000webhostapp.com/slike_profila/"+newKorisnik.getUrl_profilna(),newKorisnik);
            /**/
            //newKorisnik.save();
            //sprema se skupa sa slikom ispod
        }
    }

    /*********/
    private Target loadtarget;

    public void loadBitmap(String url, final mpet.project2018.air.database.entities.Korisnik kor) {

        loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //handleLoadedBitmap(bitmap);
                kor.setSlika(bitmap);
                kor.save();
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };

        Picasso.get().load(url).into(loadtarget);
    }

    public void handleLoadedBitmap(Bitmap b) {

    }
    /*********/

}
