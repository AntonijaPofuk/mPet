package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.RemotePost.KorisnikService;
import Retrofit.RemotePost.SkeniranjeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SkeniranjeMethod {

    public static String Upload(String datum, String vrijeme, String kontakt, String procitano, String koordinata_x, String koordinata_y, String korisnik, String kartica) {

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




        SkeniranjeService api = retrofit.create(SkeniranjeService.class);

       SkeniranjeRequest request = new SkeniranjeRequest();

        request.setDatum(datum);
        request.setVrijeme(vrijeme);
        request.setKontakt(kontakt);
        request.setKoordinata_x(koordinata_x);
        request.setKoordinata_y(koordinata_y);
        request.setProcitano(procitano);
        request.setKorisnik(korisnik);
        request.setKartica(kartica);

        Call<SkeniranjeResponse> SkeniranjeResponseCall = api.createScan(request,"https://airprojekt.000webhostapp.com/zapisSkeniranja.php");

        SkeniranjeResponseCall.enqueue(new Callback<SkeniranjeResponse>() {
            @Override
            public void onResponse(Call<SkeniranjeResponse> call, Response<SkeniranjeResponse> response) {

                SkeniranjeResponse body = response.body();
                Body[0]=body.SkeniranjeID;

            }

            @Override
            public void onFailure(Call<SkeniranjeResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        return Body[0];

    }

}
