package com.aizenberg.wheather.model;

import com.aizenberg.wheather.utils.NullExcludeList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Юрий on 06.07.14.
 */
public class Forecast extends AbsDataModel {

    private long cityId;
    private String cityName;
    private String country;
    private double lat;
    private double lon;

    private List<ForecastWeatherItem> forecastWeatherItems = new NullExcludeList<ForecastWeatherItem>();


    /**
     * Need to simple store in database
     *
     * @param jsonObject {@link org.json.JSONObject}
     * @throws org.json.JSONException
     */
    public Forecast(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        JSONObject city = jsonObject.optJSONObject("city");
        if (city != null) {
            cityId = city.getLong("id");
            cityName = city.getString("name");
            country = city.getString("country");
            JSONObject coord = city.optJSONObject("coord");
            if (coord != null) {
                lat = coord.getDouble("lat");
                lon = coord.getDouble("lon");
            }
        }
        JSONArray jsonArray = jsonObject.optJSONArray("list");
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                forecastWeatherItems.add(new ForecastWeatherItem(jsonArray.getJSONObject(i)));
            }
        }

    }


    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public List<ForecastWeatherItem> getForecastWeatherItems() {
        return forecastWeatherItems;
    }

    public void setForecastWeatherItems(List<ForecastWeatherItem> forecastWeatherItems) {
        this.forecastWeatherItems = forecastWeatherItems;
    }
}

