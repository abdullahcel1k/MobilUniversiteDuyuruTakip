package com.example.abdullah.socialuniversity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by abdullah on 05.05.2017.
 */

public class UniversityAllFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "UserActivityFollows";
    String url = "http://abdullahcelik.com.tr/social_university/universityalllist.php";
    //Context context;
    List<Universities> universitiesList = new ArrayList<>();
    MyAdapter myAdapter;
    ListView listView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // rootview boş ise doldur
        if (rootView == null) {

            rootView = inflater.inflate(R.layout.activity_univeristylist, container, false);
            listeyiDoldur(url, rootView);
            listView = (ListView) rootView.findViewById(R.id.mylist);
            myAdapter= new MyAdapter((Activity) rootView.getContext(), universitiesList, rootView.getContext());
            listView.setOnItemClickListener(this);
            // Initialise your layout here

        } else {
            // bu koşulla bişi eklemiyoruz diğer şekil listeye ekleme yapıyordu
            // buraya daha sonra takip edilen üniversite sayısı çekilip değişmesi durumunda root view
            // değiştirme işlemleri eklencek
            rootView = inflater.inflate(R.layout.activity_univeristylist, container, false);
            listeyiDoldur(url, rootView);
            listView = (ListView) rootView.findViewById(R.id.mylist);
            myAdapter= new MyAdapter((Activity) rootView.getContext(), universitiesList, rootView.getContext());
            listView.setOnItemClickListener(this);
        }


        return rootView;
    }

    private void listeyiDoldur(String url, final View view) {
        Log.i("Giriş yaptımı","Evet");
        String cancel_req_tag = "login";
        if(universitiesList == null){
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
                    Toast.makeText(view.getContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to login url
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

            };
            // Adding request to request queue
            AppSingleton.getInstance(view.getContext()).addToRequestQueue(strReq,cancel_req_tag);
        }else{
            universitiesList.clear();
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
                    Toast.makeText(view.getContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to login url
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

            };
            // Adding request to request queue
            AppSingleton.getInstance(view.getContext()).addToRequestQueue(strReq,cancel_req_tag);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dataPosition = String.valueOf(position);
        Log.d("dataposition ",dataPosition);
        Intent i = new Intent(view.getContext(), UniversityWebActivity.class);
        i.putExtra("dataposition", universitiesList.get(position));
        startActivity(i);
    }
}
