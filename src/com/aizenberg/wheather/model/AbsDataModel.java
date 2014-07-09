package com.aizenberg.wheather.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Юрий on 05.07.14.
 */
public abstract class AbsDataModel implements Serializable {

    public static final float NO_COORD = -0x01f;
    public static final long MAX_LIFE_TIME_IN_CACHE = 1000l * 60l * 10l; //10 minutes
    protected String jsonRepresentation;
    private long createdAt = System.currentTimeMillis();

    /**
     * Need to simple store in database
     *
     * @param jsonObject {@link org.json.JSONObject}
     * @throws JSONException
     */
    public AbsDataModel(JSONObject jsonObject) throws JSONException {
        jsonRepresentation = jsonObject.toString();
    }

    public String getJsonRepresentation() {
        return jsonRepresentation;
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() - createdAt) > MAX_LIFE_TIME_IN_CACHE;
    }

}
