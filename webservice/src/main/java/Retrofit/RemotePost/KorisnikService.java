package Retrofit.RemotePost;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import Retrofit.DataPost.RegistracijaRequest;
import Retrofit.DataPost.RegistracijaResponse;

public interface KorisnikService {
    @POST
    Call<RegistracijaResponse> createKorisnik(@Body RegistracijaRequest korisnikBody, @Url String url);
}
