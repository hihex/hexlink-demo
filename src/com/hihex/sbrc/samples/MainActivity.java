package com.hihex.sbrc.samples;

import hihex.sbrc.Client;
import hihex.sbrc.ClientFactory;
import hihex.sbrc.SbrcManager;
import hihex.sbrc.android.RemoteTextInputMonitor;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hihex.sbrc.samples.client.DpadClient;
//import hihex.sbrc.android.RemoteTextInputMonitor;

public class MainActivity extends Activity {
    EditText editText1,editText2;
    Activity self;
    SbrcManager manager;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self=this;
        setContentView(R.layout.main);

        // SBRC Specific: Initialize the server on start.
        initGame();
    }

    // SBRC Specific: Terminate the server on stop.
    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.destroy(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.resume();
    }

    private void initGame() {
        manager = SbrcManager.create(self);
        // Let the clients know our game.
        manager.setIcon(fetchGameIcon());

        manager.setClientFactory(new ClientFactory() {
            @Override
            public Client create() {
                return new DpadClient(MainActivity.this);
            }
        });
        editText1=(EditText) this.findViewById(R.id.editText1);
        editText2=(EditText) this.findViewById(R.id.editText2);
        RemoteTextInputMonitor.attach(manager, editText1);
        RemoteTextInputMonitor.attach(manager, editText2);

        final Button button=(Button) findViewById(R.id.button_ok);
        button.requestFocus();
    }

    public void onClickButton(View v) {
        if (v instanceof Button) {
            Toast.makeText(this, ((Button) v).getText(), Toast.LENGTH_SHORT).show();
        }
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
