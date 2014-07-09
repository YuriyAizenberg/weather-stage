package com.aizenberg.wheather.utils;

/**
 * Created by Server on 21.01.14.
 */
public interface INetworkConnectivityStateCallback {
    void OnNetworkStateChange(NetworkTypes type);

    enum NetworkTypes {
        DISABLE,
        MOBILE,
        WIFI,
        WIMAX
    }
}
