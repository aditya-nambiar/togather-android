package com.togather.me.gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.togather.me.togather.R;
import com.togather.me.util.LogUtils;
import com.togather.me.util.UIUtils;

public class GCMNotificationIntentService extends IntentService {
	private static String TAG = LogUtils.makeLogTag(GCMNotificationIntentService.class);
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				LogUtils.LOGD(TAG, "extras :" + extras.toString());
				if(extras.containsKey(com.togather.me.gcm.GCMConstants.GCM_MSG_KEY)){
					String msg = extras.getString(com.togather.me.gcm.GCMConstants.GCM_MSG_KEY);
					/*This msg is typically a json. This is what we need to
					* process and handle the subsequent. Right now just
					* showing a notification*/
					sendNotification(msg);
				}
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void sendNotification(String msg) {
//		resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        resultIntent.putExtra("msg", msg);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
//                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder mBuilder = new Notification.Builder(this);
		mBuilder.setContentTitle(getString(R.string.app_name))
				.setContentText("You've received new message.")
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentText(msg)
				.setAutoCancel(true);

		if (!UIUtils.hasLollipop())
			mBuilder.setSmallIcon(R.mipmap.ic_launcher);
		else {
			mBuilder.setColor(UIUtils.getColor(this, R.color.theme_primary));
		}

		if (UIUtils.hasJellyBean()) {
			mBuilder.setStyle(new Notification.BigTextStyle().bigText(msg));
			mNotificationManager.notify(notifyID, mBuilder.build());
		} else
			mNotificationManager.notify(notifyID, mBuilder.getNotification());
	}
}
