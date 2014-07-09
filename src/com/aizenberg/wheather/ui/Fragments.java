package com.aizenberg.wheather.ui;

import com.aizenberg.wheather.ui.fragment.AbsFragment;
import com.aizenberg.wheather.ui.fragment.AddCityFragment;
import com.aizenberg.wheather.ui.fragment.HomeFragment;
import com.aizenberg.wheather.ui.fragment.WeatherForecastFragment;

/**
 * Created by Юрий on 05.07.14.
 */
public enum Fragments {

    HOME(HomeFragment.class),
    ADD_CITY(AddCityFragment.class),
    DETAIL(WeatherForecastFragment.class);
    private Class<? extends AbsFragment> clazz;

    Fragments(Class<? extends AbsFragment> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends AbsFragment> getClazz() {
        return clazz;
    }
}
