package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import Retrofit.DataGet.KarticaData;
import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KarticaDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Kartica;
import Retrofit.Model.Korisnik;
import mpet.project2018.air.database.entities.Korisnik_Table;

public class KarticaDataLoader {

    protected KarticaDataLoadedListener mKarticaDataLoadedListener;

    private boolean cardsArrived= false;

    private String idKorisnika;

    public KarticaDataLoader(KarticaDataLoadedListener karticaDataLoadedListener)
    {
        this.mKarticaDataLoadedListener = karticaDataLoadedListener;
    }

    public void loadDataByuserId(String userId) {
        idKorisnika=userId;
        KarticaData cardWS = new KarticaData(cardHandler);

        cardWS.DownloadByUserId(userId);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler cardHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, boolean prijava) {
            if(ok){
                List<Kartica> listaKartica = (List<Kartica>) result;
                if(prijava) {
                    saveCardsInLocalDatabase(listaKartica);
                }
                cardsArrived = true;
                checkDataArrival(listaKartica);
            }
        }
    };

    private void saveCardsInLocalDatabase(List<Kartica> listaKartica)
    {
        for (Kartica kartica : listaKartica)
        {
            mpet.project2018.air.database.entities.Korisnik k=new SQLite().select().from(mpet.project2018.air.database.entities.Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt(idKorisnika))).querySingle();
            mpet.project2018.air.database.entities.Kartica newKartica=new mpet.project2018.air.database.entities.Kartica();
            newKartica.setId_kartice(kartica.id_kartice);
            newKartica.setKorisnik(k);
            newKartica.save();
        }
    }

    private void checkDataArrival(List<Kartica> karticaList){
        if(cardsArrived){
            mKarticaDataLoadedListener.KarticaOnDataLoaded(karticaList);
        }
    }
}
