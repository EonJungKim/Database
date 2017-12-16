package com.example.user.termproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by user on 2017-11-27.
 */

public class TownActivity extends AppCompatActivity {

    TextView txtTownName;
    TextView txtTownCity, txtTownAddress;
    TextView txtTownProgram, txtTownActivity, txtTownFacility;
    TextView txtTownPresident, txtTownCall, txtTownHomePage;
    TextView txtTownManagement;

    Button btnTownCall, btnTownHomePage;

    String townName;
    String state, city, address;
    String program, activity, facility;
    String president, callNumber, homePage;
    String management;
    Double latitude, longitude;

    SupportMapFragment mapFragment;

    MarkerOptions marker;

    SQLiteDatabase db;

    private void initWidget() {
        txtTownName = (TextView) findViewById(R.id.txtTownName);
        txtTownCity = (TextView) findViewById(R.id.txtTownCity);
        txtTownAddress = (TextView) findViewById(R.id.txtTownAddress);
        txtTownProgram = (TextView) findViewById(R.id.txtTownProgram);
        txtTownActivity = (TextView) findViewById(R.id.txtTownActivity);
        txtTownFacility = (TextView) findViewById(R.id.txtTownFacility);
        txtTownPresident = (TextView) findViewById(R.id.txtTownPresident);
        txtTownCall = (TextView) findViewById(R.id.txtTownCall);
        txtTownHomePage = (TextView) findViewById(R.id.txtTownHomePage);
        txtTownManagement = (TextView) findViewById(R.id.txtTownManagement);

        txtTownHomePage.setVisibility(View.VISIBLE);

        btnTownCall = (Button) findViewById(R.id.btnTownCall);
        btnTownHomePage = (Button) findViewById(R.id.btnTownHomePage);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        townName = intent.getStringExtra("TOWN_NAME");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town);

        initWidget();

        getIntentData();

        // 여기에서 Server로 요청

        setWidget();

        btnTownCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + callNumber));
                startActivity(myIntent);
            }
        });

        btnTownHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homePage));
                startActivity(myIntent);
            }
        });
    }

    private void setWidget() {
        txtTownName.setText(townName);
        txtTownCity.setText(state + " " + city);
        txtTownAddress.setText(address);
        txtTownProgram.setText(program);
        txtTownActivity.setText(activity);
        txtTownFacility.setText(facility);
        txtTownPresident.setText(president);
        txtTownCall.setText(callNumber);
        txtTownHomePage.setText(homePage);
        txtTownManagement.setText(management);

        if(homePage.equals(""))
            btnTownHomePage.setVisibility(View.INVISIBLE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));

                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                googleMap.animateCamera(zoom);

                marker = new MarkerOptions();

                marker.position(new LatLng(latitude, longitude));
                marker.title(townName);
                marker.snippet(address);
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));

                googleMap.addMarker(marker).showInfoWindow();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        Intent myIntent;

        switch (curId) {
            case R.id.menu_logout:
                db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

                String sql = "drop table user;";

                if(db != null)
                    db.execSQL(sql);

                myIntent = new Intent(getApplicationContext(), LogInActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);

                break;

            case R.id.menu_user_info:
                myIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
                startActivity(myIntent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}