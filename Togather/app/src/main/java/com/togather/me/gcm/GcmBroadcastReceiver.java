package com.togather.me.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.togather.me.util.LogUtils;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	private static String TAG = LogUtils.makeLogTag(GcmBroadcastReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtils.LOGD(TAG, "Message received");
		ComponentName comp = new ComponentName(context.getPackageName(),
				com.togather.me.gcm.GCMNotificationIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}
}
