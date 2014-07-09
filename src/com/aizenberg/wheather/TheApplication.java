package com.aizenberg.wheather;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import com.aizenberg.wheather.utils.LocationHelper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */
public class TheApplication extends Application {
    private static TheApplication application;
    private SharedPreferences mSharedPreferences;
    private ImageLoaderConfiguration config;
    public static TheApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initImageLoader();
        if (Preferences.getCities().isEmpty()) {
            List<Long> defCities = new ArrayList<Long>() {{
                add(524901L);
                add(498817L);
            }};
            Preferences.saveCities(defCities);
        }
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .discCache(new UnlimitedDiscCache(getCacheDir())) // default
                .discCacheSize(75 * 480 * 800)
                .discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .defaultDisplayImageOptions(defaultOptions) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        LocationHelper.initialize(this);
    }

    public SharedPreferences getPreferences() {
        if (mSharedPreferences != null) {
            return mSharedPreferences;
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return mSharedPreferences;
    }
}
