package Retrofit.DataGet;

import java.util.ArrayList;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.WebServiceHandler;
import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;
import Retrofit.RemoteGet.KorisniciService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KorisnikData {

    WebServiceHandler userServiceHandler;

    Retrofit retrofit;

    final static List<Korisnik> KorisnikList=new ArrayList<Korisnik>();

    public KorisnikData(WebServiceHandler userServiceHandler)
    {
        this.userServiceHandler=userServiceHandler;
        OkHttpClient client = new OkHttpClient();

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    public void DownloadByUserId(String korisnikId){

        KorisniciService api = retrofit.create(KorisniciService.class);

        Call<List<Korisnik>> call = api.GetKorisnik("https://airprojekt.000webhostapp.com/services.php?korisnikID="+korisnikId);

        call.enqueue(new Callback<List<Korisnik>>() {

            @Override
            public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {
                List<Korisnik> korisnik = response.body();
                KorisnikList.clear();
                if(korisnik.get(0)==null)
                {
                    userServiceHandler.onDataArrived(KorisnikList,true);
                }
                else {
                    for (Korisnik k : korisnik) {
                        Korisnik korisnikNew = new Korisnik();
                        korisnikNew.prezime = k.prezime;
                        korisnikNew.adresa = k.adresa;
                        korisnikNew.broj_mobitela = k.broj_mobitela;
                        korisnikNew.broj_telefona = k.broj_telefona;
                        korisnikNew.email = k.email;
                        korisnikNew.ime = k.ime;
                        korisnikNew.id = k.id;
                        korisnikNew.korisnicko_ime = k.korisnicko_ime;
                        korisnikNew.url_profilna = k.url_profilna;
                        KorisnikList.add(korisnikNew);
                    }

                    userServiceHandler.onDataArrived(KorisnikList,true);
                }

            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {

            }
        });



    }
}
