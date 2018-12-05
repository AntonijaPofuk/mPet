package Retrofit.Model;

import com.google.gson.annotations.SerializedName;

public class Skeniranje {
    @SerializedName("id_skeniranja")
    public String id_skeniranja;
    @SerializedName("datum")
    public String datum;
    @SerializedName("vrijeme")
    public String vrijeme;
    @SerializedName("kontakt")
    public String kontakt;
    @SerializedName("procitano")
    public String procitano;
    @SerializedName("koordinata_x")
    public String koordinata_x;
    @SerializedName("koordinata_y")
    public String koordinata_y;
    @SerializedName("korisnik")
    public String korisnik;
    @SerializedName("kartica")
    public String kartica;

    public String getId_skeniranja() {
        return id_skeniranja;
    }

    public void setId_skeniranja(String id_skeniranja) {
        this.id_skeniranja = id_skeniranja;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public String getProcitano() {
        return procitano;
    }

    public void setProcitano(String procitano) {
        this.procitano = procitano;
    }

    public String getKoordinata_x() {
        return koordinata_x;
    }

    public void setKoordinata_x(String koordinata_x) {
        this.koordinata_x = koordinata_x;
    }

    public String getKoordinata_y() {
        return koordinata_y;
    }

    public void setKoordinata_y(String koordinata_y) {
        this.koordinata_y = koordinata_y;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getKartica() {
        return kartica;
    }

    public void setKartica(String kartica) {
        this.kartica = kartica;
    }
}
