package com.cs528.zishanqin.screen3_susan;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by zishanqin on 2/10/16.
 */
public class WebViewFragment extends Fragment {
    View rootView;
    WebView v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.webview, container, false);
        v=(WebView)rootView.findViewById(R.id.web);
        return rootView;
    }
public void setUrl(String s){
    v.loadUrl(s);
}

}
