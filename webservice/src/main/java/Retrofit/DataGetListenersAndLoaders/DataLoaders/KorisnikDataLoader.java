package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import java.util.List;

import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGet.LjubimacData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;

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

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler userHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                List<Korisnik> listaKorisnika = (List<Korisnik>) result;
                /*for(Store store : stores){
                    store.save();
                }*/
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

}
