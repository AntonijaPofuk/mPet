package Retrofit.DataGet;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Ljubimac;
import Retrofit.RemoteGet.LjubimciService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class LjubimacData extends AppCompatActivity  {

    WebServiceHandler petServiceHandler;

    Retrofit retrofit;

    public LjubimacData(WebServiceHandler airWebServiceHandler){
        this.petServiceHandler = airWebServiceHandler;

        OkHttpClient client = new OkHttpClient();

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    final static List<Ljubimac> LjubimacList=new ArrayList<Ljubimac>();

    public void DownloadByTag(String tagId){

        LjubimciService api = retrofit.create(LjubimciService.class);

        Call<List<Ljubimac>> call = api.GetLjubimac("https://airprojekt.000webhostapp.com/skeniranje.php?kod="+tagId);

        call.enqueue(new Callback<List<Ljubimac>>() {
            @Override
            public void onResponse(Call<List<Ljubimac>> call, Response<List<Ljubimac>> response) {

                List<Ljubimac> ljubimac = response.body();

                LjubimacList.clear();

                if(ljubimac.get(0) == null)
                {
                    petServiceHandler.onDataArrived(LjubimacList, true);
                }
                else {
                    for (Ljubimac l : ljubimac) {
                        Ljubimac ljubimacNew = new Ljubimac();
                        ljubimacNew.godina = l.godina;
                        ljubimacNew.id = l.id;
                        ljubimacNew.ime = l.ime;
                        ljubimacNew.kartica = l.kartica;
                        ljubimacNew.masa = l.masa;
                        ljubimacNew.opis = l.opis;
                        ljubimacNew.url_slike = l.url_slike;
                        ljubimacNew.spol = l.spol;
                        ljubimacNew.vrsta = l.vrsta;
                        ljubimacNew.vlasnik=l.vlasnik;
                        LjubimacList.add(ljubimacNew);
                    }
                    petServiceHandler.onDataArrived(LjubimacList,true);
                }

            }

            @Override
            public void onFailure(Call<List<Ljubimac>> call, Throwable t) {

            }
        });



    }

    public void DownloadByUserId(String userId){

        LjubimciService api = retrofit.create(LjubimciService.class);

        Call<List<Ljubimac>> call = api.GetLjubimac("https://airprojekt.000webhostapp.com/services.php?ljubimci_korID="+userId);

        call.enqueue(new Callback<List<Ljubimac>>() {
            @Override
            public void onResponse(Call<List<Ljubimac>> call, Response<List<Ljubimac>> response) {

                List<Ljubimac> ljubimac = response.body();

                LjubimacList.clear();

                if(ljubimac.get(0) == null)
                {
                    petServiceHandler.onDataArrived(LjubimacList, true);
                }
                else {
                    for (Ljubimac l : ljubimac) {
                        Ljubimac ljubimacNew = new Ljubimac();
                        ljubimacNew.godina = l.godina;
                        ljubimacNew.id = l.id;
                        ljubimacNew.ime = l.ime;
                        ljubimacNew.kartica = l.kartica;
                        ljubimacNew.masa = l.masa;
                        ljubimacNew.opis = l.opis;
                        ljubimacNew.url_slike = l.url_slike;
                        ljubimacNew.spol = l.spol;
                        ljubimacNew.vrsta = l.vrsta;
                        ljubimacNew.vlasnik=l.vlasnik;
                        LjubimacList.add(ljubimacNew);
                    }
                    petServiceHandler.onDataArrived(LjubimacList,true);
                }

            }

            @Override
            public void onFailure(Call<List<Ljubimac>> call, Throwable t) {

            }
        });



    }

}
