package com.aizenberg.wheather.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.Preferences;
import com.aizenberg.wheather.async.DataManager;
import com.aizenberg.wheather.async.IRemoteCallback;
import com.aizenberg.wheather.model.WeatherItem;
import com.aizenberg.wheather.ui.Fragments;
import com.aizenberg.wheather.ui.adapter.CityAdapter;
import com.aizenberg.wheather.utils.Utils;

import java.util.List;

/**
 * Created by Юрий on 05.07.14.
 */
public class HomeFragment extends AbsFragment implements IRemoteCallback<List<WeatherItem>>, AdapterView.OnItemClickListener {

    private CityAdapter adapter;
    private boolean isRefreshComplete = true;

    @Override
    protected String getTitle() {
        return Utils.getStringFromResources(R.string.home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.cities, container, false);
        adapter = new CityAdapter(getAbsActivity());
        ListView listView = ListView.class.cast(inflate.findViewById(R.id.listView));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        DataManager.getWeatherList(this, Preferences.getCities());
        ImageView imgLeft = getAbsActivity().getImgLeft();
        imgLeft.setImageResource(R.drawable.refresh_icon);
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRefreshComplete) {
                    DataManager.getWeatherList(HomeFragment.this, Preferences.getCities());
                }
            }
        });
        ImageView imgRight = getAbsActivity().getImgRight();
        imgRight.setImageResource(R.drawable.action_accept);
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAbsActivity().switchFragment(Fragments.ADD_CITY, true, null);
            }
        });
    }

    @Override
    public void onBegin() {
        super.onBegin();
        isRefreshComplete = false;
    }

    @Override
    public void onEnd() {
        super.onEnd();
        isRefreshComplete = true;
    }

    @Override
    public void onSuccess(List<WeatherItem> result) {
        adapter.setData(result);
    }

    @Override
    public void onError(Throwable error) {
        informUser(error.getMessage());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeatherItem adapterItem = adapter.getItem(position);
        if (adapterItem == null) return;
        Bundle args = new Bundle();
        args.putSerializable(WeatherItem.BUNDLE_KEY, adapterItem);
        getAbsActivity().switchFragment(Fragments.DETAIL, args);
    }
}
