package com.wang.freetime.Utils;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.wang.freetime.model.Photo;
import com.wang.freetime.model.Save_Love;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
    private static is_Save save;
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

    //启动拍照
    public static void startCamera(Fragment fragment){
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+
                File.separator+"user_icon.jpg");
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data",false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ComponentName componentName=intent.resolveActivity(fragment.getContext().getPackageManager());
        if (componentName!=null){
            fragment.startActivityForResult(intent,Variable.request_camera_code);
        }
    }
    //启动相册
    public static void startPhoto(Fragment fragment){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        ComponentName componentName = intent.resolveActivity(fragment.getContext().getPackageManager());
        Log.d("tag", "startPhoto: "+componentName);
        if (componentName != null) {
            fragment.startActivityForResult(intent, Variable.request_photo);
        }
    }
    //裁剪图片
    public static void cropphoto(Fragment fragment, Uri uri){
        File bomb=new File(fragment.getContext().getExternalCacheDir(),"bmob");
        Log.d("tag", "cropphoto: "+bomb);
        if (!bomb.exists()){
            bomb.mkdir();
        }
        File file=new File(bomb,"user_icon.jpg");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ComponentName componentName = intent.resolveActivity(fragment.getContext().getPackageManager());
        Log.d("TAG", "cropphoto: "+componentName);
        if (componentName!=null){
            fragment.startActivityForResult(intent,Variable.request_crop);
        }
    }

    public static void isSave(String url){
        BmobQuery<Save_Love> query=new BmobQuery<>();
        query.addWhereEqualTo("url",url);
        query.findObjects(new FindListener<Save_Love>() {
            @Override
            public void done(List<Save_Love> list, BmobException e) {
                if (e==null){
                    if (list.size()!=0){
                        save.issave(list.get(0).getObjectId());
                    }else {
                        save.unsave();
                    }
                }
            }
        });
    }

    public interface is_Save{
        void issave(String id);
        void unsave();
    }

    public void setSave(is_Save save) {
        this.save = save;
    }

    public interface ResponseListener {

        void onSuccess(Photo pic);
        void onFail();
    }

}
