package com.example.abdullah.socialuniversity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abdullah on 3/7/17.
 */
public class UserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "UserActivity";
    String url = "http://abdullahcelik.com.tr/social_university/followlist.php";
    Context context;
    List<Universities> universitiesList = new ArrayList<>();
    SharedPreferences preferences;
    String mail;
    MyAdapter myAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_univeristylist);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d("preferencesdeki mail", preferences.getString("usermail",null));
        listeyiDoldur(url);
        listView = (ListView) findViewById(R.id.mylist);
        myAdapter= new MyAdapter(UserActivity.this, universitiesList,getApplicationContext());
        listView.setOnItemClickListener(this);
    }

    private void listeyiDoldur(String url) {
        Log.i("Giriş yaptımı","Evet");
        String cancel_req_tag = "login";
        mail =preferences.getString("usermail", null);

        /*
        * json u array olarak karşılayınca mevcut metot ile post parametre gönderilemiyordu
        * bu sebepten parametreyi strinrequest olarak gönderdik karşıldağımız response'u
        * JSONArray tipine çevirdik ve bu şekilde gelen listemizi kullandık
        * */
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    // gelen jsondan error satırının değerini alıyor ve gelen değer karşılaştırarak diğer sayfayı açıyo
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d(TAG,response.toString());
                    Log.d(TAG,"Len "+response.length());
                    // Parsing json
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            Universities universities = new Universities();
                            universities.setUniversityName(obj.getString("universityName"));
                            universities.setUniversityWeb(obj.getString("universityWeb"));
                            universities.setUniversityLogo(obj.getString("universityLogo"));

                            universitiesList.add(universities);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    listView.setAdapter(myAdapter);

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
                params.put("mail", mail);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l ) {
        String dataPosition = String.valueOf(position);
        Log.d("dataposition ",dataPosition);
        Intent i = new Intent(getApplication(), UniversityWebActivity.class);
        i.putExtra("dataposition", universitiesList.get(position));
        startActivity(i);
    }
}
