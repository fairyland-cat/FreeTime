package com.wang.freetime.Utils;

/**
 * FreeTime
 * Created by wang on 2017.6.10.
 */

public  class Variable {
    /**
     * Created by wang on 2017.6.10
     * 详情页类型
     */
    public static final int content_handwork=0x01;//纸艺详情页
    public static final int content_photo=0x02;//图片详情页
    public static final int content_video=0x03;//视频详情页
    public static final int content_user=0x04;//个人详情页

    public static final int request_code=0x00;//读写权限的请求码
    public static final int request_camera=0x06;//照相机权限请求码

    public static final int request_photo=0x07;//相册请求码
    public static final int request_camera_code=0x08;//照相机启动请求码
    public static final int request_crop=0x09;//裁剪请求码

    public static final int request_login=0x1;//福利页面跳转到登陆页面请求码


    public static final int request_account=0x05;
    public static final int result_true=0;
    public static final int result_false=1;
}
