package com.aizenberg.wheather.model;

import com.aizenberg.wheather.utils.NullExcludeList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */
public class WeatherItem extends AbsDataModel {

    public static final String BUNDLE_KEY = AbsDataModel.class.getSimpleName();

    private float temp;
    private float tempMin;
    private float tempMax;
    private float pressure;
    private float seaLevel;
    private float grndLevel;
    private float humidity;
    private float windSpeed;
    private float windDeg;
    private int allCloud; //???
    private String country;
    private long sunrise;
    private long sunset;
    private double lat = NO_COORD;
    private double lng = NO_COORD;
    private long timestamp;
    private String name;
    private long cityId;


    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    private List<WeatherDetail> weatherDetails = new NullExcludeList<WeatherDetail>();

    /**
     * Need to simple store in database
     *
     * @param jsonObject {@link org.json.JSONObject}
     * @throws org.json.JSONException
     */
    public WeatherItem(JSONObject jsonObject) throws JSONException {
        super(jsonObject);

        JSONObject sysJson = jsonObject.getJSONObject("sys");
        country = sysJson.getString("country");
        sunrise = sysJson.getLong("sunrise");
        sunset = sysJson.getLong("sunset");

        timestamp = jsonObject.getLong("dt");

        name = jsonObject.getString("name");
        cityId = jsonObject.getLong("id");

        JSONObject coord = jsonObject.optJSONObject("coord");
        if (coord != null) {
            lat = coord.getDouble("lat");
            lng = coord.getDouble("lon");

        }

        JSONObject mainJson = jsonObject.getJSONObject("main");

        temp = (float) mainJson.optDouble("temp");
        tempMin = (float) mainJson.optDouble("temp_min");
        tempMax = (float) mainJson.optDouble("temp_max");
        pressure = (float) mainJson.optDouble("pressure");
        seaLevel = (float) mainJson.optDouble("sea_level");
        grndLevel = (float) mainJson.optDouble("grnd_level");
        humidity = (float) mainJson.getDouble("humidity");
        JSONArray weatherArray = jsonObject.optJSONArray("weather");

        for (int i = 0; i < weatherArray.length(); i++) {
            weatherDetails.add(new WeatherDetail(weatherArray.getJSONObject(i)));
        }

        JSONObject windJSON = jsonObject.optJSONObject("wind");
        if (windJSON != null) {
            windSpeed = (float) windJSON.optDouble("speed");
            windDeg = (float) windJSON.optDouble("deg");
        }

        JSONObject clouds = jsonObject.optJSONObject("clouds");
        if (clouds != null) {
            allCloud = clouds.optInt("all");
        }

    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
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

    public float getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(float seaLevel) {
        this.seaLevel = seaLevel;
    }

    public float getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(float grndLevel) {
        this.grndLevel = grndLevel;
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
