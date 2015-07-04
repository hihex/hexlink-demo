package com.hihex.sbrc.samples;

import hihex.sbrc.ClientFactories;
import hihex.sbrc.SbrcManager;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.hihex.sbrc.samples.client.DpadClient;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks{
    private SbrcManager mSbrcManager=null;
    private int mPauseResumeCount = -1;

    public final SbrcManager getSbrcManager() {
        return mSbrcManager;
    }
    @Override
    public void onCreate() {
        registerActivityLifecycleCallbacks(this);
        
    }
    
    private void initSbrc(){
            mSbrcManager = SbrcManager.create(this);
            mSbrcManager.runWhenReady(new Runnable() {
                @Override
                public void run() {
                    // The DirectionModule simulates a D-Pad, and inject appropriate key events to the current application.
                    final DpadClient client = new DpadClient();
                    mSbrcManager.setClientFactory(ClientFactories.singleton(client));
                }
            });
        
    }
    

    // Here we put the SBRC pause/resume in the started/stopped pair. This is mainly to avoid the client being
    // disconnected when another app in background has interrupted (e.g. showing a dialog). If you do want to client to
    // disconnect, then put the code in the pause/resume pair. Note that the activity lifecycle is usually like this:
    //
    //    ... [old activity pause] → [new activity start] → [new activity resume] → [old activity stop] ...
    //
    // so if you use pause/resume, remember to delay the ref-counting with a timeout.

    @Override
    public void onActivityStarted(final Activity activity) {
        if (mPauseResumeCount < 0) {
            mPauseResumeCount = 1;
        } else {
            if (mPauseResumeCount == 0 && mSbrcManager != null) {
                mSbrcManager.resume();
            }
            mPauseResumeCount += 1;
        }
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        mPauseResumeCount -= 1;
        if (mPauseResumeCount == 0 && mSbrcManager != null) {
            mSbrcManager.pause();
        }
    }

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
        if(mSbrcManager==null){
            initSbrc();
        }
    }


    @Override
    public void onActivityResumed(final Activity activity) {}

    @Override
    public void onActivityPaused(final Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {}

    @Override
    public void onActivityDestroyed(final Activity activity) {}
}
