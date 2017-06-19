package com.example.abdullah.socialuniversity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * follow control webden control verisini doğru çekiyor fakat boolean mesaj değişkenine yanlış atıyor
 * Created by abdullah on 15.04.2017.
 */
public class UniversityWebActivity extends AppCompatActivity {

    String FOLLOWINGURL = "http://abdullahcelik.com.tr/social_university/following.php";
    String FOLLOWCONTROL = "http://abdullahcelik.com.tr/social_university/followcontrol.php";

    private static String TAG = "UniversityWebActivity";
    Universities universities;
    WebView webView;
    SharedPreferences preferences;
    ImageView imageView;
    CheckBox cboxFollow;
    String usermail;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.university_web);

        preferences = PreferenceManager.getDefaultSharedPreferences(UniversityWebActivity.this);
        usermail = preferences.getString("usermail",null);
        universities = (Universities) getIntent().getSerializableExtra("dataposition");
        cboxFollow = (CheckBox) findViewById(R.id.cboxFollow);

        if(followControl(FOLLOWCONTROL,universities.getUniversityWeb(),usermail)){
            Toast.makeText(UniversityWebActivity.this,"followcontrol true geldi",Toast.LENGTH_LONG).show();
            cboxFollow.setChecked(true);
        }else{
            Toast.makeText(UniversityWebActivity.this,"followcontrol false geldi",Toast.LENGTH_LONG).show();
            cboxFollow.setChecked(false);
        }
        imageView = (ImageView) findViewById(R.id.universityWebLogo);
        Picasso.with(this).load(universities.getUniversityLogo()).into(imageView);



        cboxFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cboxFollow.isChecked()){
                    setFollowing(FOLLOWINGURL,universities.getUniversityWeb(),usermail);
                    Toast.makeText(getApplicationContext(),"Takibi bıraktın!",Toast.LENGTH_SHORT).show();
                }else{
                    setFollowing(FOLLOWINGURL,universities.getUniversityWeb(),usermail);
                    Toast.makeText(getApplicationContext(),"Takip ettin",Toast.LENGTH_SHORT).show();
                }

            }
        });

        String url = universities.getUniversityWeb();
        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Desktop");
        webView.setWebViewClient(new Callback());
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }

    private boolean followControl(String url, final String universityWeb, final String userMail){
        String cancel_req_tag = "universityFollowing";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    // gelen jsondan error satırının değerini alıyor ve gelen değer karşılaştırarak diğer sayfayı açıyo
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Girilen parametlerin veritabanında karşılığı var ise
                    if (!error) {
                        // mesajdan gelen veriyi kontrol edip o üniversiteyi takip ediyorsa checkbox yakacağız

                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*if (msg[0]){
                    Log.d("buraya girdimi","girdi");
                    mesaj = true;
                }else{
                    Log.d("buraya girdimi","girmedi");
                    mesaj = false;
                }*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userMail", userMail);
                params.put("universityWeb", universityWeb);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
        return Boolean.parseBoolean(null);
    }
    private void setFollowing(String url, final String universityWeb, final String userMail){
        String cancel_req_tag = "universityFollowing";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                try {
                    // gelen jsondan error satırının değerini alıyor ve gelen değer karşılaştırarak diğer sayfayı açıyo
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //Girilen parametlerin veritabanında karşılığı var ise
                    if (!error) {
                        String msg = jObj.getString("msg");
                        Toast.makeText(UniversityWebActivity.this,""+msg,Toast.LENGTH_LONG).show();
                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userMail", userMail);
                params.put("universityWeb", universityWeb);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }
    private class Callback extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }

}

