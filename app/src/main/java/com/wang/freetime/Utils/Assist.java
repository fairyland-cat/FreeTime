package com.wang.freetime.Utils;

import com.wang.freetime.model.Photo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * FreeTime
 * Created by wang on 2017.6.10.
 */

public class Assist {

    private Assist(){}
    private static final Assist assist=new Assist();
    public static Assist getAssist(){
        return assist;
    }

    public void getBoon_Photo(final ResponseListener listener, int page,String type){

        /**
         * Created by wang on 2017.6.11
         * 请求干货集中营的福利图片
         */
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetApi net=retrofit.create(NetApi.class);
        Call<Photo> photo=net.getboon(type,"10",page);
        photo.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()){
                    listener.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                    listener.onFail();
            }
        });

    }


    public interface ResponseListener {

        void onSuccess(Photo pic);
        void onFail();
    }

}
