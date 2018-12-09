package Retrofit.DataPost;


import com.google.gson.annotations.SerializedName;

public class RegistracijaResponse {
    @SerializedName("id")
    String id;

    public String getKorisnikID() {
        return id;
    }
}
