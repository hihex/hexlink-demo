package com.hihex.sbrc.samples;

import hihex.sbrc.Client;
import hihex.sbrc.ClientFactory;
import hihex.sbrc.SbrcManager;

import com.hihex.sbrc.samples.client.DpadClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity{
    static SbrcManager manager;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSbrc();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(manager!=null){
            Log.e("SBRC", "onPause:"+manager);
//            manager.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(manager!=null){
            Log.e("SBRC", "onResume:"+manager);
//            manager.resume();
        }
    }

    private final void initSbrc() {
        if(manager==null){
            manager = SbrcManager.create(this.getApplicationContext());
            getSbrcManager().setClientFactory(new ClientFactory() {
                @Override
                public Client create() {
                    return new DpadClient();
                }
            });
            Log.e("SBRC", "initSbrc:"+manager);
        }
    }

    public final SbrcManager getSbrcManager() {
        if (manager == null) {
            initSbrc();
        }
        return manager;
    }

}
