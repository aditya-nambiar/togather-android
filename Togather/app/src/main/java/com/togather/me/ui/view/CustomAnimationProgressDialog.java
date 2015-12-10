package com.togather.me.ui.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.togather.me.util.LogUtils;
import com.togather.me.R;

/**
 * Created by mouli on 8/24/15.
 */
public class CustomAnimationProgressDialog extends ProgressDialog {
    private static String TAG = LogUtils.makeLogTag(CustomAnimationProgressDialog.class);

    private AnimationDrawable mAnimation;

    public static CustomAnimationProgressDialog newInstance(Context context) {
        CustomAnimationProgressDialog progressDialog = new CustomAnimationProgressDialog(context);
        progressDialog.setIndeterminate(true);
        return progressDialog;
    }

    public CustomAnimationProgressDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.view_custom_progress_dialog);
        ImageView imageView = (ImageView) findViewById(R.id.iv_animation);
        imageView.setImageResource(R.drawable.anim_custom_progress_dialog);
        mAnimation = (AnimationDrawable) imageView.getDrawable();
    }

    @Override
    public void show() {
        super.show();
        if(mAnimation!=null) {
            mAnimation.start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(mAnimation!=null) {
            mAnimation.stop();
        }
    }
}
