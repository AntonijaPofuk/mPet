package Retrofit.Model;

import com.google.gson.annotations.SerializedName;

public class Korisnik {
    @SerializedName("id")
    public String id;
    @SerializedName("ime")
    public String ime;
    @SerializedName("prezime")
    public String prezime;
    @SerializedName("korisnicko_ime")
    public String korisnicko_ime;
    @SerializedName("email")
    public String email;
    @SerializedName("adresa")
    public String adresa;
    @SerializedName("broj_mobitela")
    public String broj_mobitela;
    @SerializedName("broj_telefona")
    public String broj_telefona;
    @SerializedName("url_profilna")
    public String url_profilna;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBroj_mobitela() {
        return broj_mobitela;
    }

    public void setBroj_mobitela(String broj_mobitela) {
        this.broj_mobitela = broj_mobitela;
    }

    public String getBroj_telefona() {
        return broj_telefona;
    }

    public void setBroj_telefona(String broj_telefona) {
        this.broj_telefona = broj_telefona;
    }

    public String getUrl_profilna() {
        return url_profilna;
    }

    public void setUrl_profilna(String url_profilna) {
        this.url_profilna = url_profilna;
    }

}
