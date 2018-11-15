package Retrofit.DataGet;

import java.util.ArrayList;
import java.util.List;

import hr.com.webservice.Retrofit.Model.Ljubimac;
import hr.com.webservice.Retrofit.RemoteGet.LjubimciService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LjubimacData {
    final static List<Ljubimac> LjubimacList=new ArrayList<Ljubimac>();
    public List<Ljubimac> Download(String korisnikId){

        Retrofit retrofit;
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LjubimciService api = retrofit.create(LjubimciService.class);

        Call<List<Ljubimac>> call = api.GetLjubimac("https://airprojekt.000webhostapp.com/");

        call.enqueue(new Callback<List<Ljubimac>>() {
            @Override
            public void onResponse(Call<List<Ljubimac>> call, Response<List<Ljubimac>> response) {
                List<Ljubimac> ljubimac = response.body();
                LjubimacList.clear();
                for (Ljubimac l:ljubimac) {
                    Ljubimac ljubimacNew=new Ljubimac();
                    ljubimacNew.godina=l.godina;
                    ljubimacNew.id_ljubimca=l.id_ljubimca;
                    ljubimacNew.ime=l.ime;
                    ljubimacNew.kartica=l.kartica;
                    ljubimacNew.masa=l.masa;
                    ljubimacNew.opis=l.opis;
                    ljubimacNew.url_slike=l.url_slike;
                    ljubimacNew.spol=l.spol;
                    ljubimacNew.vrsta=l.vrsta;
                    LjubimacList.add(ljubimacNew);
                }

            }

            @Override
            public void onFailure(Call<List<Ljubimac>> call, Throwable t) {

            }
        });

        return LjubimacList;

    }
}
