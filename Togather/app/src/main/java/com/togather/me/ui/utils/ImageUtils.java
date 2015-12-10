package com.cityflo.customer.ui.utils;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by mouli on 7/28/15.
 */
public class ImageUtils {

    public static void rotateBy180(ImageView imageView, float fromDegrees, float toDegrees) {
        RotateAnimation r;
        r = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        r.setRepeatCount(0);
        r.setDuration(350);
        r.setFillEnabled(true);
        r.setFillAfter(true);
        imageView.startAnimation(r);
    }
}
