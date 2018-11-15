package Retrofit.RemoteGet;

import java.util.List;

import hr.com.webservice.Retrofit.Model.Ljubimac;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface LjubimciService {

    @GET
    Call<List<Ljubimac>> GetLjubimac(@Url String Url);
}
