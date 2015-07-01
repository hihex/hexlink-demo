package com.hihex.sbrc.samples.client;

import hihex.sbrc.DisconnectReason;
import hihex.sbrc.Identity;
import hihex.sbrc.Module;
import hihex.sbrc.StandardClient;
import hihex.sbrc.android.DirectionModule;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class DpadClient extends StandardClient {
    private final Activity mContext;

    public DpadClient(final Activity context) {
        super(/*rows*/1, /*columns*/1, /*isLandscape*/false);
        mContext = context;
        setModules(new Module[] {createDpadModule()});
    }

    private final Module createDpadModule() {
        return new DirectionModule(mContext) {
            @Override
            protected void onDpadEvent(final Command command) {
                // Set the actions to do when we receive a dpad event.
                // We update the text of the label.
                // can override kLongPress and other command here
                // then not call super.onDpadEvent()
                super.onDpadEvent(command);
                Log.e("SBRC", "onDpadEvent:"+command);
            }
        };
    }

    @Override
    public void onConnect(final Identity identity) {
        mContext.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                Toast.makeText(mContext, "device onConnect:"+identity, Toast.LENGTH_SHORT).show();                
            }
            
        });
        if(identity==null){
            return;
        }
        super.onConnect(identity);
    }

    @Override
    public void onDisconnect(final Identity identity, final DisconnectReason reason) {
        super.onDisconnect(identity, reason);
        mContext.runOnUiThread(new Runnable(){

            @Override
            public void run() {
                Toast.makeText(mContext, "device onConnect:"+identity, Toast.LENGTH_SHORT).show();                
            }
            
        });
    }
}
