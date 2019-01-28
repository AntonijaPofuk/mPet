package Retrofit.DataGet;

import java.util.ArrayList;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Skeniranje;
import Retrofit.RemoteGet.SkeniranjaService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SkeniranjeData {

    WebServiceHandler scanServiceHandler;

    Retrofit retrofit;

    final static List<Skeniranje> SkeniranjeList=new ArrayList<Skeniranje>();

    public SkeniranjeData(WebServiceHandler scanServiceHandler) {

        this.scanServiceHandler=scanServiceHandler;

        OkHttpClient client = new OkHttpClient();

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public void DownloadByUserId(String korisnikId){

        SkeniranjaService api = retrofit.create(SkeniranjaService.class);

        Call<List<Skeniranje>> call = api.GetSkeniranje("https://airprojekt.000webhostapp.com/services.php?skeniranja_korID="+korisnikId);

        call.enqueue(new Callback<List<Skeniranje>>() {
            @Override
            public void onResponse(Call<List<Skeniranje>> call, Response<List<Skeniranje>> response) {
                List<Skeniranje> skeniranje = response.body();
                SkeniranjeList.clear();
                if(skeniranje.get(0)==null)
                {
                    scanServiceHandler.onDataArrived(SkeniranjeList,true,true);
                }
                else {
                    for (Skeniranje s : skeniranje) {
                        Skeniranje skeniranjeNew = new Skeniranje();
                        skeniranjeNew.datum = s.datum;
                        skeniranjeNew.id_skeniranja = s.id_skeniranja;
                        skeniranjeNew.kartica = s.kartica;
                        skeniranjeNew.kontakt = s.kontakt;
                        skeniranjeNew.koordinata_x = s.koordinata_x;
                        skeniranjeNew.koordinata_y = s.koordinata_y;
                        skeniranjeNew.korisnik = s.korisnik;
                        skeniranjeNew.procitano = s.procitano;
                        skeniranjeNew.vrijeme = s.vrijeme;
                        SkeniranjeList.add(skeniranjeNew);
                    }

                    scanServiceHandler.onDataArrived(SkeniranjeList,true,true);
                }

            }

            @Override
            public void onFailure(Call<List<Skeniranje>> call, Throwable t) {

            }
        });


    }

    /********/

    public void DownloadDataForNotification(String korisnikId){

        SkeniranjaService api = retrofit.create(SkeniranjaService.class);

        Call<List<Skeniranje>> call = api.GetSkeniranje("https://airprojekt.000webhostapp.com/services.php?skeniranja_korID="+korisnikId);

        call.enqueue(new Callback<List<Skeniranje>>() {
            @Override
            public void onResponse(Call<List<Skeniranje>> call, Response<List<Skeniranje>> response) {
                List<Skeniranje> skeniranje = response.body();
                SkeniranjeList.clear();
                if(skeniranje.get(0)==null)
                {
                    scanServiceHandler.onDataArrived(SkeniranjeList,true,false);
                }
                else {
                    for (Skeniranje s : skeniranje) {
                        Skeniranje skeniranjeNew = new Skeniranje();
                        skeniranjeNew.datum = s.datum;
                        skeniranjeNew.id_skeniranja = s.id_skeniranja;
                        skeniranjeNew.kartica = s.kartica;
                        skeniranjeNew.kontakt = s.kontakt;
                        skeniranjeNew.koordinata_x = s.koordinata_x;
                        skeniranjeNew.koordinata_y = s.koordinata_y;
                        skeniranjeNew.korisnik = s.korisnik;
                        skeniranjeNew.procitano = s.procitano;
                        skeniranjeNew.vrijeme = s.vrijeme;
                        SkeniranjeList.add(skeniranjeNew);
                    }

                    scanServiceHandler.onDataArrived(SkeniranjeList,true,false);
                }

            }

            @Override
            public void onFailure(Call<List<Skeniranje>> call, Throwable t) {

            }
        });


    }

}
