package com.your_package_name;

import org.json.JSONArray;
import org.json.JSONException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import org.json.JSONArray;

public class SystemNotification extends Plugin {

    final int notif_ID = 1234;
    NotificationManager notificationManager;
    Notification note;
    PendingIntent contentIntent;

    @Override
    public PluginResult execute(String action, JSONArray args, String callbackId)
    {
        PluginResult.Status status = PluginResult.Status.OK;
        String result = "";

        try {
            if (action.equals("createStatusBarNotification")) {
                this.createStatusBarNotification(args.getString(0), args.getString(1), args.getString(2));
            }
            else if (action.equals("updateNotification")) {
                this.updateNotification(args.getString(0), args.getString(1), args.getInt(2));
            }
            else if (action.equals("cancelNotification")) {
                this.cancelNotification();
            }
            else if (action.equals("showTickerText")) {
                this.showTickerText(args.getString(0));
            }
            return new PluginResult(status, result);
        } catch(JSONException e) {
            return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
        }
    }

    private void updateNotification(String contentTitle, String contentText, int number)
    {
        note.setLatestEventInfo(this.ctx, contentTitle, contentText, contentIntent);
        note.number = number;
        notificationManager.notify(notif_ID,note);
    }

    private void createStatusBarNotification(String contentTitle, String contentText, String tickerText)
    {
        notificationManager = (NotificationManager) this.ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        note = new Notification(android.R.drawable.btn_star_big_on, tickerText, System.currentTimeMillis() );
	//change the icon
        
	Intent notificationIntent = new Intent(this.ctx, pwm.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent = notificationIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        contentIntent = PendingIntent.getActivity(this.ctx, 0, notificationIntent, 0);

        note.setLatestEventInfo(this.ctx, contentTitle, contentText, contentIntent);

        note.number = 1;  //Just created notification so number=1. Remove this line if you dont want numbers

        notificationManager.notify(notif_ID,note);
    }

    private void cancelNotification()
    {
        notificationManager.cancel(notif_ID);
    }

    private void showTickerText(String tickerText)
    {
        note.tickerText = tickerText;
        notificationManager.notify(notif_ID,note);
    }

    @Override
    public void onPause()
    {
        super.webView.loadUrl("javascript:navigator.systemNotification.onBackground();");
    }

    @Override
    public void onResume()
    {
        super.webView.loadUrl("javascript:navigator.systemNotification.onForeground();");
    }
}


