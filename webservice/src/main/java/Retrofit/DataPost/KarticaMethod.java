package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.RemotePost.KarticaOnDataPostedListener;
import Retrofit.RemotePost.KarticaService;
import Retrofit.RemotePost.SkeniranjeOnDataPostedListener;
import Retrofit.RemotePost.SkeniranjeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KarticaMethod {

    KarticaOnDataPostedListener karticaListener;

    public KarticaMethod(KarticaOnDataPostedListener karticaListener) {
        this.karticaListener = karticaListener;
    }

    /**
     * spremanje nove kartice na poslužitelj
     * @param kartica
     * @param korisnikID
     */
    public void Upload(String kartica, String korisnikID) {

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

        KarticaService api = retrofit.create(KarticaService.class);

        KarticaRequest request = new KarticaRequest();

        request.setId_kartice(kartica);
        request.setKorisnik(korisnikID);

        Call<KarticaResponse> KarticaResponseCall = api.createTag(request,"https://airprojekt.000webhostapp.com/novaKartica.php");

        KarticaResponseCall.enqueue(new Callback<KarticaResponse>() {
            @Override
            public void onResponse(Call<KarticaResponse> call, Response<KarticaResponse> response) {

                KarticaResponse body = response.body();
                Body[0]=body.KarticaID;

                karticaListener.onDataPosted(Body[0]);
            }

            @Override
            public void onFailure(Call<KarticaResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}
