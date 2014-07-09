package com.aizenberg.wheather.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.async.DataManager;
import com.aizenberg.wheather.async.IRemoteCallback;
import com.aizenberg.wheather.model.Forecast;
import com.aizenberg.wheather.model.WeatherItem;
import com.aizenberg.wheather.ui.adapter.ForecastAdapter;

/**
 * Created by Юрий on 07.07.14.
 */
public class WeatherForecastFragment extends AbsFragment implements IRemoteCallback<Forecast> {

    private WeatherItem item;
    private ForecastAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            item = (WeatherItem) arguments.getSerializable(WeatherItem.BUNDLE_KEY);
        }
        View view = inflater.inflate(R.layout.detail_list_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ForecastAdapter(getAbsActivity());
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (item != null) {
            DataManager.getForecast(this, item.getCityId(), 7);
        }
        getAbsActivity().getImgRight().setImageBitmap(null);
    }

    @Override
    protected String getTitle() {
        return item != null ? item.getName() + "(" + item.getCountry() + ")" : "Undefined";
    }


    @Override
    public void onSuccess(Forecast result) {
        adapter.setData(result.getForecastWeatherItems());
    }

    @Override
    public void onError(Throwable error) {
        informUser(error.getMessage());
    }
}
