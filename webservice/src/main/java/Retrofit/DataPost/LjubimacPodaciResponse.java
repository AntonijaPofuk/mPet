package Retrofit.DataPost;

import com.google.gson.annotations.SerializedName;

public class LjubimacPodaciResponse {
    @SerializedName("id")
    String LjubimacID="";

    public String getLjubimacID() {
        return LjubimacID;
    }
}
