package com.natrelin.earthquake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mukondono on 10/21/15.
 */
public class EarthquakeAlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_REFRESH_EARTHQUAKE_ALARM =
            "com.natrelin.earthquake.ACTION_REFRESH_EARTHQUAKE_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startIntent = new Intent(context, EarthquakeUpdateService.class);
        context.startService(startIntent);
    }
}