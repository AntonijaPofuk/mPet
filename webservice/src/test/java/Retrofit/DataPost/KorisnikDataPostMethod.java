package Retrofit.DataPost;

import java.util.ArrayList;
import java.util.List;

import hr.com.webservice.Retrofit.RemotePost.KorisnikService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KorisnikDataPostMethod {
    final static List<KorisnikDataResponse> ResponseList=new ArrayList<KorisnikDataResponse>();
    public List<KorisnikDataResponse> Download(String korisnikId) {

        Retrofit retrofit;
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
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

        Call<KorisnikDataResponse> KorisnikResponseCall = api.createKorisnik(request,"https://airprojekt.000webhostapp.com/services.php?korisnici_korID="+korisnikId);

        KorisnikResponseCall.enqueue(new Callback<KorisnikDataResponse>() {
            @Override
            public void onResponse(Call<KorisnikDataResponse> call, Response<KorisnikDataResponse> response) {
                ResponseList.clear();

                int statusCode = response.code();

                KorisnikDataResponse KorisnikResponse = response.body();

                ResponseList.add(KorisnikResponse);
            }

            @Override
            public void onFailure(Call<KorisnikDataResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return ResponseList;

    }



}

