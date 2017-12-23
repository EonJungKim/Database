package com.example.user.termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
 * Created by user on 2017-12-23.
 */

public class FavoriteListViewActivity extends AppCompatActivity {

    SQLiteDatabase db;

    ListView listView2;
    ListViewAdapter adapter2;

    TownItem[] TownItems;

    int itemNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list_view);

        listView2 = (ListView) findViewById(R.id.listView2);

        Intent intent = getIntent();
        String REQUEST_CODE = intent.getStringExtra("REQUEST_CODE");

        if(REQUEST_CODE.equals("favorite_state")) {
            favoriteStateRequest();
        }
        else if(REQUEST_CODE.equals("favorite_activity")) {
            favoriteActivityRequest();
        }

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

    private String findID(){

        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        String sql = "select ID from user";

        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToNext();

        String ID = cursor.getString(0);

        return ID;
    }

    private String findFavoriteState() {
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        String sql = "select favoriteState from user";

        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToNext();

        String favoriteState = cursor.getString(0);

        return favoriteState;
    }

    private String findFavoriteActivity() {
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        String sql = "select favoriteActivity from user";

        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToNext();

        String favoriteActivity = cursor.getString(0);

        return favoriteActivity;
    }

    private void favoriteActivityRequest() {
        final String tag_string_req = "req_favorite_activity";

        String requestUrl = Splashscreen.url + "favoriteActivity";

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
                Toast.makeText(getApplicationContext(), 123 + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() { // Server에 보내는 Data를 지정
                Map<String, String> params = new HashMap<String, String>();
                params.put("USER_ID", findID());
                params.put("USER_FAVORITE_ACTIVITY", findFavoriteActivity());

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 Add
    }

    private void favoriteStateRequest() {
        final String tag_string_req = "req_favorite_state";

        String requestUrl = Splashscreen.url + "favoriteState";

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
                Toast.makeText(getApplicationContext(), 123 + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() { // Server에 보내는 Data를 지정
                Map<String, String> params = new HashMap<String, String>();
                params.put("USER_ID", findID());
                params.put("USER_FAVORITE_STATE", findFavoriteState());

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 Add
    }

    private void setListView(JSONArray jsonArray) {
        itemNum = jsonArray.length();

        adapter2 = new ListViewAdapter();
        TownItems = new TownItem[itemNum];

        for(int i = 0; i < itemNum; i++) {
            try {
                TownItems[i] = new TownItem(jsonArray.getJSONObject(i).getString("NAME"),
                        jsonArray.getJSONObject(i).getString("STATE"), jsonArray.getJSONObject(i).getString("CITY"),
                        jsonArray.getJSONObject(i).getString("ACTIVITY"));

                adapter2.addItem(TownItems[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        listView2.setAdapter(adapter2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
