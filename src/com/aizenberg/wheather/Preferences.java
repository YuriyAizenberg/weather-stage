package com.aizenberg.wheather;

import android.content.SharedPreferences;
import android.util.Log;
import com.aizenberg.wheather.utils.NullExcludeList;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Preferences {


    public static final String SP_DEFAULT_CITIES = "def_cities";

    private Preferences() {
    }

    public static List<Long> getCities() {
       return fromJSON(get().getString(SP_DEFAULT_CITIES, ""));
    }

    public static void saveCities(List<Long> citiesId) {
        get().edit().putString(SP_DEFAULT_CITIES, toJSON(citiesId)).commit();
    }

    public static void addCity(Long cityId) {
        List<Long> cities = getCities();
        if (!cities.contains(cityId)) {
            cities.add(cityId);
        }
        saveCities(cities);
    }

    /**
     * Exposes the shared preferences to the clients
     */
    public static SharedPreferences get() {
        return TheApplication.getInstance().getPreferences();
    }

    private static String toJSON(List<Long> strings) {
        JSONArray jsonArray = new JSONArray();
        if (strings.isEmpty()) return jsonArray.toString();
        for (Long s : strings) {
            jsonArray.put("" + s);
        }
        return jsonArray.toString();
    }

    private static List<Long> fromJSON(String s) {
        List<Long> strings = new ArrayList<Long>();
        if (s == null || s.isEmpty()) return strings;
        try {
            JSONArray array = new JSONArray(s);
            for (int i = 0; i < array.length(); i++) {
                strings.add(array.getLong(i));
            }
        } catch (JSONException e) {
            Log.e(Preferences.class.getSimpleName(), "Cant parse json", e);
        }
        return strings;
    }



}
