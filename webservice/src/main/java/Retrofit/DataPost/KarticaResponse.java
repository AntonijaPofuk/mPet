package Retrofit.DataPost;

import com.google.gson.annotations.SerializedName;

public class KarticaResponse {

    @SerializedName("id")
    String KarticaID="";

    public String getKarticaID() {
        return KarticaID;
    }
}
