package com.example.user.termproject;

/**
 * Created by beeup on 2017-12-22.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReviewSubmitActivity extends AppCompatActivity {

    EditText edtReviewTitle, edtReviewContent;

    RatingBar ratingbar;

    Button btnSubmit_Review;

    String title, content;

    float rating;
    String date;
    String townName;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_submit);

        edtReviewTitle = (EditText) findViewById(R.id.edtReviewTitle);
        edtReviewContent = (EditText) findViewById(R.id.edtReviewContent);

        ratingbar = (RatingBar) findViewById(R.id.rating_submit);

        btnSubmit_Review = (Button) findViewById(R.id.btnSubmitReview);
        btnSubmit_Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                townName = intent.getStringExtra("TOWN_NAME");
                title = edtReviewTitle.getText().toString().trim();
                content = edtReviewContent.getText().toString().trim();
                rating = ratingbar.getRating();
                date = getDateString();

                submit();
            }
        });

    }

    private String findID(){

        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        String sql = "select ID from user";

        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToNext();

        String ID = cursor.getString(0);

        return ID;
    }

    private void submit() {
        final String tag_string_req = "req_review_submit";

        RequestQueue rq = Volley.newRequestQueue(this);

        String requestUrl = Splashscreen.url + "reviewSubmit";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    String SUBMIT_CHECK_CODE = json_receiver.getString("ERROR_CODE");

                    showMessage(SUBMIT_CHECK_CODE);
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
                params.put("REVIEW_RATING", Float.toString(rating));
                params.put("REVIEW_TITLE", title);
                params.put("REVIEW_CONTENT", content);
                params.put("REVIEW_DATE", date);
                params.put("USER_ID", findID());
                params.put("TOWN_NAME", townName);

                return params;
            }
        };
        SingleTon.getInstance(this).addToRequestQueue(strReq, tag_string_req);
    }

    private void showMessage(String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");

        if(code.equals("140")) {
            builder.setMessage("마을 후기 등록에 실패했습니다.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
        }
        else if(code.equals("141")) {
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage("마을 후기 등록이 완료되었습니다.");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
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
