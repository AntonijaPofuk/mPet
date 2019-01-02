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
import Retrofit.Model.Ljubimac;


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

    public void loadDataByUserId(String userId) {

        LjubimacData petsWS = new LjubimacData(petHandler);

        petsWS.DownloadByUserId(userId);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler petHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                List<Ljubimac> listaLjubimaca = (List<Ljubimac>) result;
                //saveInLocalDatabase(listaLjubimaca);
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

}
