package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import java.util.List;

import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGet.SkeniranjeData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.SkeniranjeDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Skeniranje;

public class SkeniranjeDataLoader {

    protected SkeniranjeDataLoadedListener mSkeniranjeDataLoadedListener;

    private boolean scansArrived= false;

    public SkeniranjeDataLoader(SkeniranjeDataLoadedListener skeniranjeDataLoadedListener)
    {
        this.mSkeniranjeDataLoadedListener = skeniranjeDataLoadedListener;
    }

    public void loadDataByUserId(String userId) {

        SkeniranjeData scanWS = new SkeniranjeData(scanHandler);

        scanWS.DownloadByUserId(userId);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler scanHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                List<Skeniranje> listaSkeniranja = (List<Skeniranje>) result;
                /*for(Store store : stores){
                    store.save();
                }*/
                scansArrived = true;
                checkDataArrival(listaSkeniranja);
            }
        }
    };


    private void checkDataArrival(List<Skeniranje> skeniranjeList){
        if(scansArrived){
            mSkeniranjeDataLoadedListener.SkeniranjeOnDataLoaded(skeniranjeList);
        }
    }

}
