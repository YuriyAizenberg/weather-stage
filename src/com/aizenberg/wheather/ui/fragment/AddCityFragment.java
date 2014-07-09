package com.aizenberg.wheather.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.Preferences;
import com.aizenberg.wheather.async.DataManager;
import com.aizenberg.wheather.async.IRemoteCallback;
import com.aizenberg.wheather.model.WeatherItem;
import com.aizenberg.wheather.ui.adapter.CityAdapter;
import com.aizenberg.wheather.ui.view.PressureView;
import com.aizenberg.wheather.ui.view.TemperatureView;
import com.aizenberg.wheather.utils.LocationHelper;
import com.aizenberg.wheather.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */
public class AddCityFragment extends AbsFragment implements IRemoteCallback<WeatherItem> {

    private ImageView icon;
    private PressureView pressureView;
    private TemperatureView temperatureView;
    private TextView name;
    private ImageView wind;
    private EditText editSearch;
    private View includedView;
    private WeatherItem weatherItem;

    @Override
    protected String getTitle() {
        return Utils.getStringFromResources(R.string.home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.add_city, container, false);
        includedView = inflate.findViewById(R.id.include);
        icon = (ImageView) inflate.findViewById(R.id.imageView);
        pressureView = (PressureView) inflate.findViewById(R.id.view_pressure);
        temperatureView = (TemperatureView) inflate.findViewById(R.id.view);
        name = (TextView) inflate.findViewById(R.id.txt_name);
        wind = (ImageView) inflate.findViewById(R.id.img_wind);
        editSearch = EditText.class.cast(inflate.findViewById(R.id.edt_search));
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch();
                }
                return false;
            }


        });

        inflate.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        inflate.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weatherItem == null) {
                    informUser("City does not defined");
                } else {
                    Preferences.addCity(weatherItem.getCityId());
                    getAbsActivity().onBackPressed();
                }
            }
        });

        inflate.findViewById(R.id.btn_geo_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location lastKnownLocation = LocationHelper.getInstance().getLastKnownLocation();
                if (lastKnownLocation == null) {
                    informUser("Location does not defined");
                } else {
                    DataManager.getWeather(AddCityFragment.this, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                }
            }
        });

        return inflate;
    }

    private void performSearch() {
        String searchText = editSearch.getText().toString();
        if (searchText.isEmpty()) return;
        DataManager.getWeather(AddCityFragment.this, searchText);
    }

    private void fillFields(WeatherItem item) {
        includedView.setVisibility(View.VISIBLE);
        pressureView.setDataWithImage(item.getPressure(), null, R.drawable.pressure);
        name.setText(item.getName());
        temperatureView.setData(item.getTemp(), null);
        wind.setRotation(item.getWindDeg());
        List<WeatherItem.WeatherDetail> weatherDetails = item.getWeatherDetails();
        if (weatherDetails != null && !weatherDetails.isEmpty()) {
            WeatherItem.WeatherDetail weatherDetail = weatherDetails.get(0);
            ImageLoader.getInstance().displayImage(CityAdapter.BITMAP_DEF_URL + weatherDetail.getIcon() + CityAdapter.BITMAP_DEF_EXTENSION, icon);
        } else {
            ImageLoader.getInstance().displayImage("", icon);
        }
    }

    @Override
    public void onSuccess(WeatherItem result) {
        fillFields(result);
        this.weatherItem = result;
    }

    @Override
    public void onError(Throwable error) {
        informUser(error.getMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
