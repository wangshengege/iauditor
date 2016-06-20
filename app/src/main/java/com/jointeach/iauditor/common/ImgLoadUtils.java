package com.jointeach.iauditor.common;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jointeach.iauditor.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.mylibrary.utils.LogTools;
import org.mylibrary.utils.Tools;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 作者: ws
 * 日期: 2016/4/7.
 * 介绍：
 */
public class ImgLoadUtils {
    private static final String TAG="ImgLoadUtils";
    private  ImageLoader imageLoader;
    private static ImgLoadUtils ourInstance;
    public static ImgLoadUtils getInstance() {
        if(ourInstance==null){
            ourInstance = new ImgLoadUtils();
        }
        return ourInstance;
    }

    private ImgLoadUtils() {
        imageLoader = ImageLoader.getInstance();
    }

    /**
     * 加载图片
     * @param url       String imageUri = "http://site.com/image.png"; // 网络图片
     *                  String imageUri = "file:///mnt/sdcard/image.png"; //SD卡图片
     *                  String imageUri = "content://media/external/audio/albumart/13"; // 媒体文件夹
     *                  String imageUri = "assets://image.png"; // assets
     *                  String imageUri = "drawable://" + R.drawable.image;
     * @param imageView 图片控件
     */
    public static void loadImageRes(String url, ImageView imageView,int round) {
        loadImageRes(url, imageView, getDisplayOptions(round), null);
    }

    /**
     * 加载图片
     *
     * @param url       String imageUri = "http://site.com/image.png"; // 网络图片
     *                  String imageUri = "file:///mnt/sdcard/image.png"; //SD卡图片
     *                  String imageUri = "content://media/external/audio/albumart/13"; // 媒体文件夹
     *                  String imageUri = "assets://image.png"; // assets
     *                  String imageUri = "drawable://" + R.drawable.image;
     * @param imageView 图片控件
     */
    public static void loadImageRes(String url, ImageView imageView) {
        loadImageRes(url, imageView, null, null);
    }

    /**
     * 加载图片
     *
     * @param url       String imageUri = "http://site.com/image.png"; // 网络图片
     *                  String imageUri = "file:///mnt/sdcard/image.png"; //SD卡图片
     *                  String imageUri = "content://media/external/audio/albumart/13"; // 媒体文件夹
     *                  String imageUri = "assets://image.png"; // assets
     *                  String imageUri = "drawable://" + R.drawable.image;
     * @param imageView 图片控件
     */
    public static void loadImageRes(String url, CircleImageView imageView) {
        DisplayImageOptions options= new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.NONE)
                .showStubImage(R.drawable.page5)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        loadImageRes(url, imageView, options, null);
    }

    /**
     * 加载图片
     *
     * @param url       String imageUri = "http://site.com/image.png"; // 网络图片
     *                  String imageUri = "file:///mnt/sdcard/image.png"; //SD卡图片
     *                  String imageUri = "content://media/external/audio/albumart/13"; // 媒体文件夹
     *                  String imageUri = "assets://image.png"; // assets
     *                  String imageUri = "drawable://" + R.drawable.image;
     * @param imageView 图片控件
     * @param options 显示设置
     * @param listener 加载监听
     */
    public static void loadImageRes(String url, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        LogTools.i(TAG, "loadImageRes:" + url);
        if(Tools.isEmpty(url)){
            url="drawable://"+ R.drawable.ic_launcher;
        }
        if(url.startsWith("www")){
            url="http://"+url;
        }
        if(!url.startsWith("file://") && !url.startsWith("drawable://") && !url.startsWith("http://")){
            url="file://"+url;
        }
        getInstance().imageLoader.displayImage(url, imageView, options, listener);
    }
    public static ImageLoader getImageLoader(){
        return getInstance().imageLoader;
    }
    /**
     * 获取显示设置类
     * @param round 圆角
     * */
    public static DisplayImageOptions getDisplayOptions(int round){
        DisplayImageOptions options= new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.NONE)
                .showStubImage(R.drawable.page5)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(round))  // 设置成圆角图片
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }
}
