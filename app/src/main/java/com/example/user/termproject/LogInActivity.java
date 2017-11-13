package com.example.user.termproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    String ID, PWD;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final Button btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtID = (EditText) findViewById(R.id.edtID);
                EditText edtPWD = (EditText) findViewById(R.id.edtPWD);

                ID = edtID.getText().toString().trim();
                PWD = edtPWD.getText().toString().trim();

                if(ID.equals(""))   // ID를 입력하지 않은 경우
                    showMessage(100);
                else if(PWD.equals("")) // Password를 입력하지 않은경우
                    showMessage(101);
                else
                    IDSearch(ID, PWD);  // Server에 ID와 Password를 보내서 가입된 User인지 Check

                edtID.setText("");
                edtPWD.setText("");
            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);   // SubmitActivity로 전환
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SubmitActivity.class);
                startActivity(myIntent);
            }
        });

        Button btnIDPWDSearch = (Button) findViewById(R.id.btnIDPWDSearch); // IDPWDSearchActivity로 전환
        btnIDPWDSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), IDPWDSearchActivity.class);
                startActivity(myIntent);
            }
        });
    }


    private void IDSearch(final String ID, final String PWD) {
        // Server에 ID와 Password를 보내서 가입되어있는 User인지 Check하는 Method
        final String tag_string_req = "req_login";

        RequestQueue rq = Volley.newRequestQueue(this); // Create Request Queue

        String url = "http://localhost:3000/";  // IP Address : localhost, Port Number : 3000

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {   // Server가 Data를 보내면 응답하는 Method

                    try {
                        JSONObject json_receiver = new JSONObject(response);

                        int ERROR_CODE = json_receiver.getInt("ERROR_CODE");

                        if (ERROR_CODE == 100 || ERROR_CODE == 101)    // ID or Password mismatch
                            showMessage(ERROR_CODE);

                        else {                  // Log in success
                            saveUserInfo(json_receiver); // User Database에 User Information을 저장

                            Toast.makeText(LogInActivity.this, ID + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                            Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
                            startActivity(myIntent);    // SearchActivity으로 화면 전환
                        }

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
                params.put("USER_ID", ID);
                params.put("USER_PASSWORD", PWD);
                params.put("REQUEST_CODE", tag_string_req);

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 Add
    }

    private void saveUserInfo(JSONObject userInfo) {
        // Server로부터 전달받은 User Information을 내부 Database에 저장하는 Method
        try {
            String userName = userInfo.getString("USER_NAME");
            String userID = userInfo.getString("USER_ID");
            String userPWD = userInfo.getString("USER_PASSWORD");
            String userSex = userInfo.getString("USER_SEX");
            String userBirthday = userInfo.getString("USER_BIRTHDAY");
            String userPhoneNumber = userInfo.getString("USER_PHONE_NUMBER");
            String userCellNumber = userInfo.getString("USER_CELL_NUMBER");
            String userEMail = userInfo.getString("USER_EMAIL");

            String databaseName = "USER_INFORMATION.db";

            createDatabase(databaseName);

            if(db != null) {
                String sql = "insert into user(name, id, password, sex, birthday, phoneNumber, cellNumber, email) values(?, ?, ?, ?, ?, ?, ?, ?)";
                Object[] params = {userName, userID, userPWD, userSex, userBirthday, userPhoneNumber, userCellNumber, userEMail};

                db.execSQL(sql, params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createDatabase(String name) {  // Log In을 위한 Database를 Create하는 Method
        db = openOrCreateDatabase(name, MODE_WORLD_WRITEABLE, null);

        if(db != null)
            db.execSQL("create table user (name text, id text, password text, " +
                    "sex text, birthday text, phoneNumber text, " +
                    "cellNumber text, email text);");
    }

    private void showMessage(int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        builder.setNegativeButton("ID/PWD 찾기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent myIntent = new Intent(getApplicationContext(), IDPWDSearchActivity.class);
                myIntent.putExtra("REQUEST_CODE", 1);

                startActivity(myIntent);
            }
        });

        if(code == 100) {
            builder.setMessage("잘못된 ID입니다.");
        }
        else if(code == 101) {
            builder.setMessage("잘못된 비밀번호입니다.");
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}