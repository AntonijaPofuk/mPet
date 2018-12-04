package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import java.util.List;

import Retrofit.DataGet.KarticaData;
import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KarticaDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Kartica;
import Retrofit.Model.Korisnik;

public class KarticaDataLoader {

    protected KarticaDataLoadedListener mKarticaDataLoadedListener;

    private boolean usersArrived= false;

    public KarticaDataLoader(KarticaDataLoadedListener karticaDataLoadedListener)
    {
        this.mKarticaDataLoadedListener = karticaDataLoadedListener;
    }

    public void loadDataByuserId(String userId) {

        KarticaData cardWS = new KarticaData(cardHandler);

        cardWS.Download(userId);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler cardHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                List<Kartica> listaKartica = (List<Kartica>) result;
                /*for(Store store : stores){
                    store.save();
                }*/
                usersArrived = true;
                checkDataArrival(listaKartica);
            }
        }
    };


    private void checkDataArrival(List<Kartica> karticaList){
        if(usersArrived){
            mKarticaDataLoadedListener.KarticaOnDataLoaded(karticaList);
        }
    }
}
