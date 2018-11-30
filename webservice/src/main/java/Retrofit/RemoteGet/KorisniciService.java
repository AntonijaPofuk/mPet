package Retrofit.RemoteGet;

import java.util.List;

import Retrofit.Model.Korisnik;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface KorisniciService {

    @GET
    Call<List<Korisnik>> GetKorisnik(@Url String Url);
}
