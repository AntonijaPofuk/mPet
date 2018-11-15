package Retrofit.DataPost;


import com.google.gson.annotations.SerializedName;

public class KorisnikDataResponse {
    @SerializedName("id")
    String KorisnikID="";

    public String getKorisnikID() {
        return KorisnikID;
    }
}
