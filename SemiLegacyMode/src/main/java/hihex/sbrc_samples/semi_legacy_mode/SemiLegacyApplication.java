package hihex.sbrc_samples.semi_legacy_mode;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import hihex.sbrc.ClientFactories;
import hihex.sbrc.SbrcManager;
import hihex.sbrc.StandardClient;
import hihex.sbrc.android.DirectionModule;

// We will manage all interactions between HexLink and the activities in the application level. Remeber to set the name
// in AndroidManifest.xml's <application> tag!
//
public final class SemiLegacyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private SbrcManager mSbrcManager;
    private int mPauseResumeCount = -1;

    public final SbrcManager getSbrcManager() {
        return mSbrcManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        mSbrcManager = SbrcManager.create(this);
        mSbrcManager.runWhenReady(new Runnable() {
            @Override
            public void run() {
                // The DirectionModule simulates a D-Pad, and inject appropriate key events to the current application.
                final StandardClient client = new StandardClient(/*isLandscape*/false, new DirectionModule());
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
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {}


    @Override
    public void onActivityResumed(final Activity activity) {}

    @Override
    public void onActivityPaused(final Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
