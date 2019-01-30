package Retrofit.DataGet;

import java.util.ArrayList;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Kartica;
import Retrofit.RemoteGet.KarticaService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KarticaData {

    WebServiceHandler cardServiceHandler;

    Retrofit retrofit;

    public final static List<Kartica> KarticaList=new ArrayList<Kartica>();

    public KarticaData(WebServiceHandler cardServiceHandler) {

        this.cardServiceHandler=cardServiceHandler;

        OkHttpClient client = new OkHttpClient();

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * dohvaćanje kartica po identifikaciji korisnika
     * @param korisnikId
     */
    public void DownloadByUserId(String korisnikId){

        KarticaService api = retrofit.create(KarticaService.class);

        Call<List<Kartica>> call = api.GetKartica("https://airprojekt.000webhostapp.com/services.php?kartice_korID="+korisnikId);

        call.enqueue(new Callback<List<Kartica>>() {
            @Override
            public void onResponse(Call<List<Kartica>> call, Response<List<Kartica>> response) {
                List<Kartica> kartica = response.body();
                KarticaList.clear();
                if(kartica.get(0)==null)
                {
                   cardServiceHandler.onDataArrived(KarticaList,true,true);
                }
               else {
                    for (Kartica k : kartica) {
                        Kartica karticaNew = new Kartica();
                        karticaNew.korisnik = k.korisnik;
                        karticaNew.id_kartice = k.id_kartice;
                        KarticaList.add(karticaNew);
                    }

                    cardServiceHandler.onDataArrived(KarticaList,true,true);
                }

            }

            @Override
            public void onFailure(Call<List<Kartica>> call, Throwable t) {

            }
        });

    }

}
