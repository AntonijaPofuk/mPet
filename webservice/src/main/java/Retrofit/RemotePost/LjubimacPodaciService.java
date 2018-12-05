package Retrofit.RemotePost;

import Retrofit.DataPost.LjubimacPodaciRequest;
import Retrofit.DataPost.LjubimacPodaciResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface LjubimacPodaciService {
    @POST
    Call<LjubimacPodaciResponse> createLjubimac(@Body LjubimacPodaciRequest ljubimacBody, @Url String url);
}
