package com.aizenberg.wheather.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.aizenberg.weather.R;

/**
 * Created by Юрий on 05.07.14.
 */
public abstract class AbsActivity extends Activity {

    protected ImageView imgLeft;
    protected ImageView imgRight;
    protected TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        imgLeft = (ImageView) findViewById(R.id.img_left);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        imgRight = (ImageView) findViewById(R.id.img_right);
    }

    public void switchFragment(Fragments fragments, boolean addToBackStack, Bundle args) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment instantiate = Fragment.instantiate(this, fragments.getClazz().getName(), args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(getFragmentContainerId(), instantiate);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public ImageView getImgLeft() {
        return imgLeft;
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public ImageView getImgRight() {
        return imgRight;
    }

    public void switchFragment(Fragments fragments) {
        switchFragment(fragments, true, null);
    }

    public void switchFragment(Fragments fragments, Bundle args) {
        switchFragment(fragments, true, args);
    }

    protected abstract int getFragmentContainerId();

    protected abstract int getLayoutResource();
}
