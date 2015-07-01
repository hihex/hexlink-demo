package hihex.sbrc_samples.hello_sbrc;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

import hihex.sbrc.Client;
import hihex.sbrc.ClientFactory;
import hihex.sbrc.DisconnectReason;
import hihex.sbrc.Identity;
import hihex.sbrc.SbrcManager;
import hihex.sbrc.StandardClient;
import hihex.sbrc.android.SbrcActivity;
import hihex.sbrc.events.PanState;
import hihex.sbrc.modules.JoystickModule;

// This is a sample Android-based activity. Connect a HexLink client from your cellphone, and drag the color squares.
//
// The SbrcActivity class is provided to simplify initialization of the HexLink server. If you are writing an
// Android-based single-activity application, you should start by subclassing SbrcActivity.
//
public final class HelloSbrcActivity extends SbrcActivity {
    private static final Random sRandom = new Random();
    private FrameLayout mFrameLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // All normal Android initialization stuff goes to here.
        mFrameLayout = new FrameLayout(this);
        setContentView(mFrameLayout);
    }

    // This method is called when the HexLink service is fully functional. All HexLink-related methods should be placed
    // here.
    @Override
    protected void onSbrcReady() {
        final SbrcManager manager = getSbrcManager();

        // HexLink supports multi-user natively. The ClientFactory will create a Client instance for each connected
        // user.
        manager.setClientFactory(new ClientFactory() {
            @Override
            public Client create() {
                return new SampleClient();
            }
        });
    }

    // The basic ingredient of a Client is its touch screen. The StandardClient class provides high-level gesture
    // analysis of these touch events.
    //
    // In a StandardClient, the screen is divided evenly into rectangles called Modules. Each Module define a gesture
    // idiom such as "D-Pad", "Joystick", "Mouse", etc. Combine these Modules together to provide a powerful controlling
    // experience.
    //
    private final class SampleClient extends StandardClient {
        private final TextView mView;

        public SampleClient() {
            // Here we simulate the whole touch screen as a joystick.
            super(/*rows*/1, /*columns*/1, /*isLandscape*/false);

            final JoystickModule module = new JoystickModule() {
                @Override
                protected void onJoystickEvent(final PanState panState, final float relX, final float relY) {
                    if (panState == PanState.kCanceled) {
                        return;
                    }

                    // Move our color box. The `relX` and `relY` describe the displacement from the center. By default
                    // the distance is less than 144 which can be configured using `module.setRadius()`. Note that in
                    // HexLink, all clients' touch screen widths are normalized to 320.
                    //
                    // Note! The callbacks from libsbrc usually aren't run in the main thread. This is a deliberate choice, as
                    // many game engines don't use the main thread as the rendering thread anyway.
                    //
                    // This means if we want to update the Android UI, we should remember to call `runOnUiThread()`:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mView.getLayoutParams();
                            params.leftMargin = 144 + (int) relX;
                            params.topMargin = 144 + (int) relY;
                            mView.setLayoutParams(params);
                        }
                    });
                }
            };

            setModules(module);

            mView = new TextView(HelloSbrcActivity.this);
        }

        // This method is called whenever the user is connected. So let's show our color box.
        @Override
        public void onConnect(final Identity identity) {
            super.onConnect(identity);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final int randomColor = Color.HSVToColor(new float[]{sRandom.nextFloat() * 360, 1, 1});
                    mView.setBackgroundColor(randomColor);
                    mView.setTextColor(Color.BLACK);
                    mView.setText(identity.nickname);

                    final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200, 50);
                    params.leftMargin = 144;
                    params.topMargin = 144;
                    mFrameLayout.addView(mView, params);
                }
            });
        }

        // This method is called whenever we are sure that the user is disconnected (due to network conditions, this
        // method may not be called at the same time the connection is lost). Here we will remove the color box.
        @Override
        public void onDisconnect(final Identity identity, final DisconnectReason disconnectReason) {
            super.onDisconnect(identity, disconnectReason);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFrameLayout.removeView(mView);
                }
            });
        }
    }
}
