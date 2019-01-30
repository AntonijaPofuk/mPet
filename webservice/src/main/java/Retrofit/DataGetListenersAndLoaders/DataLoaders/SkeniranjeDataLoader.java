package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import com.raizlabs.android.dbflow.sql.language.SQLite;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


import Retrofit.DataGet.SkeniranjeData;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.SkeniranjeDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;

import Retrofit.Model.Skeniranje;
import mpet.project2018.air.database.entities.Kartica_Table;
import mpet.project2018.air.database.entities.Korisnik_Table;

/**
 * Klasa za ostvarenje komunikacije između klase koja postavlja zahtjev prema web servisu i klase koja ga zaista i izvršava
 */
public class SkeniranjeDataLoader {

    protected SkeniranjeDataLoadedListener mSkeniranjeDataLoadedListener;
    private boolean scansArrived= false;
    private String idKorisnika;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Konstruktor klase koji postavlja listener na klasu koja je postavila zahtjev na web servisi
     * @param skeniranjeDataLoadedListener
     */
    public SkeniranjeDataLoader(SkeniranjeDataLoadedListener skeniranjeDataLoadedListener)
    {
        this.mSkeniranjeDataLoadedListener = skeniranjeDataLoadedListener;
    }

    /**
     * Metoda za dohvat skeniranja ljubimaca korisnika koji im je vlasnik
     * @param userId korisnik odnosno vlasnik
     */
    public void loadDataByUserId(String userId) {

        idKorisnika=userId;
        SkeniranjeData scanWS = new SkeniranjeData(scanHandler);
        scanWS.DownloadByUserId(userId);
    }

    /**
     * Metoda za dohvat skeniranja u svrhunotifikacijskog servisa
     * @param userId korisnik, vlasnik
     */
    public void loadDataForUser(String userId) {

        idKorisnika=userId;
        SkeniranjeData scanWS = new SkeniranjeData(scanHandler);
        scanWS.DownloadDataForNotification(userId);
    }


    /**
     * Instanca sučelja kao listener na pridošli odgovor web servisa
     */
    WebServiceHandler scanHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, boolean prijava) {
            if(ok){
                scansArrived = true;
                List<Skeniranje> listaSkeniranja = (List<Skeniranje>) result;
                if(prijava) {
                    saveScansInLocalDatabase(listaSkeniranja);
                    checkDataArrival(listaSkeniranja);
                }

                else checkDataArrival(listaSkeniranja);
            }
        }
    };


    /**
     * metoda koja objektu koji jje postavio zahtjev web servisu vraća odgovor istog kao listu objekata
     * @param skeniranjeList lista objekata kao odgovor na zahtjev
     */
    private void checkDataArrival(List<Skeniranje> skeniranjeList){
        if(scansArrived){
            mSkeniranjeDataLoadedListener.SkeniranjeOnDataLoaded(skeniranjeList);
        }
    }

    /**
     * Metoda za spremanje skeniranja u lokalnu bazu podataka
     * @param listaSkeniranja lista skeniranja kao odgovor web servisa
     */
    private void saveScansInLocalDatabase(List<Skeniranje> listaSkeniranja)
    {

        for (Skeniranje skeniranje : listaSkeniranja)
        {

            mpet.project2018.air.database.entities.Korisnik usr=new mpet.project2018.air.database.entities.Korisnik();
            mpet.project2018.air.database.entities.Kartica crd=new mpet.project2018.air.database.entities.Kartica();
            if(skeniranje.korisnik!=null){
                usr=new SQLite().select().from(mpet.project2018.air.database.entities.Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt(skeniranje.korisnik))).querySingle();
            }
            if(skeniranje.kartica!=null){
                crd=new SQLite().select().from(mpet.project2018.air.database.entities.Kartica.class).where(Kartica_Table.id_kartice.is(skeniranje.kartica)).querySingle();
            }
            java.util.Date datum=null;
            try {
                datum=format.parse(skeniranje.datum);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mpet.project2018.air.database.entities.Skeniranje newSkeniranje=new mpet.project2018.air.database.entities.Skeniranje(Integer.parseInt(skeniranje.id_skeniranja),datum,skeniranje.vrijeme,skeniranje.kontakt,skeniranje.procitano,skeniranje.koordinata_x,skeniranje.koordinata_y);
            newSkeniranje.setKorisnik(usr);
            newSkeniranje.setKartica(crd);
            newSkeniranje.save();

        }

    }

}
