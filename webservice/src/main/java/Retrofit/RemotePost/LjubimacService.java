package Retrofit.RemotePost;

import Retrofit.DataPost.KarticaRequest;
import Retrofit.DataPost.KarticaResponse;
import Retrofit.DataPost.LjubimacRequest;
import Retrofit.DataPost.LjubimacResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface LjubimacService {

    @POST
    Call<LjubimacResponse> switchPet(@Body LjubimacRequest tagBody, @Url String url);
}
