package mpet.project2018.air.database.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.ByteArrayOutputStream;

import mpet.project2018.air.database.MainDatabase;

@Table(database = MainDatabase.class)
public class Korisnik extends BaseModel {

@PrimaryKey(autoincrement = true)
@Column int id_korisnika;

@Column String ime;
@Column String prezime;
@Column String korisnicko_ime;
@Column String lozinka;
@Column String email;
@Column String adresa;
@Column String broj_mobitela;
@Column String broj_telefona;
@Column String url_profilna;
@Column String slika;

    public Korisnik(int id_korisnika, String ime, String prezime, String korisnicko_ime, String lozinka, String email, String adresa, String broj_mobitela, String broj_telefona, String url_profilna) {
        this.id_korisnika = id_korisnika;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnicko_ime = korisnicko_ime;
        this.lozinka = lozinka;
        this.email = email;
        this.adresa = adresa;
        this.broj_mobitela = broj_mobitela;
        this.broj_telefona = broj_telefona;
        this.url_profilna = url_profilna;
    }

    public Korisnik() {
    }

    public int getId_korisnika() {
        return id_korisnika;
    }

    public void setId_korisnika(int id_korisnika) {
        this.id_korisnika = id_korisnika;
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

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
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
