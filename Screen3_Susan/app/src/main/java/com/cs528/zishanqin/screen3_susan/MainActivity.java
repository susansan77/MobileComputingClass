package com.cs528.zishanqin.screen3_susan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity {
    Button buttonGoogle;
    Button buttonAmazon;
    Button buttonApple;
    WebViewFragment fragment;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGoogle=(Button) findViewById(R.id.google);
        fragment=(WebViewFragment)getFragmentManager().findFragmentById(R.id.fragment);

        fragment.setUrl("https://www.google.com");
        buttonGoogle.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                fragment.setUrl("https://www.google.com");
            }
        });
        buttonAmazon=(Button) findViewById(R.id.amazon);
        buttonAmazon.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                fragment.setUrl("https://www.amazon.com");
            }
        });
        buttonApple=(Button) findViewById(R.id.apple);
        buttonApple.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                fragment.setUrl("https://www.apple.com");
            }
        });

    }

}
