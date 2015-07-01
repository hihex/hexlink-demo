package com.hihex.sbrc.samples;

import hihex.sbrc.Client;
import hihex.sbrc.ClientFactory;
import hihex.sbrc.SbrcManager;
import android.app.Activity;
import android.app.Application;

import com.hihex.sbrc.samples.client.DpadClient;

public class MyApplication extends Application{
    SbrcManager manager;
    @Override
    public void onCreate(){
        manager = SbrcManager.create(this);

    }
    public final SbrcManager getSbrcManager(){
        if(manager==null){
            manager = SbrcManager.create(this);
        }
        return manager;
    }
    public final void initActivity(final Activity activity){
        getSbrcManager().setClientFactory(new ClientFactory() {
            @Override
            public Client create() {
                return new DpadClient(activity);
            }
        });
    }
}
