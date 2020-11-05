package com.example.bodyrehab.network;

import com.example.bodyrehab.models.ModelHome;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class YoutubeAPI {

    public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    public static final String KEY = "key=AIzaSyBh0sc10wmvbOTYcc5GmJs7rnS25Hjw8sM";
    //public static final String KEY = "key=AIzaSyDjyTkPTntAn0EB-rv_Jiwmobrni_D4pwU";
    //public static final String KEY = "key=AIzaSyCS8M4N08wGt_LVvZNH1izvYUYCtsy1yHs";
    //public static final String KEY = "key=AIzaSyD4quaR9yPTdT1Q20HysdcXctq_iNbWmK4";
    public static final String SCH = "search?";
    public static final String CHID = "&channelId=UCR7z7IuoLx2Zp83w-ZjqClw";
    public static final String MX = "&maxResults=50";
    public static final String ORD = "&order=relevance";
    public static final String PART = "&part=snippet";
    public static final String QUERY = "&q=";
    public static final String TYPE = "&type=video";

    public interface HomeVideo{
        @GET
        Call<ModelHome> getYT(@Url String url);
    }

    private static HomeVideo homeVideo =  null;

    public static HomeVideo getHomeVideo(){
        if(homeVideo == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            homeVideo = retrofit.create(HomeVideo.class);
        }
        return homeVideo;
    }

}
