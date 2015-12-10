package com.togather.me.ui.utils;

/**
 * Created by Aditya
 */
public class InterfaceUtils {

    public interface StopCheckedListener {
        void onStopChecked(int index, boolean isChecked);
    }

    public interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(int position);
    }


    public interface DrawerOpenListener {
        void onDrawerToggle(boolean isOpened);
    }
}
