package com.example.user.termproject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017-11-20.
 */

public class MarginActivity extends AppCompatActivity {

    SQLiteDatabase db;

    SupportMapFragment mapFragment;

    MarkerOptions marker;
    MarkerOptions[] markers;

    double diffLatitude, diffLongitude;

    double minLatitude, maxLatitude;
    double minLongitude, maxLongitude;

    int itemNum;

    String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_margin);
    }

    @Override
    protected void onResume() {
        super.onResume();

        long minTime = 1000;
        float minDistance = 0;

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        showCurrentLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                }
        );
    }

    public void showCurrentLocation(final Location location) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapMargin);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

                marker = new MarkerOptions();
                marker.position(new LatLng(location.getLatitude(), location.getLongitude()));
                marker.title("현재 위치");
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
                googleMap.addMarker(marker).showInfoWindow();

                diffLatitude = LatitudeInDifference(30000);
                diffLongitude = LongitudeInDifference(location.getLatitude(), 30000);

                DecimalFormat form = new DecimalFormat("#.##########");

                minLatitude = Double.parseDouble(form.format(location.getLatitude() - diffLatitude));
                maxLatitude = Double.parseDouble(form.format(location.getLatitude() + diffLatitude));
                minLongitude = Double.parseDouble(form.format(location.getLongitude() - diffLongitude));
                maxLongitude = Double.parseDouble(form.format(location.getLongitude() + diffLongitude));

                CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
                googleMap.animateCamera(zoom);

                findMargin(googleMap);
            }
        });
    }

    public void setMarker(JSONArray jsonArray, GoogleMap googleMap) {
        itemNum = jsonArray.length();

        markers = new MarkerOptions[itemNum];

        for (int i = 0; i < itemNum; i++) {
            markers[i] = new MarkerOptions();

            try {
                markers[i].position(new LatLng(Double.valueOf(jsonArray.getJSONObject(i).getString("latitude")), Double.valueOf(jsonArray.getJSONObject(i).getString("longitude"))));
                markers[i].title(jsonArray.getJSONObject(i).getString("name"));
                markers[i].snippet(jsonArray.getJSONObject(i).getString("activity"));
                markers[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));

                googleMap.addMarker(markers[i]).showInfoWindow();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (!marker.getTitle().equals("현재 위치")) {
                    if (marker.getTitle().equals(key)) {
                        Intent myIntent = new Intent(getApplicationContext(), TownActivity.class);
                        myIntent.putExtra("TOWN_NAME", marker.getTitle());

                        startActivity(myIntent);
                    }
                }
                key = marker.getTitle();

                return false;
            }
        });
    }

    // 반경 m이내의 위도 차(degree)
    public double LatitudeInDifference(int diff) {
        final int earth = 6371000;  // 지구 반지름, 단위 : m

        return (diff * 360.0) / (2 * Math.PI * earth);
    }

    public double LongitudeInDifference(double _latitude, int diff) {
        final int earch = 6371000;  // 지구 반지름, 단위 : m

        return (diff * 360.0) / (2 * Math.PI * earch * Math.cos(Math.toRadians(_latitude)));
    }

    private void findMargin(final GoogleMap googleMap) {

        final String tag_string_req = "req_margin";

        String requestUrl = Splashscreen.url + "margin";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    setMarker(jsonArray, googleMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MIN_LATITUDE", String.valueOf(minLatitude));
                params.put("MAX_LATITUDE", String.valueOf(maxLatitude));
                params.put("MIN_LONGITUDE", String.valueOf(minLongitude));
                params.put("MAX_LONGITUDE", String.valueOf(maxLongitude));

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
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