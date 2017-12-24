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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    Button btnReview;

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

        btnReview = (Button) findViewById(R.id.btnReview);

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

        findTown();

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

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ReviewActivity.class);
                myIntent.putExtra("TOWN_NAME", townName);
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

    private void findTown() {
        final String tag_string_req = "req_town_information";

        String requestUrl = Splashscreen.url + "townInformation";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {  // Server가 Data를 보내면 응답하는 Method
                
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    state = json_receiver.getString("STATE");
                    city = json_receiver.getString("CITY");
                    program = json_receiver.getString("PROGRAM");
                    activity = json_receiver.getString("ACTIVITY");
                    facility = json_receiver.getString("FACILITY");
                    address = json_receiver.getString("ADDRESS");
                    president = json_receiver.getString("PRESIDENT");
                    callNumber = json_receiver.getString("CALL_NUMBER");
                    homePage = json_receiver.getString("HOMEPAGE");
                    management = json_receiver.getString("MANAGEMENT");
                    latitude = Double.valueOf(json_receiver.getString("LATITUDE"));
                    longitude = Double.valueOf(json_receiver.getString("LONGITUDE"));

                    setWidget();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {    // Error가 발생하는 경우
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() { // Server에 보내는 Data를 지정
                Map<String, String> params = new HashMap<String, String>();
                params.put("NAME", townName);

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 Add
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
                    try {
                        db.execSQL(sql);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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