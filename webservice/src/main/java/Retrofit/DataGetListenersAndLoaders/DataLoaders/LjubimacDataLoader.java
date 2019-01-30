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


import Retrofit.Model.Ljubimac;
import mpet.project2018.air.database.entities.Kartica_Table;
import mpet.project2018.air.database.entities.Korisnik_Table;

/**
 * Klasa za ostvarenje komunikacije između klase koja postavlja zahtjev prema web servisu i klase koja ga zaista i izvršava
 */
public class LjubimacDataLoader  {


    protected LjubimacDataLoadedListener mLjubimacDataLoadedListener;
    private boolean petsArrived= false;
    private Integer petCount=0;
    private String idKorisnika;
    private List<Ljubimac> lista;

    /**
     * Klasa za ostvarenje komunikacije između klase koja postavlja zahtjev prema web servisu i klase koja ga zaista i izvršava
     */
    public LjubimacDataLoader(LjubimacDataLoadedListener ljubimacDataLoadedListener)
    {
        this.mLjubimacDataLoadedListener = ljubimacDataLoadedListener;
    }

    /**
     * Metoda za dohvat ljubimca prema kodu NFC taga
     * @param code kod na NFC tagu
     */
    public void loadDataByTag(String code) {

        LjubimacData petsWS = new LjubimacData(petHandler);
        petsWS.DownloadByTag(code);
    }

    /**
     * Metoda za dohvat korisnikovih ljubimaca
     * @param userId korisnik
     */
    public void loadDataByUserId(String userId) {

        idKorisnika=userId;
        LjubimacData petsWS = new LjubimacData(petHandler);
        petsWS.DownloadByUserId(userId);
    }

    /**
     * Instancirano sučelje koje služi kao listener na pridošli odgovor web servisa na prosljeđeni zahtjev
     */
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


    /**
     * Provjera pristiglosti podataka i njihovo prosljeđivanje objektu koji je postavio zahtjev
     * @param ljubimciList lista dohvaćenih ljubimaca
     */
    private void checkDataArrival(List<Ljubimac> ljubimciList){
        if(petsArrived){
            mLjubimacDataLoadedListener.LjubimacOnDataLoaded(ljubimciList);
        }
    }

    /**
     * Spremanje pristiglih podataka u lokalnu bazu podataka
     * @param listaLjubimaca pristigli podaci
     */
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
            loadBitmap("https://airprojekt.000webhostapp.com/slike_ljubimaca/"+newLjubimac.getUrl_slike(),newLjubimac);
        }
    }


    /**
     * Spremanje slike ljubimca u lokalnu bazu podataka
     * @param url url slike
     * @param ljub ljubimac čija se slika dohvaća
     */
    public void loadBitmap(String url, final mpet.project2018.air.database.entities.Ljubimac ljub) {

        Target loadtarget;
        loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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


}
