package Retrofit.Model;

import com.google.gson.annotations.SerializedName;

public class Kartica {
    @SerializedName("id_kartice")
    public String id_kartice;
    @SerializedName("korisnik")
    public String korisnik;



    public String getId_kartice() {
        return id_kartice;
    }

    public String getKorisnik() {
        return korisnik;
    }
}
