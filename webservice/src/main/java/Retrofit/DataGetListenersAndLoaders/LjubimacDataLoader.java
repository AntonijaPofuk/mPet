package Retrofit.DataGetListenersAndLoaders;

import java.util.ArrayList;
import java.util.List;

import Retrofit.DataGet.LjubimacData;
import Retrofit.Model.Ljubimac;

public class LjubimacDataLoader extends  DataLoader {

    private boolean petsArrived= false;
    private String code;

    public LjubimacDataLoader(String code)
    {
        this.code=code;
    }

    @Override
    public void loadData(DataLoadedListener dataLoadedListener) {
        super.loadData(dataLoadedListener);

        LjubimacData petsWS = new LjubimacData(petHandler);

        petsWS.DownloadByTag(code);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler petHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                ArrayList<Ljubimac> listaLjubimaca = (ArrayList<Ljubimac>) result;
                /*for(Store store : stores){
                    store.save();
                }*/
                petsArrived = true;
                checkDataArrival(listaLjubimaca);
            }
        }
    };


    private void checkDataArrival(ArrayList<Ljubimac> ljubimciList){
        if(petsArrived){
            mDataLoadedListener.onDataLoaded(ljubimciList);
        }
    }
}
