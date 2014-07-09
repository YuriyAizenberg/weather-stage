package com.aizenberg.wheather.async;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {
    public static final int SOCKET_TIMEOUT = 25000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    public static final int PORT80 = 80;
    public static final int PORT443 = 443;
    private static DefaultHttpClient client;

    public synchronized static DefaultHttpClient getThreadSafeClient() {
        if (client != null) {
            return client;
        }
        client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        mgr.getSchemeRegistry().register(new Scheme(HTTP, PlainSocketFactory.getSocketFactory(), PORT80));
        mgr.getSchemeRegistry().register(new Scheme(HTTPS, SSLSocketFactory.getSocketFactory(), PORT443));
        HttpParams params = client.getParams();
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
        return client;
    }
}
