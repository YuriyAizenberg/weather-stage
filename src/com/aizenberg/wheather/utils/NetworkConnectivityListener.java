package com.aizenberg.wheather.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.aizenberg.wheather.TheApplication;

import java.util.ArrayList;
import java.util.List;

import static com.aizenberg.wheather.utils.INetworkConnectivityStateCallback.NetworkTypes.*;

/**
 * Created by yuriy
 * 7/9/14
 */
public class NetworkConnectivityListener {
    private static final String TAG = "NetworkConnectivityListener";
    private static NetworkConnectivityListener instance;
    private static Context mContext;
    private static boolean mListening;
    private static List<INetworkConnectivityStateCallback> networkConnectivityStateCallbackList = new ArrayList<INetworkConnectivityStateCallback>();
    private ConnectivityBroadcastReceiver mReceiver;

    NetworkConnectivityListener() {
        mReceiver = new ConnectivityBroadcastReceiver();
    }

    public static NetworkConnectivityListener getInstance() {
        if (instance == null) {
            instance = new NetworkConnectivityListener();
        }
        return instance;
    }

    /**
     * Inform all instances of {@link INetworkConnectivityStateCallback} what network was enabled with {@link INetworkConnectivityStateCallback.NetworkTypes} type
     *
     * @param networkType Network connection type
     */
    private static void invokeAllCallbacks(INetworkConnectivityStateCallback.NetworkTypes networkType) {
        for (INetworkConnectivityStateCallback callback : networkConnectivityStateCallbackList) {
            if (callback != null) callback.OnNetworkStateChange(networkType);
        }
    }

    /**
     * Start listening
     */
    public synchronized void startListening() {
        if (!mListening) {
            mContext = TheApplication.getInstance().getApplicationContext();

            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mReceiver, filter);
            mListening = true;
        }
    }

    /**
     * Stop listening
     */
    public synchronized void stopListening() {
        if (mListening) {
            mContext.unregisterReceiver(mReceiver);
            mContext = null;
            mListening = false;
        }
    }


    public void addNetworkConnectivityListener(INetworkConnectivityStateCallback callback) {
        if (networkConnectivityStateCallbackList.contains(callback)) {
            Log.d(TAG, "Callback already registered");
        } else {
            networkConnectivityStateCallbackList.add(callback);
            invokeImmediate(callback);
        }
    }

    private void invokeImmediate(INetworkConnectivityStateCallback callback) {
        if (callback != null) {
            callback.OnNetworkStateChange(Utils.isNetworkConnected(TheApplication.getInstance().getApplicationContext()) ? WIFI : DISABLE);
        }
    }

    public void removeNetworkConnectivityListener(INetworkConnectivityStateCallback callback) {
        if (!networkConnectivityStateCallbackList.contains(callback)) {
            Log.d(TAG, "Callback already unregistered");
        } else {
            networkConnectivityStateCallbackList.remove(callback);
        }
    }

    public static class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || !mListening) {
                return;
            }

            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
                invokeAllCallbacks(DISABLE);
                return;
            }

            // Check each connection type

            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            /**
             * WIFI
             */

            /** Check the connection **/
            NetworkInfo network = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            // Make sure the network is available
            if (isNetworkAvailable(network)) {
                invokeAllCallbacks(WIFI);
                return;
            }

            /**
             * 2G/3G
             */
            /** Check the connection **/
            network = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (isNetworkAvailable(network)) {
                invokeAllCallbacks(MOBILE);
                return;
            }

            /**
             * 4G
             */

            /** Check the connection **/
            network = cm.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);

            // Make sure the network is available
            if (isNetworkAvailable(network)) {
                invokeAllCallbacks(WIMAX);
                return;
            }

            invokeAllCallbacks(DISABLE);


        }

        private boolean isNetworkAvailable(NetworkInfo network) {
            return network != null && network.isAvailable() && network.isConnected();
        }
    }


}

