package com.hnweb.amk8ptaapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnweb.amk8ptaapp.contants.AppConstant;
import com.hnweb.amk8ptaapp.fragment.ChatFragment;
import com.hnweb.amk8ptaapp.fragment.EventsFragment;
import com.hnweb.amk8ptaapp.fragment.HomeFragment;
import com.hnweb.amk8ptaapp.R;
import com.hnweb.amk8ptaapp.utils.SharedPreference;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private View navHeader;
    SharedPreferences pref;
    String userFirstName, userLastName;
    ImageView imageViewClose;
    TextView tv_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getApplicationContext().getSharedPreferences("AOP_PREFS", MODE_PRIVATE);
        userFirstName = pref.getString(AppConstant.KEY_FIRSTNAME, null);
        userLastName = pref.getString(AppConstant.KEY_LASTNAME, null);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);

        imageViewClose = navHeader.findViewById(R.id.imageView_close);
        tv_user_name = navHeader.findViewById(R.id.tv_user_name);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);

            }
        });
        tv_user_name.setText(userFirstName + " " + userLastName);
        if (savedInstanceState == null) {

            Fragment fragment = null;
            fragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.frame_layout, fragment);
            transaction.commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.navigation_messges) {
                fragment = new HomeFragment();
            } else if (id == R.id.navigation_events) {
                fragment = new EventsFragment();
            } else if (id == R.id.navigation_chat) {
                fragment = new ChatFragment();
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
            }

            return true;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_logout) {
            showLogoutAlert();
        } else if (id == R.id.nav_message) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_events) {
            fragment = new EventsFragment();
        } else if (id == R.id.nav_chat) {
            fragment = new ChatFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Logout Successfully");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                pref.edit().clear().apply();
                showLogoutAlert();
                dialog.cancel();
                Intent in = new Intent(HomeActivity.this, LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1) {
                finish();
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                    //getFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                } else {
                    super.onBackPressed();
                }
            }
        }
    }
}
