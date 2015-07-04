package com.hihex.sbrc.samples;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent0) {
        if (intent0.getAction().equals(Intent.ACTION_BOOT_COMPLETED)
                || intent0.getAction().equals(
                        "com.duokan.duokanplayer.BOOT_COMPLETED")
                        || intent0.getAction().equals(
                                "android.net.conn.CONNECTIVITY_CHANGE")
                                || intent0.getAction().equals(
                                        "android.intent.action.QUICKBOOT_POWERON")) {
            Log.e("SBRC", "TV Boot up, hold on...");
            final Intent intent = new Intent(context, DummyService.class);
            context.startService(intent);
        }
    }
}
