package com.hnweb.amk8ptaapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hnweb.amk8ptaapp.R;
import com.hnweb.amk8ptaapp.contants.AppConstant;
import com.hnweb.amk8ptaapp.utils.ConnectionDetector;
import com.hnweb.amk8ptaapp.utils.PermissionUtility;

import java.util.ArrayList;

/**
 * Created by Priyanka H on 05/07/2018.
 */
public class MainActivity extends AppCompatActivity {
    Button btn_getStarted;
    SharedPreferences pref;
    String useridUser;
    ConnectionDetector connectionDetector;
    private PermissionUtility putility;
    ArrayList<String> permission_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionDetector = new ConnectionDetector(MainActivity.this);
        runTimePermission();
        pref = getApplicationContext().getSharedPreferences("AOP_PREFS", MODE_PRIVATE);
        useridUser = pref.getString(AppConstant.KEY_ID, null);
        btn_getStarted = (Button) findViewById(R.id.btn_getStart);
        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet()) {
                    if (useridUser == null || useridUser.equals("")) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Intent intentLogin = new Intent(MainActivity.this, HomeActivity.class);
                        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentLogin);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection, Please try Again!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void runTimePermission() {
        putility = new PermissionUtility(this);
        permission_list = new ArrayList<String>();
        permission_list.add(Manifest.permission.INTERNET);
        permission_list.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permission_list.add(Manifest.permission.WAKE_LOCK);
        permission_list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permission_list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permission_list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permission_list.add(Manifest.permission.CAMERA);
        putility.setListner(new PermissionUtility.OnPermissionCallback() {
            @Override
            public void OnComplete(boolean is_granted) {
                Log.i("OnPermissionCallback", "is_granted = " + is_granted);
                if (is_granted) {

                } else {
                    putility.checkPermission(permission_list);
                }
            }
        });
        putility.checkPermission(permission_list);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (putility != null) {
            putility.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
