package Retrofit.DataPost;

import com.google.gson.annotations.SerializedName;

public class LjubimacPodaciResponse {
    @SerializedName("id")
    String id;

    public String getLjubimacID() {
        return id;
    }
}
