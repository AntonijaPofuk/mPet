package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.RemotePost.ObavijestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ObavijestiMethod {

    /**
     * promjena vrijednosti kod skeniranja
     * @param ID
     * @param vrijednost
     * @return
     */
    public static String Upload(String ID,String vrijednost) {

        final String[] Body = new String[1];

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit;
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ObavijestService api = retrofit.create(ObavijestService.class);

        ObavijestiRequest request = new ObavijestiRequest();

        request.setID(ID);  // tu ide id prijavljenog korisnika

        request.setProcitano(vrijednost);

        Call<ObavijestiResponse> ObavijestResponseCall = api.createKorisnik(request,"https://airprojekt.000webhostapp.com/updateSkeniranje.php");

        ObavijestResponseCall.enqueue(new Callback<ObavijestiResponse>() {
            @Override
            public void onResponse(Call<ObavijestiResponse> call, Response<ObavijestiResponse> response) {

                ObavijestiResponse body = response.body();
                Body[0]=body.id;

            }

            @Override
            public void onFailure(Call<ObavijestiResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return Body[0];

    }

}
