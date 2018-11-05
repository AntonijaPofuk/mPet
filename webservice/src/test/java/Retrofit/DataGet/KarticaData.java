package Retrofit.DataGet;

import java.util.ArrayList;
import java.util.List;

import hr.com.webservice.Retrofit.Model.Kartica;
import hr.com.webservice.Retrofit.RemoteGet.KarticaService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KarticaData {
    public final static List<Kartica> KarticaList=new ArrayList<Kartica>();
    public List<Kartica> Download(String korisnikId){

        Retrofit retrofit;
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KarticaService api = retrofit.create(KarticaService.class);

        Call<List<Kartica>> call = api.GetKartica("https://airprojekt.000webhostapp.com/services.php?kartice_korID="+korisnikId);

        call.enqueue(new Callback<List<Kartica>>() {
            @Override
            public void onResponse(Call<List<Kartica>> call, Response<List<Kartica>> response) {
                KarticaList.clear();
                List<Kartica> kartica = response.body();
                for (Kartica k:kartica) {
                    Kartica karticaNew=new Kartica();
                    karticaNew.korisnik=k.korisnik;
                    karticaNew.id_kartice=k.id_kartice;
                    KarticaList.add(karticaNew);
                }

            }

            @Override
            public void onFailure(Call<List<Kartica>> call, Throwable t) {

            }
        });

        return KarticaList;

    }

}
