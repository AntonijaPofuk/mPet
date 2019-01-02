package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import java.util.List;

import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Korisnik;

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

    public void loadUsersByUserId(String userId) {

        KorisnikData userWS = new KorisnikData(userHandler);

        userWS.DownloadUsersByUserId(userId);

    }

    //TODO: As an exercise, change the architecture so that you have only one AirWebServiceHandler

    WebServiceHandler userHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok) {
            if(ok){
                List<Korisnik> listaKorisnika = (List<Korisnik>) result;
                saveUserInLocalDatabase(listaKorisnika);
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

    private void saveUserInLocalDatabase(List<Korisnik> listaKorisnika)
    {
        for (Korisnik korisnik : listaKorisnika)
        {
            mpet.project2018.air.database.entities.Korisnik newKorisnik=new mpet.project2018.air.database.entities.Korisnik(Integer.parseInt(korisnik.id),
                    korisnik.ime,korisnik.prezime,korisnik.korisnicko_ime,null,korisnik.email,korisnik.adresa,korisnik.broj_mobitela,
                    korisnik.broj_telefona,korisnik.url_profilna);
            newKorisnik.save();
        }
    }

}
