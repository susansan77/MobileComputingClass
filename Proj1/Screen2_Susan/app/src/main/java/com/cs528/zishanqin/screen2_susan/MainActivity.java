package com.cs528.zishanqin.screen2_susan;

import android.app.ListActivity;
import android.os.Bundle;

public class MainActivity extends ListActivity {
    String[] values = {"icon", "selfie", "friend", "selfie", "friend",
            "selfie", "friend", "selfie", "friend", "selfie", "friend", "selfie",
            "friend", "selfie", "friend", "selfie", "friend", "selfie", "friend", "selfie", "friend",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //listView=(ListView) findViewById(R.id.listView);
        setListAdapter(new MobileArrayAdapter(this, values));
    }


}
