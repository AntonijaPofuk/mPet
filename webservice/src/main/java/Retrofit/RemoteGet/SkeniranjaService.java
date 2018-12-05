package Retrofit.RemoteGet;

import java.util.List;

import Retrofit.Model.Skeniranje;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface SkeniranjaService {

    @GET
    Call<List<Skeniranje>> GetSkeniranje(@Url String Url);
}
