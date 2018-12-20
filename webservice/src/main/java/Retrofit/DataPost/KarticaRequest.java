package Retrofit.DataPost;

public class KarticaRequest {
    public  String id_kartice;
    public String korisnik;

    public String getId_kartice() {
        return id_kartice;
    }

    public void setId_kartice(String id_kartice) {
        this.id_kartice = id_kartice;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }
}
