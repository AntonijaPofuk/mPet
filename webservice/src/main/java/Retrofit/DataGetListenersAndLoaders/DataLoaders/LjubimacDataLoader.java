package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import Retrofit.DataGet.LjubimacData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.database.entities.Ljubimac_Table;


public class LjubimacDataLoader  {


    protected LjubimacDataLoadedListener mLjubimacDataLoadedListener;

    private boolean petsArrived= false;

    public LjubimacDataLoader(LjubimacDataLoadedListener ljubimacDataLoadedListener)
    {
        this.mLjubimacDataLoadedListener = ljubimacDataLoadedListener;
    }

    public void loadDataByTag(String code) {

        LjubimacData petsWS = new LjubimacData(petHandler);

        petsWS.DownloadByTag(code);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler petHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                List<Ljubimac> listaLjubimaca = (List<Ljubimac>) result;
                saveInLocalDatabase(listaLjubimaca);
                petsArrived = true;
                checkDataArrival(listaLjubimaca);
            }
        }
    };


    private void checkDataArrival(List<Ljubimac> ljubimciList){
        if(petsArrived){
            mLjubimacDataLoadedListener.LjubimacOnDataLoaded(ljubimciList);
        }
    }

    private void saveInLocalDatabase(List<Ljubimac> listaDohvacenihLjubimaca)
    {
        for (Ljubimac ljubimac : listaDohvacenihLjubimaca)
        {
            mpet.project2018.air.database.entities.Korisnik dohKorisnik =
                    SQLite.select().from(mpet.project2018.air.database.entities.Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt(ljubimac.korisnik))).querySingle();

            if(dohKorisnik!=null)
            {
                mpet.project2018.air.database.entities.Ljubimac newLjubimac= new mpet.project2018.air.database.entities.Ljubimac(
                        Integer.parseInt(ljubimac.id_ljubimca),ljubimac.ime,Integer.parseInt(ljubimac.godina),Long.parseLong(ljubimac.masa),ljubimac.vrsta,
                        ljubimac.spol,ljubimac.opis,ljubimac.url_slike,dohKorisnik);
            }
            else
            {
                mpet.project2018.air.database.entities.Ljubimac newLjubimac= new mpet.project2018.air.database.entities.Ljubimac(
                        Integer.parseInt(ljubimac.id_ljubimca),ljubimac.ime,Integer.parseInt(ljubimac.godina),Long.parseLong(ljubimac.masa),ljubimac.vrsta,
                        ljubimac.spol,ljubimac.opis,ljubimac.url_slike,null);
            }
        }
    }
}
