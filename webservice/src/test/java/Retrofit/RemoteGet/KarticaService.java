package Retrofit.RemoteGet;

import java.util.List;

import hr.com.webservice.Retrofit.Model.Kartica;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface KarticaService {

    @GET
    Call<List<Kartica>> GetKartica(@Url String url);


}
