package com.example.user.termproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017-11-20.
 */

public class ListViewActivity extends AppCompatActivity {

    String REQUEST_CODE;

    Button btnSearch;
    ListView listView;
    TextView txtSelect;
    Spinner spnSelect1, spnSelect2;

    ArrayAdapter spnAdapter1, spnAdapter2;

    TownItem[] TownItems;
    ListViewAdapter adapter;

    String key1 = "전체보기", key2 = "전체보기";
    int itemNum;

    SQLiteDatabase db;

    private void set() {
        Intent intent = getIntent();
        REQUEST_CODE = intent.getStringExtra("REQUEST_CODE");

        if(REQUEST_CODE.equals("city")) {
            spnSelect1.setVisibility(View.VISIBLE);
            spnSelect2.setVisibility(View.VISIBLE);

            spnAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.state_spinner, R.layout.spinner_item);
            setSpinner();
        }
        else if(REQUEST_CODE.equals("activity")) {
            spnSelect1.setVisibility(View.VISIBLE);

            spnAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.program_spinner, R.layout.spinner_item);

            spnSelect2.setVisibility(View.GONE);

            setSpinner();
        }
    }

    private void activityRequest() {
        final String tag_string_req = "req_activity_search";

        String requestUrl = Splashscreen.url + "activitySearch";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray json_receiver = new JSONArray(response);

                    setListView(json_receiver);
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
            protected Map<String, String> getParams() { // Server에 보내는 Data를 지정
                Map<String, String> params = new HashMap<String, String>();
                params.put("ACTIVITY", key1);

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 Add
    }

    private void cityRequest() {
            final String tag_string_req = "req_city_search";

            String requestUrl = Splashscreen.url + "citySearch";

            StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray json_receiver = new JSONArray(response);

                        setListView(json_receiver);
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
            protected Map<String, String> getParams() { // Server에 보내는 Data를 지정
                Map<String, String> params = new HashMap<String, String>();
                params.put("STATE", key1);
                params.put("CITY", key2);

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 Add
    }

    private void setSpinner() {
        spnSelect1.setAdapter(spnAdapter1);

        spnSelect1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                key1 = String.valueOf(parent.getItemAtPosition(position));

                if(REQUEST_CODE.equals("city"))
                    setCitySpinner(key1);
                else if(REQUEST_CODE.equals("activity"))
                    txtSelect.setText(key1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCitySpinner(String key) {
        if(key1.equals("전체보기")) {
            txtSelect.setText("전체보기");
        }
        else {
            spnSelect2.setVisibility(View.VISIBLE);

            if(key.equals("서울특별시"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.seoul_spinner, R.layout.spinner_item);
            else if(key.equals("대전광역시"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.daejeon_spinner, R.layout.spinner_item);
            else if(key.equals("대구광역시"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.daegu_spinner, R.layout.spinner_item);
            else if(key.equals("울산광역시"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ulsan_spinner, R.layout.spinner_item);
            else if(key.equals("부산광역시"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.busan_spinner, R.layout.spinner_item);
            else if(key.equals("광주광역시"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gwangju_spinner, R.layout.spinner_item);
            else if(key.equals("세종특별자치시"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sejong_spinner, R.layout.spinner_item);
            else if(key.equals("경기도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeonggi_spinner, R.layout.spinner_item);
            else if(key.equals("강원도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gangwon_spinner, R.layout.spinner_item);
            else if(key.equals("충청남도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.chungnam_spinner, R.layout.spinner_item);
            else if(key.equals("충청북도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.chungbuk_spinner, R.layout.spinner_item);
            else if(key.equals("경상북도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeongbuk_spinner, R.layout.spinner_item);
            else if(key.equals("경상남도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gyeongnam_spinner, R.layout.spinner_item);
            else if(key.equals("전라북도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeonbuk_spinner, R.layout.spinner_item);
            else if(key.equals("전라남도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeonnam_spinner, R.layout.spinner_item);
            else if(key.equals("제주특별자치도"))
                spnAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.jeju_spinner, R.layout.spinner_item);

            spnSelect2.setAdapter(spnAdapter2);

            spnSelect2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    key2 = String.valueOf(parent.getItemAtPosition(position));

                    txtSelect.setText(key1 + " " + key2);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = (ListView) findViewById(R.id.listView);
        spnSelect1 = (Spinner) findViewById(R.id.spnSelect1);
        spnSelect2 = (Spinner) findViewById(R.id.spnSelect2);
        txtSelect = (TextView) findViewById(R.id.txtSelect);

        set();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(REQUEST_CODE.equals("city")) {
                    cityRequest();
                }
                else if(REQUEST_CODE.equals("activity")) {
                    activityRequest();
                }

            }
        });

    }

    class ListViewAdapter extends BaseAdapter {
        ArrayList<TownItem> items = new ArrayList<TownItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        private void addItem(TownItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TownItemView view = new TownItemView(getApplicationContext());

            TownItem item = items.get(position);
            view.setName(item.getName());
            view.setCity(item.getState(), item.getCity());
            view.setActivity(item.getActivity());

            return view;
        }
    }

    private void setListView(JSONArray jsonArray) {

        itemNum = jsonArray.length();

        adapter = new ListViewAdapter();
        TownItems = new TownItem[itemNum];

        for(int i = 0; i < itemNum; i++) {
            try {
                TownItems[i] = new TownItem(jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getString("state"), jsonArray.getJSONObject(i).getString("city"),
                        jsonArray.getJSONObject(i).getString("activity"));

                adapter.addItem(TownItems[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String townName = TownItems[position].getName();

                Intent myIntent = new Intent(getApplicationContext(), TownActivity.class);

                myIntent.putExtra("TOWN_NAME", townName);

                startActivity(myIntent);
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
