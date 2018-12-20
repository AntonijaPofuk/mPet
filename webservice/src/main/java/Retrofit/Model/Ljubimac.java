package Retrofit.Model;

import com.google.gson.annotations.SerializedName;

public class Ljubimac {

    @SerializedName("id")
    public String id;
    @SerializedName("ime")
    public String ime;
    @SerializedName("godina")
    public int godina;
    @SerializedName("masa")
    public float masa;

    @SerializedName("vrsta")
    public String vrsta;
    @SerializedName("spol")
    public String spol;
    @SerializedName("opis")
    public String opis;
    @SerializedName("url_slike")
    public String url_slike;

    @SerializedName("vlasnik")
    public String vlasnik;
    @SerializedName("kartica")
    public String kartica;


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

    public String getGodina() {
        return String.valueOf(godina);
    }

    public void setGodina(String godina) {
        this.godina = Integer.parseInt(godina);
    }

    public String getMasa() {
        return String.valueOf(masa);
    }

    public void setMasa(String masa) {
        this.masa = Float.parseFloat(masa);
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

    public String getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(String vlasnik) {
        this.vlasnik = vlasnik;
    }

    public String getKartica() {
        return kartica;
    }

    public void setKartica(String kartica) {
        this.kartica = kartica;
    }


}
