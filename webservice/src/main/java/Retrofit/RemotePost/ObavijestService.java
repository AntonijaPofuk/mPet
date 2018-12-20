package Retrofit.RemotePost;

import Retrofit.DataPost.ObavijestiRequest;
import Retrofit.DataPost.ObavijestiResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ObavijestService {
    @POST
    Call<ObavijestiResponse> createKorisnik(@Body ObavijestiRequest obavijestBody, @Url String url);
}
