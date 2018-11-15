package Retrofit.RemotePost;

import Retrofit.DataPost.KorisnikDataRequest;
import Retrofit.DataPost.KorisnikDataResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface KorisnikService {
    @POST
    Call<KorisnikDataResponse> createKorisnik(@Body KorisnikDataRequest korisnikBody, @Url String url);
}
