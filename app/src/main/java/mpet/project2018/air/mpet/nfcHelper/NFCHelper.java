package mpet.project2018.air.mpet.nfcHelper;

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
