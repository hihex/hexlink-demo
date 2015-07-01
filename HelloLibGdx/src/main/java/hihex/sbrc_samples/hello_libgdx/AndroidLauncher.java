package hihex.sbrc_samples.hello_libgdx;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import hihex.sbrc.SbrcManager;

// This is a sample libgdx-based activity. Connect a HexLink client from your cellphone, and drag the color squares.
//
// Since libgdx provided their own Activity subclass, we cannot use SbrcActivity like the `HelloSbrcActivity` project
// did. We need to write out all the lifecycle methods, see below.
//
public final class AndroidLauncher extends AndroidApplication {
    private SbrcManager mSbrcManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Construct a new SbrcManager instance in onCreate().
        mSbrcManager = SbrcManager.create(this);

        // Proceed to libgdx initialization.
        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new GdxGame(mSbrcManager), config);
    }

    // Tell the SbrcManager when the activity is paused, resumed and destroyed.
    @Override
    protected void onPause() {
        if (mSbrcManager != null) {
            mSbrcManager.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSbrcManager != null) {
            mSbrcManager.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mSbrcManager != null) {
            mSbrcManager.destroy(this);
        }
        super.onDestroy();
    }
}
