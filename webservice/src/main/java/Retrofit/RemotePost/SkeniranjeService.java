package Retrofit.RemotePost;

import Retrofit.DataPost.RegistracijaRequest;
import Retrofit.DataPost.RegistracijaResponse;
import Retrofit.DataPost.SkeniranjeRequest;
import Retrofit.DataPost.SkeniranjeResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SkeniranjeService {

    @POST
    Call<SkeniranjeResponse> createScan(@Body SkeniranjeRequest scanBody, @Url String url);

}
