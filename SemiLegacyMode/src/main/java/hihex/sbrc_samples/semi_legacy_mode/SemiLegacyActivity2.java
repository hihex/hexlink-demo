package hihex.sbrc_samples.semi_legacy_mode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import hihex.sbrc.SbrcManager;
import hihex.sbrc.android.RemoteTextInputMonitor;

public class SemiLegacyActivity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        final SemiLegacyApplication app = (SemiLegacyApplication) getApplication();
        final SbrcManager manager = app.getSbrcManager();

        // We need to register the manager to each EditText manually. This is unfortunate, but we cannot avoid it —
        // automatic registration is only possible by creating an InputMethodService (an IME), which a third-party app
        // like this shouldn't provide.
        RemoteTextInputMonitor.attach(manager, (EditText) findViewById(R.id.editText));
        RemoteTextInputMonitor.attach(manager, (EditText) findViewById(R.id.multilineText));
    }

    public final void startActivity1(final View v) {
        final Intent intent = new Intent(this, SemiLegacyActivity1.class);
        startActivity(intent);
    }
}
