package com.example.abdullah.socialuniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abdullah on 3/7/17.
 */

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "http://abdullahcelik.com.tr/social_university/login.php";
    ProgressDialog progressDialog;


    private EditText emailTxt, passwordTxt;
    private Button loginBtn;
    private TextView signupTxt;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailTxt = (EditText) findViewById(R.id.input_email);
        passwordTxt = (EditText) findViewById(R.id.input_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        signupTxt = (TextView) findViewById(R.id.link_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean regex = login();
                if (regex){
                    hideDialog();
                    loginUser(emailTxt.getText().toString(), passwordTxt.getText().toString());
                }else{
                    hideDialog();
                    Toast.makeText(LoginActivity.this, "Lütfen Bilgileri Kontrol Edip Tekrar Deneyin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kayıt activity sine yönlendirme
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public boolean login(){
        Log.d(TAG, "Login");
        boolean valid = validate();
        if (!validate()){
            //onLoginFailed();
        }
        return  valid;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    /*public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }*/

    /*public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }*/

    public boolean validate() {
        boolean valid = true;

        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("enter a valid email address");
            valid = false;
        } else {
            emailTxt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordTxt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordTxt.setError(null);
        }

        return valid;
    }


    private void loginUser( final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {
                    // gelen jsondan error satırının değerini alıyor ve gelen değer karşılaştırarak diğer sayfayı açıyo
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //Girilen parametlerin veritabanında karşılığı var ise
                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("name");

                        // kullanıcı bilgilerini localde sakldık bu sayede main activityde uygulama kayıt ekranı değil doğrudan kullanıcya özel sayfa açılacak
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("username", jObj.getJSONObject("user").getString("name"));
                        editor.putString("usermail", jObj.getJSONObject("user").getString("mail"));
                        editor.putBoolean("userlogin", true);

                        editor.commit();
                        // Launch User activity
                        Intent intent = new Intent(
                                LoginActivity.this,
                                MainActivity.class);
                        intent.putExtra("username", user);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail", email);
                params.put("password", password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}