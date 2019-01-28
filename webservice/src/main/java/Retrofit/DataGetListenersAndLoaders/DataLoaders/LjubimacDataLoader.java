package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import Retrofit.DataGet.LjubimacData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;

import Retrofit.Model.Kartica;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.database.entities.Kartica_Table;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Ljubimac_Table;
import Retrofit.Model.Ljubimac;


public class LjubimacDataLoader  {


    protected LjubimacDataLoadedListener mLjubimacDataLoadedListener;

    private boolean petsArrived= false;

    private Integer petCount=0;

    private String idKorisnika;

    private List<Ljubimac> lista;

    public LjubimacDataLoader(LjubimacDataLoadedListener ljubimacDataLoadedListener)
    {
        this.mLjubimacDataLoadedListener = ljubimacDataLoadedListener;
    }

    public void loadDataByTag(String code) {

        LjubimacData petsWS = new LjubimacData(petHandler);

        petsWS.DownloadByTag(code);

    }

    public void loadDataByUserId(String userId) {

        idKorisnika=userId;
        LjubimacData petsWS = new LjubimacData(petHandler);

        petsWS.DownloadByUserId(userId);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler petHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, boolean prijava) {
            if(ok){
                List<Ljubimac> listaLjubimaca = (List<Ljubimac>) result;
                lista=listaLjubimaca;
                petCount=listaLjubimaca.size();
                petsArrived = true;
                if(prijava){
                    savePetInLocalDatabase(listaLjubimaca);
                }

                else checkDataArrival(listaLjubimaca);
            }
        }
    };


    private void checkDataArrival(List<Ljubimac> ljubimciList){
        if(petsArrived){
            mLjubimacDataLoadedListener.LjubimacOnDataLoaded(ljubimciList);
        }
    }

    private void savePetInLocalDatabase(List<Ljubimac> listaLjubimaca)
    {
        mpet.project2018.air.database.entities.Korisnik k=new SQLite().select().from(mpet.project2018.air.database.entities.Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt(idKorisnika))).querySingle();
        if(petCount==0) checkDataArrival(lista);
        for (Ljubimac ljubimac : listaLjubimaca)
        {

            mpet.project2018.air.database.entities.Ljubimac newLjubimac=new mpet.project2018.air.database.entities.Ljubimac(Integer.parseInt(ljubimac.id),ljubimac.ime,Integer.parseInt(ljubimac.godina),Long.parseLong(ljubimac.masa),ljubimac.vrsta,ljubimac.spol,ljubimac.opis,ljubimac.url_slike,k);
            mpet.project2018.air.database.entities.Kartica card=new mpet.project2018.air.database.entities.Kartica();
            if(ljubimac.kartica!=null){
                mpet.project2018.air.database.entities.Kartica kart=new SQLite().select().from(mpet.project2018.air.database.entities.Kartica.class).where(Kartica_Table.id_kartice.is(ljubimac.kartica)).querySingle();
                card=kart;
            }
            newLjubimac.setKartica(card);
            /*Slika*/
            loadBitmap("https://airprojekt.000webhostapp.com/slike_ljubimaca/"+newLjubimac.getUrl_slike(),newLjubimac);
            /**/
            //newLjubimac.save();
            //sprema se skupa sa slikom ispod
        }

    }

    /*********/
    //private Target loadtarget;

    public void loadBitmap(String url, final mpet.project2018.air.database.entities.Ljubimac ljub) {

        Target loadtarget;
        loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //handleLoadedBitmap(bitmap);
                ljub.setSlika(bitmap);
                ljub.save();

                petCount=petCount-1;

                if(petCount==0) checkDataArrival(lista);
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
