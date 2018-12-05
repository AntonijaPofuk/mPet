package Retrofit.Model;

import com.google.gson.annotations.SerializedName;

public class Ljubimac {
    @SerializedName("id_ljubimca")
    public String id_ljubimca;
    @SerializedName("ime")
    public String ime;
    @SerializedName("godina")
    public String godina;
    @SerializedName("masa")
    public String masa;
    @SerializedName("vrsta")
    public String vrsta;
    @SerializedName("spol")
    public String spol;
    @SerializedName("opis")
    public String opis;
    @SerializedName("url_slike")
    public String url_slike;
    @SerializedName("kartica")
    public String kartica;
    @SerializedName("korisnik")
    public String korisnik ;

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getId_ljubimca() {
        return id_ljubimca;
    }

    public void setId_ljubimca(String id_ljubimca) {
        this.id_ljubimca = id_ljubimca;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getGodina() {
        return godina;
    }

    public void setGodina(String godina) {
        this.godina = godina;
    }

    public String getMasa() {
        return masa;
    }

    public void setMasa(String masa) {
        this.masa = masa;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public String getSpol() {
        return spol;
    }

    public void setSpol(String spol) {
        this.spol = spol;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getUrl_slike() {
        return url_slike;
    }

    public void setUrl_slike(String url_slike) {
        this.url_slike = url_slike;
    }

    public String getKartica() {
        return kartica;
    }

    public void setKartica(String kartica) {
        this.kartica = kartica;
    }
}
