package Retrofit.DataPost;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.RemotePost.KorisnikPrijavaServis;
import Retrofit.RemotePost.onLoginValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import Retrofit.RemotePost.KorisnikService;



public class PrijavaMethod {

    private static onLoginValidation onLoginValidationinstance;

    public PrijavaMethod(onLoginValidation onLoginValidationi) {
        this.onLoginValidationinstance=onLoginValidationi;
    }



    public static void Upload(String korIme, String lozinka) {

        final PrijavaResponse Body = new PrijavaResponse();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit;
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://airprojekt.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();




        KorisnikPrijavaServis api = retrofit.create(KorisnikPrijavaServis.class);

        PrijavaRequest request = new PrijavaRequest();


        request.setKorisnicko_ime(korIme);
        request.setLozinka(lozinka);

         Call<PrijavaResponse> KorisnikResponseCall = api.createKorisnik(request,"https://airprojekt.000webhostapp.com/prijava.php");

        KorisnikResponseCall.enqueue(new Callback<PrijavaResponse>() {
            @Override
            public void onResponse(Call<PrijavaResponse> call, Response<PrijavaResponse> response) {
                PrijavaResponse resp=response.body();

                Body.id= resp.id;

                onLoginValidationinstance.onDataLoaded(Body.id);

            }

            @Override
            public void onFailure(Call<PrijavaResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });




    }
}
