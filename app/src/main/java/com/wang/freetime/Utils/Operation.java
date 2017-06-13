package com.wang.freetime.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * FreeTime
 * Created by wang on 2017.6.10.
 */

public class Operation {
    private Context mContext;
    private MyListening myListening;
    public Operation(Context context) {
        mContext = context;
    }

    public int getWidth(){
        DisplayMetrics dm =mContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    //Glide保存图片
    public void savePicture(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL m_url=new URL(url);
                    InputStream ism=m_url.openStream();
                    Bitmap btm= BitmapFactory.decodeStream(ism);
                    savaFileToSD(String.valueOf(url.hashCode()),btm);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //往SD卡写入文件的方法
    public void savaFileToSD(String filename, Bitmap bitmap) throws IOException {
        File filePath =new File(mContext.getExternalCacheDir(),"photo") ;
        if (!filePath.exists()){
            filePath.mkdirs();
        }
        String photo_path=filePath.getPath()+File.separator+filename+".jpg";
        Logger.d(photo_path);
        FileOutputStream output = new FileOutputStream(photo_path);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
        output.flush();
        output.close();
        handler.sendEmptyMessage(1);
        }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
                myListening.downOver();
                Logger.d("下载完成");
                return true;
        }
    });


    public void setMyListening(MyListening myListening) {
        this.myListening = myListening;
    }

    public interface MyListening{
        void downOver();
    }

    /**
     * Created by wang on 2017.6.12
     * 获取本地图片并转换成bitmap
     */
    public Bitmap decodeBitmap(String path){
        BitmapFactory.Options op = new BitmapFactory.Options();
        //inJustDecodeBounds
        //If set to true, the decoder will return null (no bitmap), but the out…
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = op.outWidth/(getWidth()/4);
        int hRatio = op.outHeight/(getWidth()/4);
        //如果超出指定大小，则缩小相应的比例
        if(wRatio > 1 && hRatio > 1){
            if(wRatio > hRatio){
                op.inSampleSize = wRatio;
            }else{
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }

}
