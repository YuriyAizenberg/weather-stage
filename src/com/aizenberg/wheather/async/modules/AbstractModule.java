package com.aizenberg.wheather.async.modules;

import android.net.Uri;
import android.util.Log;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.TheApplication;
import com.aizenberg.wheather.async.HttpClientFactory;
import com.aizenberg.wheather.model.ServerError;
import com.aizenberg.wheather.model.ServiceResponseObject;
import com.aizenberg.wheather.utils.LoggerFactory;
import com.aizenberg.wheather.utils.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */

public abstract class AbstractModule<T> {
    public static final String TAG = AbstractModule.class.getSimpleName();
    protected com.aizenberg.wheather.utils.Logger logger = LoggerFactory.getLogger(TAG);
    protected final static String SERVER_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    protected static HttpClient sClient = HttpClientFactory.getThreadSafeClient();
    protected final String UTF_8 = "UTF-8";
    ServiceResponseObject<T> serviceResponseObject;

    /**
     * Convert {@link org.apache.http.HttpResponse} to {@link String} with UTF-8 charset
     *
     * @param response instance of the {@link org.apache.http.HttpResponse}
     * @return HttpResponse in the String format
     */
    protected static String httpResponseToString(HttpResponse response) {
        String oResult = null;
        try {
            oResult = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (org.apache.http.ParseException e) {
            Log.e(TAG, e.toString());
        }
        return oResult;
    }

    public ServiceResponseObject<T> getServiceResponseObject() {
        return serviceResponseObject;
    }


    protected abstract HttpRequestBase createHttpUriRequest();

    protected abstract boolean isRequestAuthorized();

    protected abstract void handleResponse(HttpResponse httpResponse);

    protected String getURL() {
        return SERVER_URL;
    }


    public void run() {
        if (!Utils.isNetworkConnected(TheApplication.getInstance().getApplicationContext())) {
            serviceResponseObject = new ServiceResponseObject<T>(null, "" + Utils.getStringFromResources(R.string.check_network_connection), null);
            return;
        }
        HttpUriRequest httpRequestBase = getHttpRequest();
        try {
            HttpResponse response = sClient.execute(httpRequestBase);
            handleResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            serviceResponseObject = new ServiceResponseObject<T>(null, e.toString(), null);
        }
    }

    protected JSONObject baseParseResponse(HttpResponse response) {
        serviceResponseObject = new ServiceResponseObject<T>();
        JSONObject result = null;
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            serviceResponseObject.setErrorMessage(response.getStatusLine().getStatusCode() + " status");
        } else {
            String sResponse = httpResponseToString(response);
            if (sResponse == null) {
                serviceResponseObject.setErrorMessage("Unable to parse entity");
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(sResponse);
                    result = new JSONObject(sResponse);
                    handleError(jsonObject, serviceResponseObject);
                    if (serviceResponseObject.getServerError() == null) {
                        result = jsonObject;
                    } else {
                        return null;
                    }
                } catch (JSONException e) {
                    serviceResponseObject.setErrorMessage("Unable to parse JSON");
                    Log.e(TAG, "JSONException", e);
                }
            }
        }
        releaseResources(response);
        return result;
    }

    protected void handleError(JSONObject jsonObject, ServiceResponseObject<T> serviceResponseObject) {
        serviceResponseObject.setServerError(getError(jsonObject));
    }

    private ServerError getError(JSONObject jsonObject) {
        try {
            if (jsonObject.has("message") && jsonObject.has("cod") && jsonObject.getInt("cod") != HttpStatus.SC_OK) {
                return new ServerError(jsonObject.getInt("cod"), jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    protected JSONArray baseParseArrayResponse(HttpResponse response) {
        serviceResponseObject = new ServiceResponseObject<T>();
        JSONArray result = null;
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            serviceResponseObject.setErrorMessage(response.getStatusLine().getStatusCode() + " status");
        } else {
            String sResponse = httpResponseToString(response);
            if (sResponse == null) {
                serviceResponseObject.setErrorMessage("Unable to parse entity");
            } else {
                try {
                    JSONArray jsonObject = new JSONArray(sResponse);
                    //handleError(jsonObject, serviceResponseObject);
                    if (serviceResponseObject.getServerError() == null) {
                        result = jsonObject;
                    } else {
                        return null;
                    }
                } catch (JSONException e) {
                    serviceResponseObject.setErrorMessage("Unable to parse JSON");
                    Log.e(TAG, "JSONException", e);
                }
            }
        }
        releaseResources(response);
        return result;
    }

    /**
     * Without this trick some tests were endless
     *
     * @param response {@link org.apache.http.HttpResponse}
     */
    protected void releaseResources(HttpResponse response) {
        try {
            response.getEntity().consumeContent();
            sClient.getConnectionManager().closeExpiredConnections();
        } catch (IOException e) {
            Log.e(TAG, "Unable to release network resources", e);
        }
    }

    protected HttpUriRequest getHttpRequest() {
        HttpRequestBase request = createHttpUriRequest();
        request.setURI(URI.create(Uri.encode(getURL(), ALLOWED_URI_CHARS)));
        return request;
    }

    /**
     * Build URL for BaseHttpRequest (Get/Delete) based on the args in putted in {@link java.util.HashMap}
     *
     * @param url  base url
     * @param args hashmap of the args
     * @return correctly formatted URL
     */
    protected String buildUrl(String url, HashMap<String, Object> args) {

        if (args == null || args.isEmpty()) {
            return url;
        }

        //Check hashmap for contain notnull values
        boolean nullProtector = false;
        for (String key : args.keySet()) {
            if (args.get(key) != null) {
                nullProtector = true;
                break;
            }
        }

        if (!nullProtector) {
            return url;
        }

        if (!url.endsWith("?")) {
            url += "?";
        }

        List<NameValuePair> params = new LinkedList<NameValuePair>();

        for (String key : args.keySet()) {
            if (args.get(key) != null) {
                params.add(new BasicNameValuePair(key, args.get(key).toString()));
            }
        }

        String paramString = URLEncodedUtils.format(params, UTF_8);

        url += paramString;
        return url;
    }

    /**
     * Build URL for enclosing request (like Put/Post) based on the args in putted in {@link java.util.HashMap}
     *
     * @param request instance of the {@link org.apache.http.client.methods.HttpPost} or {@link org.apache.http.client.methods.HttpPut}
     * @param args    args in the HashMap
     */
    protected void buildRequest(HttpEntityEnclosingRequestBase request, HashMap<String, Object> args) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        for (String key : args.keySet()) {
            if (args.get(key) != null) {
                params.add(new BasicNameValuePair(key, args.get(key).toString()));
            }
        }

        try {
            request.setEntity(new UrlEncodedFormEntity(params, UTF_8));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Build simple BaseHttpRequest. To delete?
     *
     * @param url   base url
     * @param key   get key
     * @param value get value
     * @return correctly formatted url
     */
    protected String buildUrl(String url, String key, String value) {
        if (value == null) return url;
        if (!url.endsWith("?")) url += "?";
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair(key, value));
        return url + URLEncodedUtils.format(params, UTF_8);

    }

}
