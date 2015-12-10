package com.togather.me.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.togather.me.R;
import com.togather.me.util.LogUtils;
import com.togather.me.util.PrefUtils;


/**
 * Created by mouli on 7/25/15.
 */
public class DrawerMainSectionView extends LinearLayout {
    private static String TAG = LogUtils.makeLogTag(DrawerMainSectionView.class);
    private Drawable mIcon;
    private String mName;
    private String inr = "\u20B9";
    private int mBalance= PrefUtils.getInt(getContext(), PrefUtils.PREF_KEY_CREDIT_BALANCE, 0);
    public DrawerMainSectionView(Context context) {
        super(context);
        init();
    }

    public DrawerMainSectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        init();
    }

    public DrawerMainSectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attributeSet) {
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.DrawerMainSectionView);

        try {
            mIcon = a.getDrawable(R.styleable.DrawerMainSectionView_icon_src);
            if(a.hasValue(R.styleable.DrawerMainSectionView_section_name)) {
                mName = a.getString(R.styleable.DrawerMainSectionView_section_name);
            } else {
                mName = getContext().getString(R.string.app_name);
            }
        } finally {
            a.recycle();
        }
    }

    private void init() {
        this.setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater.from(getContext()).inflate(getLayoutId(), this);
//        if(mIcon!=null) {
//            ((ImageView) findViewById(R.id.iv_main_section_icon)).setImageDrawable(mIcon);
//        }
//        if(mName!=null) {
//            ((TextView) findViewById(R.id.tv_main_section_name)).setText(mName);
//        }
//        if(mName.equals("Wallet")) {
//            ((TextView) findViewById(R.id.tv_balance_value)).setText(inr+" "+mBalance+"");
//        } else {
//            ((TextView) findViewById(R.id.tv_balance_value)).setText("");
//        }

    }

    private int getLayoutId() {
        return R.layout.drawer_main_section_item;
    }

}
