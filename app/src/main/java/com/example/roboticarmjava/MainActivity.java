package com.example.roboticarmjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

//johnny
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import com.example.roboticarmjava.Fragments.DeviceScanFragment;
import com.example.roboticarmjava.Fragments.inp_out_orientation;
import com.google.android.material.navigation.NavigationView;

import android.Manifest;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public FragmentManager fragmentManager;
    private CharSequence mTitle;
    static public BluetoothLeService mBluetoothLeService;

    String[] permissions = {Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    static public String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothLeService = new BluetoothLeService(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getFragmentManager();
        mTitle = getTitle();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            mBluetoothLeService = new BluetoothLeService(this);
            startScanFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (id == R.id.bluetooth) {
            fragmentManager.beginTransaction().replace(R.id.container, new DeviceScanFragment()).addToBackStack(null).commit();
        }

        if (id == R.id.orientation_in) {
                fragmentManager.beginTransaction().replace(R.id.container,new inp_out_orientation()).addToBackStack(null).commit();
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mTitle);
        return true;
    }
    public void cancelConnectDialogue() {
        mBluetoothLeService.scanBluetooth(false);
        mBluetoothLeService.terminateConnection();
        finish();
        restart();
    }

    public void restart() {
        Intent intent = new Intent(this, this.getClass());
        startActivity(intent);
    }

    public void requestPermission(){
        final Context mainActivityContext = this.getApplicationContext();
        final Activity mainActivity = this;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if ((ContextCompat.checkSelfPermission(mainActivityContext, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(mainActivityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(mainActivityContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(mainActivityContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        )
        {
            builder.setTitle("Permission Required");
            builder.setMessage("Please grant Location access so this application can perform Bluetooth scanning.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    ActivityCompat.requestPermissions(mainActivity, permissions, 1);
                }
            });
            builder.show();
        }
    }

    public void startScanFragment(){
        fragmentManager.beginTransaction().replace(R.id.container, new DeviceScanFragment()).commit();
    }
}