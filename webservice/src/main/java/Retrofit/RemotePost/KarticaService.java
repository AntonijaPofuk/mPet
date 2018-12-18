package Retrofit.RemotePost;

import Retrofit.DataPost.KarticaRequest;
import Retrofit.DataPost.KarticaResponse;
import Retrofit.DataPost.SkeniranjeRequest;
import Retrofit.DataPost.SkeniranjeResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface KarticaService {

    @POST
    Call<KarticaResponse> createTag(@Body KarticaRequest tagBody, @Url String url);
}
