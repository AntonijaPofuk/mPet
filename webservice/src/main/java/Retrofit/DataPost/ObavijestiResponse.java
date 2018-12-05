package Retrofit.DataPost;

import com.google.gson.annotations.SerializedName;

public class ObavijestiResponse {

    @SerializedName("id")
    String id;

    public String getId() {
        return id;
    }
}
