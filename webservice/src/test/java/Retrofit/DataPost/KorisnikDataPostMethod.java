package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.RemotePost.KorisnikService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KorisnikDataPostMethod {
    public String Upload() {

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


        //primjer za dodavanje novog korisnika

        KorisnikDataRequest korisnikRequest = new KorisnikDataRequest();

        KorisnikService api = retrofit.create(KorisnikService.class);

        KorisnikDataRequest request = new KorisnikDataRequest();

        request.setAdresa("adresa");

        request.setBroj_mobitela("091111222");

        request.setEmail("email");

        request.setIme("ime");

        request.setPrezime("Prezime");

        request.setKorisnicko_ime("KorIme");

        request.setBroj_telefona("000212");

        request.setUrl_profilna("UrlProfilna");

        request.setLozinka("1234");

     /*   Call<KorisnikDataResponse> KorisnikResponseCall = api.createKorisnik(request,"https://airprojekt.000webhostapp.com/registracija.php");

        KorisnikResponseCall.enqueue(new Callback<KorisnikDataResponse>() {
            @Override
            public void onResponse(Call<KorisnikDataResponse> call, Response<KorisnikDataResponse> response) {

                KorisnikDataResponse body = response.body();
                Body[0]=body.KorisnikID;

            }

            @Override
            public void onFailure(Call<KorisnikDataResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

*/
        return Body[0];

    }



}

