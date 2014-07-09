package com.aizenberg.wheather.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aizenberg.weather.R;

/**
 * Created by yuriy
 * 7/9/14
 */
public abstract class DataView extends LinearLayout {

    protected ImageView imageView;
    protected TextView textView;

    public DataView(Context context) {
        super(context);
        init(context);
    }

    public DataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DataView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_view_temp, this, true);
        textView = (TextView) findViewById(R.id.txt_temp);
        imageView = (ImageView) findViewById(R.id.img_temp);
    }

    public DataView setData(float data, String text) {
        textView.setText(String.valueOf(data) + (text != null ? text : ""));
        return this;
    }

    public DataView setDataWithImage(float data, String text, int resourceId) {
        this.setData(data, text);
        if (getResources() != null) {
            imageView.setImageResource(resourceId);
        }
        return this;
    }
}
