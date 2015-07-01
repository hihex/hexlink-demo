package com.hihex.sbrc.samples;

import java.io.ByteArrayOutputStream;

import hihex.sbrc.Client;
import hihex.sbrc.ClientFactory;
import hihex.sbrc.SbrcManager;
import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.hihex.sbrc.samples.client.DpadClient;

public class MyApplication extends Application {
    SbrcManager manager;

    @Override
    public void onCreate() {
        initSbrc();
    }

    private final void initSbrc() {
        manager = SbrcManager.create(this);
        getSbrcManager().setClientFactory(new ClientFactory() {
            @Override
            public Client create() {
                return new DpadClient();
            }
        });
        manager.setIcon(fetchGameIcon());
    }

    public final SbrcManager getSbrcManager() {
        if (manager == null) {
            initSbrc();
        }
        return manager;
    }

    private byte[] fetchGameIcon() {
        final Drawable drawable = getResources().getDrawable(
                R.drawable.ic_launcher);
        if (!(drawable instanceof BitmapDrawable)) {
            Log.e("SBRCSample",
                    "Something's wrong -- ic_launcher.png does not give us a BitmapDrawable?");
            return null;
        }
        final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        final byte[] icon = stream.toByteArray();
        if (icon.length >= 32768) {
            Log.e("SBRCSample", "The icon is larger than 32 KB.");
            return null;
        }
        return icon;
    }

    public final void initActivity(final Activity activity) {
    }
}
