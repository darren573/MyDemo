package com.darren.mydemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.darren.mydemo.R;
import com.darren.mydemo.bean.ResultPicture;

public class PictureActivity extends AppCompatActivity {
    private WebView wv_pic;
    private Intent intent;
    private ResultPicture resultPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        findViews();
        setEvent();
    }

    private void setEvent() {
        intent=getIntent();
        resultPicture=intent.getExtras().getParcelable("picture");
        wv_pic.loadUrl(resultPicture.getPicUrl());
        WebSettings setting = wv_pic.getSettings();
        setting.setJavaScriptEnabled(true);
        wv_pic.setWebViewClient(new WebViewClient());
        wv_pic.setWebChromeClient(new WebChromeClient());
    }
    private void findViews() {
        wv_pic= (WebView) findViewById(R.id.wv_pic);
    }
}
