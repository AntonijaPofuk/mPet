package Retrofit.DataPost;

import com.google.gson.annotations.SerializedName;

public class LjubimacResponse {

    @SerializedName("id")
    String LjubimacID="";

    public String getLjubimacID() {
        return LjubimacID;
    }
}
