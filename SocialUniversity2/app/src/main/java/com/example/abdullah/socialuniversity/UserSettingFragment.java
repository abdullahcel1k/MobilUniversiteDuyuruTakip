package com.example.abdullah.socialuniversity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abdullah on 16.05.2017.
 */

public class UserSettingFragment extends Fragment {
    private static final String TAG = "UserSettingFragment";

    private Button btnCikis, btnRefreshPassword;
    private EditText mailET, passwordET, retryPasswordET, newPasswordET;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String URL = "http://abdullahcelik.com.tr/social_university/refreshpassword.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_usersetting, container, false);

        mailET = (EditText) view.findViewById(R.id.input_email);
        passwordET = (EditText) view.findViewById(R.id.input_password);
        retryPasswordET = (EditText) view.findViewById(R.id.input_reEnterPassword);
        newPasswordET = (EditText) view.findViewById(R.id.input_newpassword);

        btnCikis = (Button) view.findViewById(R.id.btn_close);
        btnRefreshPassword = (Button) view.findViewById(R.id.btn_refreshpass);

        preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(view.getContext());
        editor = preferences.edit();

        btnCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("username");
                editor.remove("usermail");
                editor.remove("userlogin");
                editor.commit();

                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRefreshPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean regex = validate();
                if (regex){
                    updatePassword(URL,view,mailET.getText().toString(),passwordET.getText().toString(),newPasswordET.getText().toString());
                }else{

                }
            }
        });
        return view;
    }

    public boolean validate() {
        boolean valid = true;

        String email = mailET.getText().toString();
        String password = passwordET.getText().toString();
        String reEnterPassword = retryPasswordET.getText().toString();
        String newPassword = newPasswordET.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mailET.setError("Mail adresiniz e-mail standartlarında değil!");
            valid = false;
        } else {
            mailET.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordET.setError("Şifreniz 4 karakterden küçük 10 karakterden büyük olamaz!");
            valid = false;
        } else {
            passwordET.setError(null);
        }


        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            retryPasswordET.setError("Parola eşleşmedi!");
            valid = false;
        } else {
            retryPasswordET.setError(null);
        }

        if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
            newPasswordET.setError("Şifreniz 4 karakterden küçük 10 karakterden büyük olamaz!");
            valid = false;
        } else {
            newPasswordET.setError(null);
        }
        return valid;
    }

    // kullanıcı mail , eskişifre ve yeni şifre bilgilerini alarak şifresini volley kütühanesi ile
    // değiştirdiğimiz metod
    private void updatePassword(final String url, final View view,final String mail, final String oldPassword, final String newPassword) {
        Log.i("Giriş yaptımı", "Evet");
        String cancel_req_tag = "login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    // gelen jsondan error satırının değerini alıyor ve gelen değer karşılaştırarak diğer sayfayı açıyo
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, response.toString());
                    Log.d(TAG, "Len " + response.length());
                    // Parsing json
                    boolean error =jsonObject.getBoolean("error");

                    if(!error){
                        Toast.makeText(view.getContext(),"Şifreniz Güncellendi!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(view.getContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
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
                params.put("mail", mail);
                params.put("oldpassword",oldPassword);
                params.put("newpassword",newPassword);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(view.getContext()).addToRequestQueue(strReq, cancel_req_tag);

    }
}