package com.aizenberg.wheather.async;

import com.aizenberg.wheather.TheApplication;
import com.aizenberg.wheather.async.modules.ForecastModule;
import com.aizenberg.wheather.async.modules.WeatherModule;
import com.aizenberg.wheather.dao.DatabaseGateway;
import com.aizenberg.wheather.model.Forecast;
import com.aizenberg.wheather.model.ServiceResponseObject;
import com.aizenberg.wheather.model.WeatherItem;
import com.aizenberg.wheather.utils.CacheLayer;
import com.aizenberg.wheather.utils.NullExcludeList;
import com.aizenberg.wheather.utils.Utils;

import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */
public class DataManager {

    public static final String NO_INTERNET_CONNECTION = "No internet connection";

    private static boolean isNetworkConnected() {
        return Utils.isNetworkConnected(TheApplication.getInstance().getApplicationContext());
    }


    public static void getWeather(IRemoteCallback<WeatherItem> callback, final long id) {
        WeatherItem fromCache = CacheLayer.getFromCacheWeather(id);
        if (fromCache != null && callback != null) {
            callback.onSuccess(fromCache);
            return;
        }
        new AbstractWorker<WeatherItem>(callback) {
            @Override
            protected ServiceResponseObject<WeatherItem> doAction() throws Throwable {
                if (isNetworkConnected()) {
                    WeatherModule weatherModule = new WeatherModule(id);
                    return getWeatherItemServiceResponseObject(weatherModule);
                } else {
                    return new ServiceResponseObject<WeatherItem>(DatabaseGateway.getInstance().getWeather(id), null, null);
                }
            }
        }.execute();
    }

    public static void getWeather(IRemoteCallback<WeatherItem> callback, final String name) {
        new AbstractWorker<WeatherItem>(callback) {
            @Override
            protected ServiceResponseObject<WeatherItem> doAction() throws Throwable {
                if (isNetworkConnected()) {
                    WeatherModule weatherModule = new WeatherModule(name);
                    return getWeatherItemServiceResponseObject(weatherModule);
                } else {
                    return new ServiceResponseObject<WeatherItem>(null, NO_INTERNET_CONNECTION, null);
                }
            }
        }.execute();
    }

    public static void getWeather(IRemoteCallback<WeatherItem> callback, final double lat, final double lng) {
        new AbstractWorker<WeatherItem>(callback) {
            @Override
            protected ServiceResponseObject<WeatherItem> doAction() throws Throwable {
                if (isNetworkConnected()) {
                    WeatherModule weatherModule = new WeatherModule(lat, lng);
                    return getWeatherItemServiceResponseObject(weatherModule);
                } else {

                    return new ServiceResponseObject<WeatherItem>(null, NO_INTERNET_CONNECTION, null);
                }
            }
        }.execute();

    }

    public static void getWeatherList(IRemoteCallback<List<WeatherItem>> callback, final List<Long> cities) {
        new AbstractWorker<List<WeatherItem>>(callback) {
            @Override
            protected ServiceResponseObject<List<WeatherItem>> doAction() throws Throwable {
                List<WeatherItem> weatherItems = new NullExcludeList<WeatherItem>();
                for (long city : cities) {
                    WeatherItem fromCacheWeather = CacheLayer.getFromCacheWeather(city);
                    if (fromCacheWeather != null) {
                        weatherItems.add(fromCacheWeather);
                        continue;
                    }
                    if (isNetworkConnected()) {
                        WeatherModule module = new WeatherModule(city);
                        module.run();
                        ServiceResponseObject<WeatherItem> weatherItemServiceResponseObject = getWeatherItemServiceResponseObject(module);
                        if (weatherItemServiceResponseObject != null && weatherItemServiceResponseObject.getEntity() != null) {
                            WeatherItem entity = weatherItemServiceResponseObject.getEntity();
                            weatherItems.add(entity);
                            CacheLayer.addToCache(entity, entity.getCityId());
                        }
                    } else {
                        WeatherItem entity = DatabaseGateway.getInstance().getWeather(city);
                        weatherItems.add(entity);
                        CacheLayer.addToCache(entity, entity.getCityId());
                    }

                }
                return new ServiceResponseObject<List<WeatherItem>>(weatherItems, null, null);
            }
        }.execute();
    }


    private static ServiceResponseObject<WeatherItem> getWeatherItemServiceResponseObject(WeatherModule weatherModule) {
        weatherModule.run();
        ServiceResponseObject<WeatherItem> serviceResponseObject = weatherModule.getServiceResponseObject();
        WeatherItem entity = serviceResponseObject.getEntity();
        if (entity != null) {
            DatabaseGateway.getInstance().saveWeather(entity);
            CacheLayer.addToCache(entity, entity.getCityId());
        }
        return serviceResponseObject;
    }


    public static void getForecast(IRemoteCallback<Forecast> callback, final long id, final int cnt) {
        Forecast fromCache = CacheLayer.getFromCacheForecast(id);
        if (fromCache != null && callback != null) {
            callback.onSuccess(fromCache);
            return;
        }
        new AbstractWorker<Forecast>(callback) {
            @Override
            protected ServiceResponseObject<Forecast> doAction() throws Throwable {
                if (isNetworkConnected()) {
                    ForecastModule weatherModule = new ForecastModule(id, cnt);
                    return getForecastItemServiceResponseObject(weatherModule);
                } else {
                    return new ServiceResponseObject<Forecast>(DatabaseGateway.getInstance().getForecast(id), null, null);
                }
            }
        }.execute();
    }

    public static void getForecast(IRemoteCallback<Forecast> callback, final String name, final int cnt) {
        new AbstractWorker<Forecast>(callback) {
            @Override
            protected ServiceResponseObject<Forecast> doAction() throws Throwable {
                if (isNetworkConnected()) {
                    ForecastModule weatherModule = new ForecastModule(name, cnt);
                    return getForecastItemServiceResponseObject(weatherModule);
                } else {
                    return new ServiceResponseObject<Forecast>(null, NO_INTERNET_CONNECTION, null);
                }
            }
        }.execute();
    }

    public static void getForecast(IRemoteCallback<Forecast> callback, final double lat, final double lng, final int cnt) {
        new AbstractWorker<Forecast>(callback) {
            @Override
            protected ServiceResponseObject<Forecast> doAction() throws Throwable {
                if (!isNetworkConnected()) {
                    ForecastModule weatherModule = new ForecastModule(lat, lng, cnt);
                    return getForecastItemServiceResponseObject(weatherModule);
                } else {
                    return new ServiceResponseObject<Forecast>(null, NO_INTERNET_CONNECTION, null);
                }
            }
        }.execute();
    }


    private static ServiceResponseObject<Forecast> getForecastItemServiceResponseObject(ForecastModule forecastModule) {
        forecastModule.run();
        ServiceResponseObject<Forecast> serviceResponseObject = forecastModule.getServiceResponseObject();
        Forecast entity = serviceResponseObject.getEntity();
        if (entity != null) {
            DatabaseGateway.getInstance().saveForecast(entity);
            CacheLayer.addToCache(entity, entity.getCityId());
        }
        return serviceResponseObject;
    }


}
