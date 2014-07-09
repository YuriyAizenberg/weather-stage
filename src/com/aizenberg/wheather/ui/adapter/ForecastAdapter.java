package com.aizenberg.wheather.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.model.ForecastWeatherItem;
import com.aizenberg.wheather.ui.view.HumidityView;
import com.aizenberg.wheather.ui.view.PressureView;
import com.aizenberg.wheather.ui.view.TemperatureView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Юрий on 07.07.14.
 */
public class ForecastAdapter extends AbsAdapter<ForecastWeatherItem> {

    public static final String PATTERN = "yyyy.MM.dd";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);

    public ForecastAdapter(Context context) {
        super(context);
    }

    @Override
    public void setData(List<ForecastWeatherItem> values, Boolean clear, Boolean addToStart) {
        Collections.sort(values);
        super.setData(values, clear, addToStart);
    }

    //TODO UIHolder pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForecastWeatherItem item = getItem(position);
        View inflate = inflater.inflate(R.layout.forecast_list_item, parent, false);
        TextView.class.cast(inflate.findViewById(R.id.txt_time)).setText(simpleDateFormat.format(new Date(item.getTimestamp() * 1000)));
        TemperatureView.class.cast(inflate.findViewById(R.id.view_morning)).setDataWithImage(item.getTempMorn(), getDegreeChar(), R.drawable.sunrise);
        TemperatureView.class.cast(inflate.findViewById(R.id.view_day)).setDataWithImage(item.getTempDay(), getDegreeChar(), R.drawable.sun);
        TemperatureView.class.cast(inflate.findViewById(R.id.view_eve)).setDataWithImage(item.getTempEve(), getDegreeChar(), R.drawable.sunset);
        TemperatureView.class.cast(inflate.findViewById(R.id.view_night)).setDataWithImage(item.getTempNight(), getDegreeChar(), R.drawable.moon);
        TemperatureView.class.cast(inflate.findViewById(R.id.view_min)).setDataWithImage(item.getTempMin(), getDegreeChar(), R.drawable.therm_min);
        TemperatureView.class.cast(inflate.findViewById(R.id.view_max)).setDataWithImage(item.getTempMax(), getDegreeChar(), R.drawable.therm_max);
        PressureView.class.cast(inflate.findViewById(R.id.view_pressure)).setDataWithImage(item.getPressure(), null, R.drawable.pressure);
        HumidityView.class.cast(inflate.findViewById(R.id.view_hum)).setDataWithImage(item.getHumidity(), null, R.drawable.humidity);
        return inflate;
    }

    private String getDegreeChar() {
        return context.getResources().getString(R.string.degree);
    }
}
