package com.hihex.sbrc.samples.client;

import hihex.sbrc.DisconnectReason;
import hihex.sbrc.EdgeCallback;
import hihex.sbrc.Identity;
import hihex.sbrc.Module;
import hihex.sbrc.StandardClient;
import hihex.sbrc.events.Edge;
import hihex.sbrc.events.PanEvent;
import hihex.sbrc.events.PanState;
import hihex.sbrc.modules.DpadModule;
import hihex.sbrc.modules.JoystickModule;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SampleStandardClient extends StandardClient {
    private final FrameLayout mParent;
    private final TextView mChild;
    private String mSeq;
    private final Activity mContext;
    private MoveChildHandler mHandler;

    private static final Random mRng = new Random();

    public SampleStandardClient(final Activity context, final FrameLayout layout) {
        // Initialize the standard client to use the landscape mode with a 1x2 grid.
        super(/*rows*/1, /*columns*/2, /*isLandscape*/true);
        // Use joystick on the left, and D-pad on the right.
        setModules(new Module[] {createJoystickModule(), createDpadModule()});
        captureSwipeFromBottom();

        // The rest are to set up the Android views.
        mParent = layout;
        mContext = context;
        mChild = new TextView(context);

        final int color = Color.HSVToColor(new float[] {mRng.nextFloat() * 360, 1, 1});
        mChild.setBackgroundColor(color);
        mChild.setTextColor(Color.BLACK);
        mChild.setTextSize(20);
        mChild.setGravity(Gravity.CENTER);
        mChild.setSingleLine(false);
        mChild.setText("(0, 0)\n.....");

        mSeq = ".....";
    }

    private final Module createJoystickModule() {
        return new JoystickModule() {
            @Override
            protected void onJoystickEvent(final PanState state, final float relX, final float relY) {
                // Set the actions to do when we receive a joystick event:

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (state) {
                        case kBegin:
                            // On begin, set the label to have white text.
                            mChild.setTextColor(Color.WHITE);
                            mHandler.startMoving();
                            break;

                        case kEnd:
                        case kCanceled:
                        case kUnknown:
                            // On end, revert the label to black text.
                            mChild.setTextColor(Color.BLACK);
                            mHandler.stopMoving();
                            break;

                        case kMove:
                            // While moving, change the velocity.
                            updateText();
                            mHandler.velocityX = relX * 0.1;
                            mHandler.velocityY = relY * 0.1;
                            Log.e("SBRC", ":" + relX + "," + relY);
                            break;
                        }
                    }
                });
            }
        };
    }

    private final Module createDpadModule() {
        return new DpadModule() {
            @Override
            protected void onDpadEvent(final Command command) {
                // Set the actions to do when we receive a dpad event.
                // We update the text of the label.

                final String newString = mSeq.substring(1);
                final String commandString;

                switch (command) {
                case kClick:
                    commandString = "\u25cb";
                    break;
                case kLongPress:
                    commandString = "\u25cf";
                    break;
                case kLeft:
                    commandString = "\u2190";
                    break;
                case kRight:
                    commandString = "\u2192";
                    break;
                case kUp:
                    commandString = "\u2191";
                    break;
                case kDown:
                    commandString = "\u2193";
                    break;
                default:
                    commandString = "?";
                }

                mSeq = newString + commandString;

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateText();
                    }
                });
            }
        };
    }

    private void captureSwipeFromBottom() {
        setEdgeCallback(new EdgeCallback() {
            @Override
            public boolean onEdgeSwipe(final Edge edge, final PanEvent event) {
                if (edge == Edge.kBottom) {
                    // TODO do something with *event*.
                }
                return false;
            }
        });
    }

    @Override
    public void onConnect(final Identity identity) {
        super.onConnect(identity);

        // Display a new label when a client connected.
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHandler = new MoveChildHandler(mChild);
                mHandler.attach(mParent);
            }
        });
    }

    @Override
    public void onDisconnect(final Identity identity, final DisconnectReason reason) {
        super.onDisconnect(identity, reason);

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHandler.stopMoving();
                mParent.removeView(mChild);
            }
        });
    }

    private void updateText() {
        mChild.setText("(" + (int) mHandler.velocityX + ", " + (int) mHandler.velocityY + ")\n" + mSeq);
    }
}

// This is a just helper class to move the child label around.
final class MoveChildHandler extends Handler {
    public double velocityX = 0;
    public double velocityY = 0;
    private double mCurX = 100;
    private double mCurY = 400;
    private boolean mIsMoving = false;
    private final FrameLayout.LayoutParams mLayoutParams;
    private final View mView;

    private int mWidth;
    private int mHeight;

    public MoveChildHandler(final View view) {
        mView = view;
        mLayoutParams = new FrameLayout.LayoutParams(100, 50);
    }

    public void attach(final FrameLayout parent) {
        mLayoutParams.topMargin = (int) mCurX;
        mLayoutParams.leftMargin = (int) mCurY;
        parent.addView(mView, mLayoutParams);

        mWidth = 500;
        mHeight = 500;
    }

    public void startMoving() {
        mIsMoving = true;
        sendEmptyMessage(1);
    }

    public void stopMoving() {
        velocityX = 0;
        velocityY = 0;
        mIsMoving = false;
        removeMessages(1);
    }

    @Override
    public void handleMessage(final Message msg) {
        if (!mIsMoving) {
            return;
        }

        mCurX += velocityX;
        mCurY += velocityY;

        if (mCurX < 0) {
            mCurX += mWidth;
        } else if (mCurX >= mWidth) {
            mCurX -= mWidth;
        }

        if (mCurY < 0) {
            mCurY += mHeight;
        } else if (mCurY >= mHeight) {
            mCurY -= mHeight;
        }

        mLayoutParams.leftMargin = (int) mCurX;
        mLayoutParams.topMargin = (int) mCurY;
        mView.setLayoutParams(mLayoutParams);

        sendEmptyMessageDelayed(1, 10);
    }
}
