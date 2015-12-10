package com.togather.me.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by rahul on 08-09-2015.
 */
public class UIUtils {

    public static void setBackground(View view, int drawableId) {
        setBackground(view, getDrawable(view.getContext(), drawableId));
    }

    @SuppressWarnings("deprecation")
    public static void setBackground(View view, Drawable drawable) {
        if (hasJellyBean())
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    public static int getColor(Context context, int colorId) {
        return getColor(context, colorId, context.getTheme());
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int colorId, Resources.Theme theme) {
        return hasM() ? context.getResources().getColor(colorId, theme)
                      : context.getResources().getColor(colorId);
    }

    public static Drawable getDrawable(Context context, int id) {
        return getDrawable(context, id, context.getTheme());
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int id, Resources.Theme theme) {
        return hasLollipop() ? context.getResources().getDrawable(id, theme)
                             : context.getResources().getDrawable(id);
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void addRippleEffectToView(View view) {
        addRippleEffectToView(view, Color.parseColor("#d3d3d3"), false, 0);
    }

    public static void addRippleEffectToView(View view, boolean isCircle, int radiusInDp) {
        addRippleEffectToView(view, Color.parseColor("#d3d3d3"), isCircle, radiusInDp);
    }

    public static void addRippleEffectToView(View view, int color, boolean isCircle,
                                             int radiusInDp) {
        if (!hasLollipop())
            MaterialRippleLayout.on(view)
                                .rippleColor(color)
                                .rippleHover(true)
                                .rippleOverlay(true)
                                .rippleDuration(400)
                                .rippleAlpha(0.5f)
                                .rippleRoundedCorners(isCircle ? radiusInDp : 0)
                                .create();
    }

}