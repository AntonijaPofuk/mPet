package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.util.List;
import Retrofit.DataGet.KarticaData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KarticaDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Kartica;
import mpet.project2018.air.database.entities.Korisnik_Table;

/**
 * Klasa koja upravlja komunikacijom između fragmenata koji upućuje GET zahtjev i klase koja zapravo šalje zahtjev web servisu
 */
public class KarticaDataLoader {

    protected KarticaDataLoadedListener mKarticaDataLoadedListener;
    private boolean cardsArrived= false;
    private String idKorisnika;

    /**
     * Konstruktor klase
     * @param karticaDataLoadedListener objekt koji traži podatke za daljnju obradu, najčešće fragment
     */
    public KarticaDataLoader(KarticaDataLoadedListener karticaDataLoadedListener)
    {
        this.mKarticaDataLoadedListener = karticaDataLoadedListener;
    }

    /**
     * Metoda koja poziva metodu klase KarticaData u svrhu preuzimanja zapisa o karticama danog korisnika
     * @param userId korisnik čije se kartice dohvaćaju, njego ID u udaljenoj bazi podataka
     */
    public void loadDataByuserId(String userId) {
        idKorisnika=userId;
        KarticaData cardWS = new KarticaData(cardHandler);

        cardWS.DownloadByUserId(userId);

    }

    /**
     * Realizirano sučelje WebServiceHandler koje služi kao listener za pristigle podatke, prosljeđuje se loadDataByUserId metodom
     */
    WebServiceHandler cardHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, boolean prijava) {
            if(ok){
                List<Kartica> listaKartica = (List<Kartica>) result;
                cardsArrived = true;
                if(prijava) {
                    saveCardsInLocalDatabase(listaKartica);
                    checkDataArrival(listaKartica);
                }
                else checkDataArrival(listaKartica);
            }
        }
    };

    /**
     * Metoda za spremanje primljene liste kartica u lokalnu bazu podataka
     * @param listaKartica lista kartica kreirana iz tijela odgovora web servisa na zahtjev
     */
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

    /**
     * Metoda koja provjerava dostupnost podataka i o tome obaještava objekt, fragment, koji je prvotno poslao zahtjev za podacima
     * @param karticaList lista objekata koji bili traženi
     */
    private void checkDataArrival(List<Kartica> karticaList){
        if(cardsArrived){
            mKarticaDataLoadedListener.KarticaOnDataLoaded(karticaList);
        }
    }
}
