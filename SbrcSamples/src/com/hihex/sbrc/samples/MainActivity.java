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

public class MainActivity extends BaseActivity {
    private SbrcManager mSbrcManager;

    EditText editText1, editText2;
    Activity self;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.main);

        final MyApplication app = (MyApplication) getApplication();
        mSbrcManager = app.getSbrcManager();

        initGame();
    }

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
        // Let the clients know our game.
        editText1 = (EditText) this.findViewById(R.id.editText1);
        editText2 = (EditText) this.findViewById(R.id.editText2);
        RemoteTextInputMonitor.attach(mSbrcManager, editText1);
        RemoteTextInputMonitor.attach(mSbrcManager, editText2);

        final Button button = (Button) findViewById(R.id.button_ok);
        button.requestFocus();
    }

    public void onClickButton(final View v) {
        if (v instanceof Button) {
            Toast.makeText(this, ((Button) v).getText(), Toast.LENGTH_SHORT)
            .show();
        }
        if (v.getId() == R.id.button_ok) {
            final Intent intent = new Intent(this, SecondaryActivity.class);
            startActivity(intent);
        }
    }

}