package com.jointeach.iauditor.common;

import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.jointeach.iauditor.MainActivity;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.mylibrary.biz.CrashHandler;
import org.mylibrary.biz.MyApplication;

import java.io.File;

/**
 * 作者: ws
 * 日期: 2016/5/25.
 * 介绍：
 */
public class JKApplication extends MyApplication {
    private static JKApplication ourInstance;
    public LocationService locationService;
    public static JKApplication getInstance() {
        if(ourInstance==null){
            ourInstance=new JKApplication();
        }
        return ourInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ctx=getApplicationContext();
        //CrashHandler.getInstance().init(ctx, MainActivity.class);
        CrashHandler.getInstance().init(ctx);
        initImageLoader();
        locationService = new LocationService(ctx);
        SDKInitializer.initialize(ctx);
    }
    private void initImageLoader(){
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "safecoo/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(2)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                // You can pass your own memory cache implementation/
                .memoryCache(new WeakMemoryCache()).memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                // .discCacheFileNameGenerator(new Md5FileNameGenerator())//
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))
                .writeDebugLogs() // Remove for release app
                .build();

        // Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(config);
    }
}
