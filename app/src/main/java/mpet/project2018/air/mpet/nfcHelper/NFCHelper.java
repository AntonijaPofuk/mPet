package mpet.project2018.air.mpet.nfcHelper;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Retrofit.DataGet.KarticaData;
import Retrofit.DataGet.KorisnikData;
import Retrofit.DataGet.LjubimacData;
import Retrofit.Model.Kartica;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;

public class NFCHelper
{
    // Metoda koja provjerava da li je uneseni ili skenirani kod u odgovvarajućem formatu
    public static boolean checkFormat(String code )
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

    // najbolje da vrati Ljubimca pa pomoću njega sve ostalo dohvatimo

    public static void checkForCodeInDatabase(String code, Context c)
    {
        LjubimacData instancaMetodeZaDohvatPodataka=new LjubimacData();
        List<Ljubimac> skeniraniLjubimac;
        skeniraniLjubimac=instancaMetodeZaDohvatPodataka.DownloadByTag(code);
        if(skeniraniLjubimac.isEmpty()) Toast.makeText(c, "no", Toast.LENGTH_SHORT).show();
        else
        {
            String ime=skeniraniLjubimac.get(0).ime;
            Toast.makeText(c,ime, Toast.LENGTH_SHORT).show();
        }
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
    }




}
