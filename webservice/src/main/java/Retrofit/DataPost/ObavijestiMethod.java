package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.RemotePost.KorisnikService;
import Retrofit.RemotePost.ObavijestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ObavijestiMethod {

    public static String Upload(String ID) {

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

        Call<ObavijestiResponse> ObavijestResponseCall = api.createKorisnik(request,"https://airprojekt.000webhostapp.com/procitaneObavijesti.php");

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
