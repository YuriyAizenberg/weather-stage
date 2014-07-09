package com.aizenberg.wheather.async.modules;

import com.aizenberg.wheather.model.Forecast;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Юрий on 06.07.14.
 */
public class ForecastModule extends AbstractModule<Forecast> {

    public static final String END_POINT = "forecast/daily";

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
    private int cnt;

    public ForecastModule(long id, int cnt) {
        this.id = id;
        this.cnt = cnt;
        filter = Filter.ID;
    }

    public ForecastModule(String name, int cnt) {
        this.name = name;
        this.cnt = cnt;
        filter = Filter.NAME;
    }

    public ForecastModule(double lat, double lng, int cnt) {
        this.lat = lat;
        this.lng = lng;
        this.cnt = cnt;
        filter = Filter.COORD;
    }

    @Override
    protected HttpRequestBase createHttpUriRequest() {
        return new HttpGet();
    }

    @Override
    protected String getURL() {
        String baseUrl = super.getURL() + END_POINT;
        String url = null;
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("units", "metric");
        args.put("cnt", cnt);
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
    protected boolean isRequestAuthorized() {
        return false;
    }

    @Override
    protected void handleResponse(HttpResponse httpResponse) {
        JSONObject jsonObject = baseParseResponse(httpResponse);
        if (jsonObject != null) {
            try {
                serviceResponseObject.setEntity(new Forecast(jsonObject));
            } catch (JSONException e) {
                logger.error(e.getMessage(), e);
                serviceResponseObject.setErrorMessage(e.getMessage());
            }
        }
    }
}
