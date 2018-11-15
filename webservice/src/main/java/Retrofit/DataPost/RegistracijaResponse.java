package Retrofit.DataPost;


import com.google.gson.annotations.SerializedName;

public class RegistracijaResponse {
    @SerializedName("id")
    String KorisnikID="";

    public String getKorisnikID() {
        return KorisnikID;
    }
}
