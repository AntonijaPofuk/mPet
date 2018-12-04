package Retrofit.DataGetListenersAndLoaders;

import java.util.ArrayList;
import java.util.List;

import Retrofit.DataGet.LjubimacData;
import Retrofit.Model.Ljubimac;

public class LjubimacDataLoader  {


    protected DataLoadedListener mDataLoadedListener;

    private boolean petsArrived= false;

    public LjubimacDataLoader(DataLoadedListener dataLoadedListener)
    {
        this.mDataLoadedListener=dataLoadedListener;
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
                /*for(Store store : stores){
                    store.save();
                }*/
                petsArrived = true;
                checkDataArrival(listaLjubimaca);
            }
        }
    };


    private void checkDataArrival(List<Ljubimac> ljubimciList){
        if(petsArrived){
            mDataLoadedListener.onDataLoaded(ljubimciList);
        }
    }
}
