package Retrofit.RemoteGet;

import java.util.List;

import hr.com.webservice.Retrofit.Model.Korisnik;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface KorisniciService {

    @GET
    Call<List<Korisnik>> GetKorisnik(@Url String Url);
}
