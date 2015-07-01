package com.hihex.sbrc.samples;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SecondaryActivity extends Activity{
    Activity self;
    MyApplication appState;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self=this;
        setContentView(R.layout.dummy_webview);

        initWebView();
    }
    private void initWebView(){

        WebView webview=(WebView) findViewById(R.id.dummyWebview);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webview.loadUrl("http://www.baidu.com");
    }
}
