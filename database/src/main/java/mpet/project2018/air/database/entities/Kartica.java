package mpet.project2018.air.database.entities;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import mpet.project2018.air.database.MainDatabase;

/**
 * Entitetna klasa za spremanje Kartice u lokalnu bazu podataka
 */
@Table(database = MainDatabase.class)
public class Kartica extends BaseModel {

    @PrimaryKey
    @Column String id_kartice;


    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;

    public Kartica(String id_kartice) {
        this.id_kartice = id_kartice;
    }

    public Kartica() {
    }

    public String getId_kartice() {
        return id_kartice;
    }

    public void setId_kartice(String id_kartice) {
        this.id_kartice = id_kartice;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
