package com.hihex.sbrc.samples;

import hihex.sbrc.android.SbrcActivity;
import android.util.Log;

public class MySbrcActivity extends SbrcActivity {
    @Override
    protected void onSbrcReady() {
        Log.e("SBRC", "onSbrcReady");
    }
}
