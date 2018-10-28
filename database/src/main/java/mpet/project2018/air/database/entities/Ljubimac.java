package mpet.project2018.air.database.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import mpet.project2018.air.database.MainDatabase;

@Table(database = MainDatabase.class)
public class Ljubimac extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column int id_ljubimca;

    @Column String ime;
    @Column int godine;
    @Column long masa;
    @Column String vrsta_zivotinje;
    @Column String spol;
    @Column String opis;
    @Column String url_slike;

    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;
    @ForeignKey(tableClass = Kartica.class)
    @Column Kartica kartica;

    public Ljubimac(int id_ljubimca, String ime, int godine, long masa, String vrsta_zivotinje, String spol, String opis, String url_slike) {
        this.id_ljubimca = id_ljubimca;
        this.ime = ime;
        this.godine = godine;
        this.masa = masa;
        this.vrsta_zivotinje = vrsta_zivotinje;
        this.spol = spol;
        this.opis = opis;
        this.url_slike = url_slike;
    }

    public Ljubimac() {
    }

    public int getId_ljubimca() {
        return id_ljubimca;
    }

    public void setId_ljubimca(int id_ljubimca) {
        this.id_ljubimca = id_ljubimca;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getGodine() {
        return godine;
    }

    public void setGodine(int godine) {
        this.godine = godine;
    }

    public float getMasa() {
        return masa;
    }

    public void setMasa(long masa) {
        this.masa = masa;
    }

    public String getVrsta_zivotinje() {
        return vrsta_zivotinje;
    }

    public void setVrsta_zivotinje(String vrsta_zivotinje) {
        this.vrsta_zivotinje = vrsta_zivotinje;
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

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Kartica getKartica() {
        return kartica;
    }

    public void setKartica(Kartica kartica) {
        this.kartica = kartica;
    }
}
