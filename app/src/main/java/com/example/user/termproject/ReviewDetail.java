package com.example.user.termproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017-12-23.
 */

public class ReviewDetail extends AppCompatActivity {

    String reviewNum;

    float rating;
    String title, ID, date, content;

    SQLiteDatabase db;

    TextView txtReviewTitle, txtReviewID, txtReviewDate, txtReviewContent;

    private void getParameter() {
        Intent intent = getIntent();
        reviewNum = intent.getStringExtra("REVIEW_NUMBER");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        txtReviewTitle = (TextView) findViewById(R.id.txtReviewTitle);
        txtReviewID = (TextView) findViewById(R.id.txtReviewID);
        txtReviewDate = (TextView) findViewById(R.id.txtReviewDate);
        txtReviewContent = (TextView) findViewById(R.id.txtReviewContent);

        getParameter();

        review();
    }

    private void review() {
        final String tag_string_req = "req_review";

        final String requestUrl = Splashscreen.url + "review";  // IP Address : localhost, Port Number : 3000

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {   // Server가 Data를 보내면 응답하는 Method
                try {
                    JSONObject receive = new JSONObject(response);

                    rating = Float.valueOf(receive.getString("RATING"));
                    ID = receive.getString("USER_ID");
                    date = receive.getString("REVIEW_DATE");
                    content = receive.getString("REVIEW_CONTENT");
                    title = receive.getString("REVIEW_TITLE");

                    setWidget();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {    // Error가 발생하는 경우
                Toast.makeText(getApplicationContext(), 123 + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() { // Server에 보내는 Data를 지정
                Map<String, String> params = new HashMap<String, String>();
                params.put("REVIEW_NUMBER", reviewNum);

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq, tag_string_req);
        // Request를 Request Queue에 Add
    }

    private void setWidget() {
        txtReviewTitle.setText(title);
        txtReviewDate.setText(date);
        txtReviewID.setText(ID);
        txtReviewContent.setText(content);
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
