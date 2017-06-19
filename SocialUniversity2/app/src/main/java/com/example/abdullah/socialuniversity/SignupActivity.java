package com.example.abdullah.socialuniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
public class SignupActivity extends AppCompatActivity{
    private static final String TAG = "SignupActivity";
    private static final String URL_FOR_REGISTRATION = "http://abdullahcelik.com.tr/social_university/register.php";
    ProgressDialog progressDialog;

    private EditText nameEt, emailEt, passwordEt, retrypasswordEt;
    private TextView loginTxt;
    private Button signupBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEt = (EditText) findViewById(R.id.input_name);
        emailEt = (EditText) findViewById(R.id.input_email);
        passwordEt = (EditText) findViewById(R.id.input_password);
        retrypasswordEt = (EditText) findViewById(R.id.input_reEnterPassword);
        loginTxt = (TextView) findViewById(R.id.link_login);
        signupBtn = (Button) findViewById(R.id.btn_signup);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean regex = signup();
                if(regex){
                    //hideDialog();
                    submitForm();
                }else{
                    //hideDialog();
                    Toast.makeText(SignupActivity.this, "Bilgileri kontrol edip tekrar deneyin!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
    public boolean signup() {
        Log.d(TAG, "Signup");
        boolean valid  = validate();
        if (!validate()) {
            //onSignupFailed();
        }

        //_signupButton.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/

        return valid;
    }


    /*public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }*/

   /* public void onSignupFailed() {
        _signupButton.setEnabled(true);
    }*/

    public boolean validate() {
        boolean valid = true;

        String name = nameEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String reEnterPassword = retrypasswordEt.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameEt.setError("at least 3 characters");
            valid = false;
        } else {
            nameEt.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("enter a valid email address");
            valid = false;
        } else {
            emailEt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEt.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            retrypasswordEt.setError("Password Do not match");
            valid = false;
        } else {
            retrypasswordEt.setError(null);
        }

        return valid;
    }

    private void submitForm() {
        String deviceID = DeviceIdCek();
        registerUser(nameEt.getText().toString(),
                emailEt.getText().toString(),
                passwordEt.getText().toString(),deviceID);
    }

    private void registerUser(final String name, final String email, final String password,final String deviceID) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Kadın Başarı ile tamamlandı : " + user, Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                SignupActivity.this,
                                LoginActivity.class);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("mail", email);
                params.put("password", password);
                params.put("device",deviceID);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public String DeviceIdCek(){
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        return deviceId;
    }
}
