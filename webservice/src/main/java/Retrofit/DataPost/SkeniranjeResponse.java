package Retrofit.DataPost;

import com.google.gson.annotations.SerializedName;

public class SkeniranjeResponse {

    @SerializedName("id")
    String SkeniranjeID="";

    public String getKorisnikID() {
        return SkeniranjeID;
    }
}
