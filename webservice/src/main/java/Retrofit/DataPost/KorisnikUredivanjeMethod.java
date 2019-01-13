package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.RemotePost.KorisnikService;
import Retrofit.RemotePost.KorisnikUredivanjeService;
import Retrofit.RemotePost.StatusListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KorisnikUredivanjeMethod {

    public static StatusListener listener;

    public KorisnikUredivanjeMethod(StatusListener statusListener){
        this.listener=statusListener;
    }

    public static void Upload(String ime, String prezime, String korIme, String adresa, String email, String mobitel, String telefon, String lozinka) {

        final KorisnikUredivanjeResponse body = new KorisnikUredivanjeResponse();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit;
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();




        KorisnikUredivanjeService api = retrofit.create(KorisnikUredivanjeService.class);

        KorisnikUredivanjeRequest request = new KorisnikUredivanjeRequest();

        request.setAdresa(adresa);

        request.setBroj_mobitela(mobitel);

        request.setEmail(email);

        request.setIme(ime);

        request.setPrezime(prezime);

        request.setKorisnicko_ime(korIme);

        request.setBroj_telefona(telefon);

        request.setUrl_profilna("/");

        request.setLozinka(lozinka);

        //request.setSlika(slika);
//TODO:createkorisnik
        Call<KorisnikUredivanjeResponse> KorisnikResponseCall = api.updateKorisnik(request,"https://airprojekt.000webhostapp.com/updateKorisnik.php");

       KorisnikResponseCall.enqueue(new Callback<KorisnikUredivanjeResponse>() {
            @Override
            public void onResponse(Call<KorisnikUredivanjeResponse> call, Response<KorisnikUredivanjeResponse> response) {
                KorisnikUredivanjeResponse resp=response.body();
                body.id= resp.id;
                listener.onStatusChanged(body.id);

            }

            @Override
            public void onFailure(Call<KorisnikUredivanjeResponse> call, Throwable t) {
                t.printStackTrace();
            }


        });

    }

}

