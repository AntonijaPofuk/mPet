package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.RemotePost.KarticaOnDataPostedListener;
import Retrofit.RemotePost.KarticaService;
import Retrofit.RemotePost.LjubimacOnDataPostedListener;
import Retrofit.RemotePost.LjubimacService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LjubimacMethod {

    LjubimacOnDataPostedListener ljubimacListener;

    public LjubimacMethod(LjubimacOnDataPostedListener ljubimacDataLoadedListener) {
        this.ljubimacListener = ljubimacDataLoadedListener;
    }

    public void Upload(String lj1, String lj2, String kartica, String mod) {

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




        LjubimacService api = retrofit.create(LjubimacService.class);

        LjubimacRequest request = new LjubimacRequest();

        request.setId_ljubimca1(lj1);
        request.setId_ljubimca2(lj2);
        request.setKartica(kartica);
        request.setMod(mod);

        Call<LjubimacResponse> LjubimacResponseCall = api.switchPet(request,"https://airprojekt.000webhostapp.com/pridruzivanjeKartici.php");

        LjubimacResponseCall.enqueue(new Callback<LjubimacResponse>() {
            @Override
            public void onResponse(Call<LjubimacResponse> call, Response<LjubimacResponse> response) {

                LjubimacResponse body = response.body();
                Body[0]=body.LjubimacID;
                if(Body[0]!="greska") ljubimacListener.onDataPostedLjubimac(Body[0]);
                else ljubimacListener.onDataPostedLjubimac("0");
            }

            @Override
            public void onFailure(Call<LjubimacResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

}
