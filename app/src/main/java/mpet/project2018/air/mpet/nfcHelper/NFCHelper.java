package mpet.project2018.air.mpet.nfcHelper;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Retrofit.DataGet.LjubimacData;
import Retrofit.DataGet.MyCallback;
import Retrofit.Model.Ljubimac;

public class NFCHelper
{

    private List<Ljubimac> listaLjubimaca;

    // Metoda koja provjerava da li je uneseni ili skenirani kod u odgovvarajućem formatu
    public boolean checkFormat(String code )
    {
        String tagContent = code;
        if(tagContent.length()==10 && tagContent.matches(("[A-Za-z0-9]+")))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*public boolean checkForCode(String code)
    {
    LjubimacData instancaMetodeZaDohvatPodataka=new LjubimacData();

    instancaMetodeZaDohvatPodataka.DownloadByTag(code, new MyCallback<List<Ljubimac>>() {
        @Override
    public void next(List<Ljubimac> result) {
        listaLjubimaca=result;
    }
    });
    if(listaLjubimaca.isEmpty()) return false;
        else return true;

    }*/







   /* public static Ljubimac getLjubimac(String code)
    {
        LjubimacData instancaMetodeZaDohvatPodataka=new LjubimacData();
        List<Ljubimac> dohvaceniLjubimac;
        dohvaceniLjubimac=instancaMetodeZaDohvatPodataka.DownloadByTag(code);
        return dohvaceniLjubimac.get(0);
    }

    public Kartica getKartica(String idKartice)
    {
        KarticaData instancaMetodeZaDohvatPodataka=new KarticaData();
        List<Kartica> dohvacenaKartica;
        dohvacenaKartica=instancaMetodeZaDohvatPodataka.Download(idKartice);
        return dohvacenaKartica.get(0);
    }

    public Korisnik getKorisnik(String idKorisnika)
    {
        KorisnikData instancaMetodeZaDohvatPodataka=new KorisnikData();
        List<Korisnik> dohvaceniKorisnik;
        dohvaceniKorisnik=instancaMetodeZaDohvatPodataka.Download(idKorisnika);
        return dohvaceniKorisnik.get(0);
    }*/




}
