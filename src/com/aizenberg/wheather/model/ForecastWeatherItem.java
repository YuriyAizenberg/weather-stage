package com.aizenberg.wheather.model;

import com.aizenberg.wheather.utils.NullExcludeList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */

/**
 * Reverse chronology compare
 */
public class ForecastWeatherItem extends AbsDataModel implements Comparable<ForecastWeatherItem> {

    private float tempMin;
    private float tempMax;
    private float tempDay;
    private float tempNight;
    private float tempMorn;
    private float tempEve;


    private float pressure;
    private float humidity;
    private float windSpeed;
    private float windDeg;
    private long timestamp;
    private int allCloud; //???



    private List<WeatherDetail> weatherDetails = new NullExcludeList<WeatherDetail>();

    /**
     * Need to simple store in database
     *
     * @param jsonObject {@link org.json.JSONObject}
     * @throws org.json.JSONException
     */
    public ForecastWeatherItem(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        JSONObject tempJson = jsonObject.getJSONObject("temp");
        timestamp = jsonObject.getLong("dt");
        tempMin = (float) tempJson.getDouble("min");
        tempMax = (float) tempJson.getDouble("max");
        tempNight = (float) tempJson.getDouble("night");
        tempEve = (float) tempJson.getDouble("eve");
        tempMorn = (float) tempJson.getDouble("morn");
        tempDay = (float) tempJson.getDouble("day");


        pressure = (float) jsonObject.getDouble("pressure");
        humidity = (float) jsonObject.getDouble("humidity");


        JSONArray weatherArray = jsonObject.optJSONArray("weather");

        for (int i = 0; i < weatherArray.length(); i++) {
            weatherDetails.add(new WeatherDetail(weatherArray.getJSONObject(i)));
        }

        windSpeed = (float) jsonObject.getDouble("speed");
        windDeg = (float) jsonObject.getDouble("deg");

        allCloud = (int) jsonObject.getDouble("clouds");

    }


    public float getTempDay() {
        return tempDay;
    }

    public void setTempDay(float tempDay) {
        this.tempDay = tempDay;
    }

    public float getTempNight() {
        return tempNight;
    }

    public void setTempNight(float tempNight) {
        this.tempNight = tempNight;
    }

    public float getTempMorn() {
        return tempMorn;
    }

    public void setTempMorn(float tempMorn) {
        this.tempMorn = tempMorn;
    }

    public float getTempEve() {
        return tempEve;
    }

    public void setTempEve(float tempEve) {
        this.tempEve = tempEve;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(float windDeg) {
        this.windDeg = windDeg;
    }

    public int getAllCloud() {
        return allCloud;
    }

    public void setAllCloud(int allCloud) {
        this.allCloud = allCloud;
    }

    public List<WeatherDetail> getWeatherDetails() {
        return weatherDetails;
    }

    public void setWeatherDetails(List<WeatherDetail> weatherDetails) {
        this.weatherDetails = weatherDetails;
    }

    @Override
    public int compareTo(ForecastWeatherItem another) {
        return Long.valueOf(another.timestamp).compareTo(timestamp);
    }

    public static class WeatherDetail extends AbsDataModel {
        private int id;
        private String main;
        private String desciption;
        private String icon;

        /**
         * Need to simple store in database
         *
         * @param jsonObject {@link org.json.JSONObject}
         * @throws org.json.JSONException
         */
        public WeatherDetail(JSONObject jsonObject) throws JSONException {
            super(jsonObject);
            id = jsonObject.getInt("id");
            main = jsonObject.getString("main");
            desciption = jsonObject.getString("description");
            icon = jsonObject.getString("icon");
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDesciption() {
            return desciption;
        }

        public void setDesciption(String desciption) {
            this.desciption = desciption;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }


}
