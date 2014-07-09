package com.aizenberg.wheather.utils;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import com.aizenberg.wheather.TheApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import java.util.List;

/**
 * Summary. This class provided location access mechanism:
 * First created GPS and A-GPS location managers.
 * After that create {@link android.os.CountDownTimer} with {@link com.aizenberg.wheather.utils.LocationHelper.LConfiguration#MAX_TIME_AWAIT_FOR_GPS_MILLIS} delay
 * If GPS or A-GPS return location early, countdown timer canceled. Otherwise we try to get location via {@link com.google.android.gms.location.LocationClient}
 * <p/>
 * If GPS return {@link android.location.Location} we turn of A-GPS manager. If GPS turned off we revoke A-GPS manager.
 */
public class LocationHelper implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    public static final String TAG = LocationHelper.class.getSimpleName();
    private static LocationHelper locationHelper;
    private static LocationManager locationManager;
    private static LocationManager locationManagerAGPS;
    private static boolean isInitialized;
    private static LocationClient mLocationClient;
    private List<LocationListener> locationListeners = new NullExcludeList<LocationListener>();
    private Location location;
    private LocationCountDown locationCountDown;
    private Approximate approximate;

    private LocationHelper(Context context) {
        locationHelper = this;
        approximate = new Approximate();
        locationCountDown = new LocationCountDown(LConfiguration.MAX_TIME_AWAIT_FOR_GPS_MILLIS, LConfiguration.COUNT_CHECK_MILLIS);
        locationCountDown.start();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManagerAGPS = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addGpsStatusListener(new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                switch (event) {
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        locationManagerAGPS.removeUpdates(locationHelper);
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED:
                        revokeAGps();
                        break;
                }
            }
        });
        isInitialized = true;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationHelper);
        revokeAGps();
    }

    /**
     * Singleton access to instance
     *
     * @return {@link com.aizenberg.wheather.utils.LocationHelper}
     */
    public static LocationHelper getInstance() {
        if (!isInitialized) {
            throw new IllegalStateException("#initialize() must be called first");
        }
        return locationHelper;
    }

    /**
     * Initialize LocationManager 2 instances:
     * 1. GPSLocationManager: GPS can start more than few seconds. We need to use A-GPS while GPS turned off or not fixed location.
     * 2. A-GPS Location Manager: use first
     *
     * @param context {@link android.content.Context}
     */
    public static void initialize(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null", new NullPointerException());
        } else {
            locationHelper = new LocationHelper(context);
            mLocationClient = new LocationClient(context, locationHelper, locationHelper);

        }
    }

    /**
     * Request updates
     */
    private static void revokeAGps() {
        locationManagerAGPS.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationHelper);
    }

    /**
     * Remove all managers
     */
    static void dispose() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationHelper);
        }
        if (locationManagerAGPS != null) {
            locationManagerAGPS.removeUpdates(locationHelper);
        }

    }

    public boolean isLocationAvailable() {
        return getLastKnownLocation() != null || (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public Location getLastKnownLocation() {
        return location;
    }

    /**
     * Add {@link android.location.LocationListener} listener to {@link #locationListeners} collection
     * After adding interface to collection invoke
     * {@link android.location.LocationListener#onProviderDisabled(String)}
     * OR
     * {@link android.location.LocationListener#onProviderEnabled(String)} event
     *
     * @param listener {@link android.location.LocationListener}
     */
    public void addLocationListener(LocationListener listener) {
        if (listener == null) return;

        checkCollection();
        if (locationListeners.contains(listener)) {
            Log.d(TAG, "Listener " + listener.toString() + " already added");
        }
        locationListeners.add(listener);
        invokeCallbackImmediately(listener);
    }

    /**
     * Invoke {@link android.location.LocationListener#onProviderDisabled(String)}
     * OR
     * {@link android.location.LocationListener#onProviderEnabled(String)} event
     *
     * @param listener {@link android.location.LocationListener}
     */
    private void invokeCallbackImmediately(LocationListener listener) {
        if (isLocationAvailable()) {
            listener.onProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            listener.onProviderDisabled(LocationManager.GPS_PROVIDER);
        }
    }

    public void removeLocationListener(LocationListener listener) {
        checkCollection();
        locationListeners.remove(listener);
    }

    private void checkCollection() {
        if (locationListeners == null) {
            locationListeners = new NullExcludeList<LocationListener>();
        }
    }

    private void cancelCountDown() {
        if (locationCountDown != null) {
            locationCountDown.cancel();
            locationCountDown = null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        checkCollection();
        cancelCountDown();
        this.location = location;
        if (!approximate.isLocationAcceptable(location)) return;
        Utils.mLat = location.getLatitude();
        Utils.mLng = location.getLongitude();
        for (LocationListener listener : locationListeners) {
            listener.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        checkCollection();
        for (LocationListener listener : locationListeners) {
            listener.onStatusChanged(provider, status, extras);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        checkCollection();
        cancelCountDown();
        for (LocationListener listener : locationListeners) {
            listener.onProviderEnabled(provider);
        }
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            locationManagerAGPS.removeUpdates(locationHelper);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        checkCollection();
        for (LocationListener listener : locationListeners) {
            if (!isLocationAvailable()) {
                listener.onProviderDisabled(provider);
            }
        }
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            revokeAGps();
        }
        //Ooops
        if (location == null && locationCountDown == null) {
            locationCountDown = new LocationCountDown(LConfiguration.MAX_TIME_AWAIT_FOR_GPS_MILLIS, LConfiguration.COUNT_CHECK_MILLIS);
            locationCountDown.start();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = mLocationClient.getLastLocation();
        if (lastLocation != null) {
            onLocationChanged(lastLocation);
            mLocationClient.disconnect();
        }
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void onCountDown() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(TheApplication.getInstance().getApplicationContext()) == ConnectionResult.SUCCESS) {
            if (location == null) {
                mLocationClient.connect();
            }
        }
    }


    private static interface LConfiguration {
        long MAX_TIME_AWAIT_FOR_GPS_MILLIS = 30000;
        long COUNT_CHECK_MILLIS = 15000;
        int MIN_ACCURACY_FOR_A_GPS_METERS = 100;
        //100km/h in m/s
        double VELOCITY = (100. * 1000.) / (60f * 60f);
        int MAX_ATTEMPT_COUNT = 2;
    }

    private static class LocationCountDown extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public LocationCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (locationHelper != null) {
                locationHelper.onCountDown();
            }
        }
    }

    private static class Approximate {
        private Location previousLocation;
        private long previousTimestamp;
        private int attemptCount;

        public boolean isLocationAcceptable(Location currentLocation) {
            if (currentLocation == null) return false;

            if (previousLocation == null) {
                previousLocation = currentLocation;
                previousTimestamp = System.currentTimeMillis();
                attemptCount = 0;
                return true;
            }

            if (!currentLocation.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                previousLocation = null;
                attemptCount = 0;
                return true;
            }
            if (currentLocation.getAccuracy() <= LConfiguration.MIN_ACCURACY_FOR_A_GPS_METERS) {
                previousLocation = null;
                attemptCount = 0;
                previousTimestamp = System.currentTimeMillis();
                return true;
            }

            float distance = Math.abs(currentLocation.distanceTo(previousLocation));
            long l = System.currentTimeMillis() - previousTimestamp;
            if (l == 0) return true;
            long timeDiff = l / 1000;
            if (timeDiff == 0) return true;
            if ((distance / timeDiff * 1f) > LConfiguration.VELOCITY) {
                if (attemptCount > LConfiguration.MAX_ATTEMPT_COUNT) {
                    previousLocation = currentLocation;
                    attemptCount = 0;
                    previousTimestamp = System.currentTimeMillis();
                    return true;
                } else {
                    ++attemptCount;
                    return false;
                }
            } else {
                previousLocation = currentLocation;
                attemptCount = 0;
                previousTimestamp = System.currentTimeMillis();
                return true;
            }
        }
    }

}
