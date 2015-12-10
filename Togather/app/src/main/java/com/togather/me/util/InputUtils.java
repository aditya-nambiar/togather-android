package com.togather.me.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mouli on 28/8/15.
 */
public class InputUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
