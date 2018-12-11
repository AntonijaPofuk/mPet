package Retrofit.RemotePost;

import Retrofit.DataPost.PrijavaRequest;
import Retrofit.DataPost.PrijavaResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface KorisnikPrijavaServis {
    @POST
    Call<PrijavaResponse> createKorisnik(@Body PrijavaRequest korisnikBody, @Url String url);
}
