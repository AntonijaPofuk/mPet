package mpet.project2018.air.database.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.ByteArrayOutputStream;

import mpet.project2018.air.database.MainDatabase;

/**
 * Entitetna klasa za spremanje entiteta Ljubimac u lokalnu bazu podataka
 */
@Table(database = MainDatabase.class)
public class Ljubimac extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column int id_ljubimca;

    @Column String ime;
    @Column int godine;
    @Column float masa;
    @Column String vrsta_zivotinje;
    @Column String spol;
    @Column String opis;
    @Column String url_slike;

    @ForeignKey(tableClass = Korisnik.class)
    @Column Korisnik korisnik;
    @ForeignKey(tableClass = Kartica.class)
    @Column Kartica kartica;

    @Column String slika;

    public Ljubimac(int id_ljubimca, String ime, int godine, long masa, String vrsta_zivotinje, String spol, String opis, String url_slike, Korisnik korisnik) {
        this.id_ljubimca = id_ljubimca;
        this.ime = ime;
        this.godine = godine;
        this.masa = masa;
        this.vrsta_zivotinje = vrsta_zivotinje;
        this.spol = spol;
        this.opis = opis;
        this.url_slike = url_slike;
        this.korisnik=korisnik;
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

    public void setMasa(float masa) {
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

    public Kartica getKartica() {return kartica;}

    public String getKarticaNumber(){
        if(kartica==null){
            return "0";
        }
        else{
            return kartica.getId_kartice();
        }
    }

    public void setKartica(Kartica kartica) {
        this.kartica = kartica;
    }

    /*****/
    public Bitmap getSlika(){
        try {
            byte [] encodeByte=Base64.decode(slika,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void  setSlika(Bitmap bmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] bi=baos.toByteArray();
        String temp=Base64.encodeToString(bi, Base64.DEFAULT);
        this.slika=temp;
    }
}
