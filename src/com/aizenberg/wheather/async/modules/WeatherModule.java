package com.aizenberg.wheather.async.modules;

import com.aizenberg.wheather.model.ServiceResponseObject;
import com.aizenberg.wheather.model.WeatherItem;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Юрий on 06.07.14.
 */
public class WeatherModule extends AbstractModule<WeatherItem> {

    public enum Filter {
        ID,
        COORD,
        NAME
    }

    private Filter filter;
    private long id;
    private String name;
    private double lat;
    private double lng;

    public WeatherModule(long id) {
        this.id = id;
        filter = Filter.ID;
    }

    public WeatherModule(String name) {
        this.name = name;
        filter = Filter.NAME;
    }

    public WeatherModule(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        filter = Filter.COORD;
    }

    @Override
    protected HttpRequestBase createHttpUriRequest() {
        return new HttpGet();
    }

    @Override
    protected boolean isRequestAuthorized() {
        return false;
    }

    @Override
    protected String getURL() {
        String baseUrl = super.getURL() + "weather/";
        String url = null;
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("units", "metric");
        switch (filter) {
            case ID:
                args.put("id", String.valueOf(id));
                break;
            case COORD:
                args.put("lat", lat);
                args.put("lon", lng);
                break;
            case NAME:
                args.put("q", name);
                break;
        }
        url = buildUrl(baseUrl, args);
        return url;
    }

    @Override
    protected void handleResponse(HttpResponse httpResponse) {
        JSONObject jsonObject = baseParseResponse(httpResponse);
        if (jsonObject != null) {
            try {
                serviceResponseObject.setEntity(new WeatherItem(jsonObject));
            } catch (JSONException e) {
                serviceResponseObject.setErrorMessage(e.getMessage());
                logger.error(e.getMessage(), e);
            }
        }
    }
}
