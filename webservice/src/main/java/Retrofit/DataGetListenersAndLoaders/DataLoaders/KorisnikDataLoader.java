package Retrofit.DataGetListenersAndLoaders.DataLoaders;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.List;
import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.KorisnikDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Korisnik;

/**
 * Klasa za ostvarenje komunikacije između klase koja postavlja zahtjev prema web servisu i klase koja ga zaista i izvršava
 */
public class KorisnikDataLoader {

    protected KorisnikDataLoadedListener mKorisnikDataLoadedListener;
    private boolean usersArrived= false;
    private List<Korisnik> lista;
    private Integer userCount=0;

    /**
     * Konstruktor klase
     * @param korisnikDataLoadedListener objekt koji postavlja GET zahtjev i kojem se vraćaju podaci pristigli odgovormo web servisa
     */
    public KorisnikDataLoader(KorisnikDataLoadedListener korisnikDataLoadedListener)
    {
        this.mKorisnikDataLoadedListener = korisnikDataLoadedListener;
    }

    /**
     * Metoda koja poziva metodu za dohvat Korisnikovih podatak prema njegovom ID-u
     * @param userId ID korisnika čiji se podaci dohvaćaju
     */
    public void loadDataById(String userId) {

        KorisnikData userWS = new KorisnikData(userHandler);

        userWS.DownloadByUserId(userId);

    }

    /**
     * Metoda za dohvat korisnika povezanih sa korisnikom čiji je ID prosljeđen, kod prijave
     * @param userId ID korisnika
     */
    public void loadUsersByUserId(String userId) {

        KorisnikData userWS = new KorisnikData(userHandler);

        userWS.DownloadUsersByUserId(userId);

    }

    /**
     * Realizacija sučelja kojim se javlja klasi kako su pristigli podaci u odgovoru web servisa
     */
    WebServiceHandler userHandler = new WebServiceHandler() {
        @Override
        public void onDataArrived(Object result, boolean ok, boolean prijava) {
            if(ok){
                usersArrived = true;
                List<Korisnik> listaKorisnika = (List<Korisnik>) result;
                lista=listaKorisnika;
                userCount=lista.size();
                if(prijava){
                    saveUserInLocalDatabase(listaKorisnika);
                }

               else checkDataArrival(listaKorisnika);
            }
        }
    };


    /**
     * Provjera pristiglosti podatak i njihovo prosljeđivanje objektu koji je postavio prvotni zahtjev
     * @param korisnikList
     */
    private void checkDataArrival(List<Korisnik> korisnikList){
        if(usersArrived){
            mKorisnikDataLoadedListener.KorisnikOnDataLoaded(korisnikList);
        }
    }

    /**
     * Metoda za spremanje pristiglih podataka u lokalnu bazu podataka
     * @param listaKorisnika lista pristiglih korisnika
     */
    private void saveUserInLocalDatabase(List<Korisnik> listaKorisnika)
    {
        for (Korisnik korisnik : listaKorisnika)
        {
            mpet.project2018.air.database.entities.Korisnik newKorisnik=new mpet.project2018.air.database.entities.Korisnik(Integer.parseInt(korisnik.id),
                    korisnik.ime,korisnik.prezime,korisnik.korisnicko_ime,null,korisnik.email,korisnik.adresa,korisnik.broj_mobitela,
                    korisnik.broj_telefona,korisnik.url_profilna);

            loadBitmap("https://airprojekt.000webhostapp.com/slike_profila/"+newKorisnik.getUrl_profilna(),newKorisnik);

        }
    }

    /**
     * Metoda za dohvat slike danog korisnika i spremanje istog u lokalnu bazu podataka
     * @param url url slike korisnika
     * @param kor korisnik čija se slika dohvaća
     */
    public void loadBitmap(String url, final mpet.project2018.air.database.entities.Korisnik kor) {

        Target loadtarget;
        loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                kor.setSlika(bitmap);
                kor.save();

                userCount=userCount-1;

                if(userCount==0) checkDataArrival(lista);
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };

        Picasso.get().load(url).into(loadtarget);
    }


}
