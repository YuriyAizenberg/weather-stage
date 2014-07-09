package com.aizenberg.wheather.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.model.WeatherItem;
import com.aizenberg.wheather.ui.view.PressureView;
import com.aizenberg.wheather.ui.view.TemperatureView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Юрий on 06.07.14.
 */
public class CityAdapter extends AbsAdapter<WeatherItem> {

    public static final String BITMAP_DEF_URL = "http://openweathermap.org/img/w/";
    public static final String BITMAP_DEF_EXTENSION = ".png";

    public CityAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherItem item = getItem(position);
        UIHolder uiHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.city_list_item, parent, false);
            uiHolder = new UIHolder();
            uiHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
            uiHolder.txtPress = (PressureView) convertView.findViewById(R.id.view_pressure);
            uiHolder.temperatureView = (TemperatureView) convertView.findViewById(R.id.view);
            uiHolder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            uiHolder.img_wind = (ImageView) convertView.findViewById(R.id.img_wind);
            convertView.setTag(uiHolder);
        } else {
            uiHolder = (UIHolder) convertView.getTag();
        }

        uiHolder.txtPress.setDataWithImage(item.getPressure(), null, R.drawable.pressure);
        //uiHolder.txtTemp.setData(context.getString(R.string.temperature) + " " + item.getTemp());
        uiHolder.txtName.setText(item.getName());
        uiHolder.temperatureView.setData(item.getTemp(), context.getResources().getString(R.string.degree));
        uiHolder.img_wind.setRotation(item.getWindDeg());
        List<WeatherItem.WeatherDetail> weatherDetails = item.getWeatherDetails();
        if (weatherDetails != null && !weatherDetails.isEmpty()) {
            WeatherItem.WeatherDetail weatherDetail = weatherDetails.get(0);
            ImageLoader.getInstance().displayImage(BITMAP_DEF_URL + weatherDetail.getIcon() + BITMAP_DEF_EXTENSION, uiHolder.image);
        } else {
            ImageLoader.getInstance().displayImage("", uiHolder.image);
        }
        return convertView;
    }

    private static class UIHolder {
        private TemperatureView temperatureView;
        private PressureView txtPress;
        private TextView txtName;
        private ImageView image;
        private ImageView img_wind;
    }
}
