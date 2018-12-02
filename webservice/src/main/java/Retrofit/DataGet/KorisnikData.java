package Retrofit.DataGet;

import java.util.ArrayList;
import java.util.List;

import Retrofit.Model.Korisnik;
import Retrofit.Model.Ljubimac;
import Retrofit.RemoteGet.KorisniciService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KorisnikData {
    final static List<Korisnik> KorisnikList=new ArrayList<Korisnik>();
    public void Download(String korisnikId, final MyCallback<List<Korisnik>> callback){

        Retrofit retrofit;
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KorisniciService api = retrofit.create(KorisniciService.class);

        Call<List<Korisnik>> call = api.GetKorisnik("https://airprojekt.000webhostapp.com/services.php?korisnici_korID="+korisnikId);

        call.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {
                List<Korisnik> korisnik = response.body();
                KorisnikList.clear();
                if(korisnik.get(0)==null)
                {
                    callback.next(KorisnikList);
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

                    callback.next(KorisnikList);
                }

            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {

            }
        });



    }
}
