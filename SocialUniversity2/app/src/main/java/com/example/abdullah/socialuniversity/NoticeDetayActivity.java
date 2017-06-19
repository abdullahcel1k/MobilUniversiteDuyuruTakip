package com.example.abdullah.socialuniversity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * Created by abdullah on 19.05.2017.
 */

public class NoticeDetayActivity extends AppCompatActivity {
    String FOLLOWINGURL = "http://abdullahcelik.com.tr/social_university/following.php";
    String FOLLOWCONTROL = "http://abdullahcelik.com.tr/social_university/followcontrol.php";

    private static String TAG = "UniversityWebActivity";
    Notices notices;
    WebView webView;
    SharedPreferences preferences;
    ImageView imageView;
    CheckBox cboxFollow;
    String usermail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticedetay);

        notices = (Notices) getIntent().getSerializableExtra("dataposition");



        String url = notices.getNoticeUrl();
        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Desktop");
        webView.setWebViewClient(new NoticeDetayActivity.Callback());
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }
    private class Callback extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
