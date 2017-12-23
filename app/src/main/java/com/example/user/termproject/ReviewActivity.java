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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by beeup on 2017-11-19.
 */


public class ReviewActivity extends AppCompatActivity {

    Button btnReviewSubmit;

    String townName;

    ListView listView3;
    ReviewAdapter adapter;

    SQLiteDatabase db;

    int itemNum;

    ReviewItem[] reviewItems;

    private void getParameter() {
        Intent intent = getIntent();
        townName = intent.getStringExtra("TOWN_NAME");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        listView3 = (ListView) findViewById(R.id.ListView);

        getParameter();

        ReviewSearch();

        btnReviewSubmit = (Button) findViewById(R.id.btnReviewSubmit);
        btnReviewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ReviewSubmitActivity.class);
                startActivity(myIntent);
            }
        });
    }

    class ReviewAdapter extends BaseAdapter {
        ArrayList<ReviewItem> items = new ArrayList<ReviewItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(ReviewItem item) {
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

            ReviewItemView view = new ReviewItemView(getApplicationContext());

            ReviewItem item = items.get(position);
            view.setRating(item.getRating());
            view.setId(item.getID());
            view.setText(item.getTitle());
            view.setDate(item.getDate());

            return view;
        }
    }

    private void setListView(JSONArray jsonArray) {
        try {
            adapter = new ReviewAdapter();

            itemNum = jsonArray.length();

            reviewItems = new ReviewItem[itemNum];

            for (int i = 0; i < itemNum; i++) {

                float score = Float.valueOf(jsonArray.getJSONObject(i).getString("REVIEW_RATING"));
                String text = jsonArray.getJSONObject(i).getString("REVIEW_CONTENT");
                String id = jsonArray.getJSONObject(i).getString("USER_ID");
                String date = jsonArray.getJSONObject(i).getString("REVIEW_DATE"); // 리스트뷰 출력을 위한 요소를 받고나서
                String num = jsonArray.getJSONObject(i).getString("REVIEW_NUMBER");

                reviewItems[i] = new ReviewItem(score, text, id, date, num);

                adapter.addItem(reviewItems[i]); // town item에 추가해준다

                listView3.setAdapter(adapter);
                listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String reviewNum = reviewItems[position].getNum();

                        Intent myIntent = new Intent(getApplicationContext(), ReviewDetail.class);
                        myIntent.putExtra("REVIEW_NUMBER", reviewNum);
                        startActivity(myIntent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ReviewSearch() {
        final String tag_string_req = "req_review_search";

        RequestQueue rq = Volley.newRequestQueue(this); // Create Request Queue

        final String requestUrl = Splashscreen.url + "reviewSearch";  // IP Address : localhost, Port Number : 3000

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {   // Server가 Data를 보내면 응답하는 Method

                try {
                    JSONArray json_receiver = new JSONArray(response);

                    setListView(json_receiver);
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
                params.put("TOWN_NAME", townName);

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