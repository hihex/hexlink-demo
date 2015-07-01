package com.hihex.sbrc.samples;

import hihex.sbrc.android.SbrcActivity;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class MySbrcActivity  extends SbrcActivity {
    @Override
    protected void onSbrcReady(){
        Log.e("SBRC","onSbrcReady");
    }

    private byte[] fetchGameIcon() {
        final Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
        if (!(drawable instanceof BitmapDrawable)) {
            Log.e("SBRCSample", "Something's wrong -- ic_launcher.png does not give us a BitmapDrawable?");
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
}
