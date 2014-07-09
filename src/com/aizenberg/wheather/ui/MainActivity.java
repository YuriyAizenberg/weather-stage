package com.aizenberg.wheather.ui;

import android.os.Bundle;
import android.view.View;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.ui.anim.AnimationHelper;
import com.aizenberg.wheather.utils.INetworkConnectivityStateCallback;
import com.aizenberg.wheather.utils.NetworkConnectivityListener;

public class MainActivity extends AbsActivity implements INetworkConnectivityStateCallback {
    /**
     * Called when the activity is first created.
     */

    private View noInternetConnectionView;
    private View progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFragment(Fragments.HOME);
        noInternetConnectionView = findViewById(R.id.no_internet_layout);
        progressBar = findViewById(R.id.progressBar);

    }

    public void managerProgressBar(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkConnectivityListener.getInstance().addNetworkConnectivityListener(this);
        NetworkConnectivityListener.getInstance().startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkConnectivityListener.getInstance().removeNetworkConnectivityListener(this);
        NetworkConnectivityListener.getInstance().stopListening();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.main_container;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.main;
    }

    @Override
    public void OnNetworkStateChange(NetworkTypes type) {
        switch (type) {
            case DISABLE:
                if (noInternetConnectionView.getVisibility() == View.GONE) {
                    AnimationHelper.expand(noInternetConnectionView);
                }
                break;
            case MOBILE:
            case WIFI:
            case WIMAX:
                if (noInternetConnectionView.getVisibility() == View.VISIBLE) {
                    AnimationHelper.collapse(noInternetConnectionView);
                }
                break;
        }
    }
}
