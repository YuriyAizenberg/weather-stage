package com.aizenberg.wheather.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.aizenberg.wheather.model.Forecast;
import com.aizenberg.wheather.model.ForecastWeatherItem;
import com.aizenberg.wheather.model.WeatherItem;
import com.aizenberg.wheather.utils.Logger;
import com.aizenberg.wheather.utils.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Юрий on 05.07.14.
 */
public class DatabaseUtils {

    public static final String WEATHER_JSON = "weather";
    public static final String FORECAST_JSON = "forecast";
    public static final String W_ID = "id";
    private static Logger logger = LoggerFactory.getLogger(DatabaseUtils.class.getSimpleName());

    public enum Tables {
        WEATHER,
        FORECAST
    }


    public static ContentValues getCV(WeatherItem weatherItem) {
        ContentValues cv = new ContentValues(2);
        cv.put(WEATHER_JSON, weatherItem.getJsonRepresentation());
        cv.put(W_ID, weatherItem.getCityId());
        return cv;
    }

    public static WeatherItem parseWeather(Cursor cursor) {
        try {
            return new WeatherItem(new JSONObject(cursor.getString(cursor.getColumnIndex(WEATHER_JSON))));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static ContentValues getCV(Forecast weatherItem) {
        ContentValues cv = new ContentValues(2);
        cv.put(FORECAST_JSON, weatherItem.getJsonRepresentation());
        cv.put(W_ID, weatherItem.getCityId());
        return cv;
    }

    public static Forecast parseForecast(Cursor cursor) {
        try {
            return new Forecast(new JSONObject(cursor.getString(cursor.getColumnIndex(FORECAST_JSON))));
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }



}
