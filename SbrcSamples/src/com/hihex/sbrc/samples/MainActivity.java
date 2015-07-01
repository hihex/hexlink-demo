package com.hihex.sbrc.samples;

import hihex.sbrc.SbrcManager;
import hihex.sbrc.android.RemoteTextInputMonitor;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText editText1, editText2;
    Activity self;
    SbrcManager manager;
    MyApplication appState;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.main);
        appState = ((MyApplication) this.getApplication());
        appState.initActivity(self);
        // SBRC Specific: Initialize the server on start.
        initGame();
    }

    // SBRC Specific: Terminate the server on stop.
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initGame() {

        manager = appState.getSbrcManager();
        // Let the clients know our game.
        editText1 = (EditText) this.findViewById(R.id.editText1);
        editText2 = (EditText) this.findViewById(R.id.editText2);
        RemoteTextInputMonitor.attach(manager, editText1);
        RemoteTextInputMonitor.attach(manager, editText2);

        final Button button = (Button) findViewById(R.id.button_ok);
        button.requestFocus();
    }

    public void onClickButton(View v) {
        if (v instanceof Button) {
            Toast.makeText(this, ((Button) v).getText(), Toast.LENGTH_SHORT)
                    .show();
        }
        if (v.getId() == R.id.button_ok) {
            Intent intent = new Intent(this, SecondaryActivity.class);
            startActivity(intent);
        }
    }

}
