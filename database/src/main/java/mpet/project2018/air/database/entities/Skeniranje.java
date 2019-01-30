package mpet.project2018.air.database.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

import mpet.project2018.air.database.MainDatabase;

/**
 * Entitetna klasa za spremanje entiteta Skeniranje u lokalnu bazu podataka
 */
@Table(database = MainDatabase.class)
public class Skeniranje extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column int id_skeniranja;

    @Column Date datum;
    @Column String vrijeme;
    @Column String kontakt;
    @Column String procitano;
    @Column String koordinata_x;
    @Column String koordinata_y;

    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;
    @ForeignKey(tableClass = Kartica.class)
    @Column Kartica kartica;

    public Skeniranje() {
    }

    public Skeniranje(int id_skeniranja, Date datum, String vrijeme, String kontakt, String procitano, String koordinata_x, String koordinata_y) {
        this.id_skeniranja = id_skeniranja;
        this.datum = datum;
        this.vrijeme = vrijeme;
        this.kontakt = kontakt;
        this.procitano = procitano;
        this.koordinata_x = koordinata_x;
        this.koordinata_y = koordinata_y;
    }

    public int getId_skeniranja() {
        return id_skeniranja;
    }

    public void setId_skeniranja(int id_skeniranja) {
        this.id_skeniranja = id_skeniranja;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
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
