package com.wang.freetime.Utils;

import com.wang.freetime.model.Photo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * FreeTime
 * Created by wang on 2017.6.11.
 */

public interface NetApi {
    @Headers("User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    @GET("api/data/福利/{number}/{page}")
    Call<Photo> getboon(@Path("number") String number,@Path("page") int page);
}
