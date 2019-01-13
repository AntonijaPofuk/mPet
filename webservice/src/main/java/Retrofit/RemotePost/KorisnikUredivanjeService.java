package Retrofit.RemotePost;

import Retrofit.DataPost.KorisnikUredivanjeRequest;
import Retrofit.DataPost.KorisnikUredivanjeResponse;
import Retrofit.DataPost.RegistracijaRequest;
import Retrofit.DataPost.RegistracijaResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface KorisnikUredivanjeService {
    @POST
    Call<KorisnikUredivanjeResponse> updateKorisnik(@Body KorisnikUredivanjeRequest korisnikBody, @Url String url);
}
