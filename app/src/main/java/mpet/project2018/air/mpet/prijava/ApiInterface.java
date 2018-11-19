package mpet.project2018.air.mpet.prijava;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("api/{email}/{lozinka}")
    Call<Login> authenticate(@Path("email") String email, @Path("lozinka") String lozinka);

}
