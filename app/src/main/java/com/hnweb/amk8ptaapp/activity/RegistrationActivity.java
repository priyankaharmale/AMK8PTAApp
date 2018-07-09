package com.hnweb.amk8ptaapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.amk8ptaapp.R;
import com.hnweb.amk8ptaapp.contants.AppConstant;
import com.hnweb.amk8ptaapp.utils.AlertUtility;
import com.hnweb.amk8ptaapp.utils.AppUtils;
import com.hnweb.amk8ptaapp.utils.LoadingDialog;
import com.hnweb.amk8ptaapp.utils.SharedPreference;
import com.hnweb.amk8ptaapp.utils.Utils;
import com.hnweb.amk8ptaapp.utils.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Priyanka H on 05/07/2018.
 */
public class RegistrationActivity extends AppCompatActivity {
    Button btn_proceed;
    EditText et_firstname, et_lastname, et_email, et_mobile, et_password, et_confrimpassword;
    String camImage = "";
    TextView tv_login;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_proceed = (Button) findViewById(R.id.btn_register);
        et_firstname = (EditText) findViewById(R.id.et_firstName);
        et_lastname = (EditText) findViewById(R.id.et_lastName);
        et_email = (EditText) findViewById(R.id.et_email);
        et_mobile = (EditText) findViewById(R.id.et_mobileNo);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confrimpassword = (EditText) findViewById(R.id.et_confirmpassword);

        loadingDialog = new LoadingDialog(RegistrationActivity.this);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {

                    if (Utils.isNetworkAvailable(RegistrationActivity.this)) {
                        register();
                    } else {
                        Utils.myToast1(RegistrationActivity.this);
                    }
                }

            }
        });
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validations.hasText(et_firstname, "Please Enter First Name"))
            ret = false;
        if (!Validations.hasText(et_lastname, "Please Enter Last Name"))
            ret = false;
        if (!Validations.hasText(et_mobile, "Please Enter the Mobile No."))
            ret = false;
        if (!Validations.hasText(et_email, "Please Enter Email ID "))
            ret = false;
        if (!Validations.hasText(et_password, "Please Enter Password"))
            ret = false;
        if (!Validations.hasText(et_confrimpassword, "Please Enter Confirm Password"))
            ret = false;
        if (!Validations.isEmailAddress(et_email, true, "Please Enter Valid Email ID"))
            ret = false;
        if (!Validations.check_text_length_7_text_layout(et_password, "Password atleast 7 characters"))
            ret = false;
        if (!Validations.check_text_length_7_text_layout(et_confrimpassword, "Password atleast 7 characters"))
            ret = false;

        return ret;
    }


    private void register() {
        final String password = et_password.getText().toString();
        final String email = et_email.getText().toString();
        final String phoneNo = et_mobile.getText().toString();
        final String firstname = et_firstname.getText().toString();
        final String lastname = et_lastname.getText().toString();
        loadingDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.API_REGISTER_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res_register" + response);
                        try {
                            final JSONObject j = new JSONObject(response);
                            int message_code = j.getInt("message_code");
                            String message = j.getString("message");
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            if (message_code == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (!et_password.getText().toString().equals(et_confrimpassword.getText().toString())) {
                                                    Toast.makeText(RegistrationActivity.this, "Password Not matching ", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                message = j.getString("message");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String reason = AppUtils.getVolleyError(RegistrationActivity.this, error);
                        AlertUtility.showAlert(RegistrationActivity.this, reason);
                        System.out.println("jsonexeption" + error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put(AppConstant.KEY_EMAIL, email);
                    params.put(AppConstant.KEY_FIRSTNAME, firstname);
                    params.put(AppConstant.KEY_LASTNAME, lastname);
                    params.put(AppConstant.KEY_MOBILENO, phoneNo);
                    params.put(AppConstant.KEY_PASSWORD, password);

                } catch (Exception e) {
                    System.out.println("error" + e.toString());
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
