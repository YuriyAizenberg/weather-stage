package com.aizenberg.wheather.utils;

import com.aizenberg.wheather.model.AbsDataModel;
import com.aizenberg.wheather.model.Forecast;
import com.aizenberg.wheather.model.WeatherItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuriy
 * 7/9/14
 * <p/>
 * Provide access to concurrency "cache" {@link #forecastCache}
 */
@SuppressWarnings("unchecked")
public class CacheLayer {

    private static final Map<Long, Forecast> forecastCache = new ConcurrentHashMap<Long, Forecast>();
    private static final Map<Long, WeatherItem> weatherCache = new ConcurrentHashMap<Long, WeatherItem>();

    public static synchronized WeatherItem getFromCacheWeather(Long id) {
        WeatherItem absDataModel = weatherCache.get(id);
        if (absDataModel != null && absDataModel.isExpired()) {
            weatherCache.remove(absDataModel);
            return null;
        }
        return absDataModel;
    }

    public static synchronized Forecast getFromCacheForecast(Long id) {
        Forecast absDataModel = forecastCache.get(id);
        if (absDataModel != null && absDataModel.isExpired()) {
            forecastCache.remove(absDataModel);
            return null;
        }
        return absDataModel;
    }

    public static synchronized void addToCache(AbsDataModel model, long id) {
        if (model != null && !model.isExpired()) {
            if (model instanceof Forecast) {
                forecastCache.put(id, (Forecast) model);
            } else if (model instanceof WeatherItem) {
                weatherCache.put(id, (WeatherItem) model);
            }
        }
    }


}
