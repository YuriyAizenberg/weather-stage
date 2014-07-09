package com.aizenberg.wheather.ui.fragment;

import android.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.aizenberg.weather.R;
import com.aizenberg.wheather.ui.AbsActivity;
import com.aizenberg.wheather.ui.MainActivity;
import com.aizenberg.wheather.utils.Utils;

public abstract class AbsFragment extends Fragment {

    protected AbsActivity getAbsActivity() {
        return getActivity() == null ? null : AbsActivity.class.cast(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getAbsActivity() != null && getAbsActivity().getImgLeft() != null) {
            ImageView imgLeft = getAbsActivity().getImgLeft();
            imgLeft.setImageResource(R.drawable.ic_back_green);
            imgLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbsActivity absActivity = getAbsActivity();
                    if (absActivity != null) {
                        absActivity.onBackPressed();
                    }
                }
            });

            getAbsActivity().getTxtTitle().setText(getTitle());
            getAbsActivity().getImgRight().setBackgroundDrawable(null);
        }
    }

    public void informUser(String message) {
        if (message == null) return;
        if (getAbsActivity() != null) {
            Toast.makeText(getAbsActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void managerProgressBar(boolean show) {
        if (getAbsActivity() != null) {
            ((MainActivity) getAbsActivity()).managerProgressBar(show);
        }
    }

    public void informUser(int message) {
        informUser(Utils.getStringFromResources(message));
    }

    public void onBegin() {
        managerProgressBar(true);
    }

    public void onEnd() {
        managerProgressBar(false);
    }


    protected abstract String getTitle();

}
