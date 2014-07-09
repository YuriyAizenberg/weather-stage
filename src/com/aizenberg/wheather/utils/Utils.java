package com.aizenberg.wheather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.TheApplication;

/**
 * Created by Юрий on 05.07.14.
 */
public class Utils {

    public static double mLat;
    public static double mLng;

    public static String getStringFromResources(int resId) {
        return TheApplication.getInstance().getResources().getString(resId);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null;
    }

    public static int getTempResource(float temperature) {
        if (temperature < -20) {
            return R.drawable.t_xl;
        } else if (temperature >= -20 && temperature <= 10) {
            return R.drawable.t_l;
        } else if (temperature > 10 && temperature < 20) {
            return R.drawable.t_m;
        } else if (temperature >= 20 && temperature < 30) {
            return R.drawable.t_h;
        }
        return R.drawable.t_xh;
    }

}
