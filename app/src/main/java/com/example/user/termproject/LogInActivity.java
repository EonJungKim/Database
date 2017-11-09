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

    String ID;
    String PWD;
    int logInResult;
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

                if(ID.equals(""))
                    showMessage(1);
                else if(PWD.equals(""))
                    showMessage(2);

                IDSearch(ID, PWD);

                if(logInResult == 0) {    // ID를 입력하지 않았거나 ID가 없는경우
                    showMessage(1);
                    edtID.setText("");
                    edtPWD.setText("");
                }

                else if(logInResult == 1) {  // PWD를 입력하지 않았거나 PWD가 맞지 않는경우
                    showMessage(2);
                    edtID.setText("");
                    edtPWD.setText("");
                }

                else {
                    edtID.setText("");
                    edtPWD.setText("");

                    Toast.makeText(LogInActivity.this, ID + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(myIntent);
                }
            }
        });

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);   // submit으로 이동
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SubmitActivity.class);
                myIntent.putExtra("ACTIVITY_CODE", 1);
                startActivity(myIntent);
            }
        });

        Button btnIDPWDSearch = (Button) findViewById(R.id.btnIDPWDSearch); // ID, PWD search로 이동
        btnIDPWDSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), IDPWDSearchActivity.class);
                startActivity(myIntent);
            }
        });
    }


    private void IDSearch(final String ID, final String PWD) {

        String tag_string_req = "req_login";

        RequestQueue rq = Volley.newRequestQueue(this);

        String url = "http://localhost:3000/login";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject json_receiver = new JSONObject(response);

                    int ERROR_CODE = json_receiver.getInt("ERROR_CODE");

                    if (ERROR_CODE == 0)    // ID mismatch
                        logInResult = 0;

                    else if(ERROR_CODE == 1)    // PWD mismatch
                        logInResult = 1;

                    else {                  // Log in success
                        logInResult = 2;
                        JSONObject userInfo = new JSONObject(response);

                        saveUserInfo(userInfo); // User Database에 User Information을 저장
                    }
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
                params.put("USER_ID", ID);
                params.put("USER_PASSWORD", PWD);

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
    }

    private void saveUserInfo(JSONObject userInfo) {
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

            db.execSQL("insert into user(name, id, password, sex, birthday," +
                    "phoneNumber, cellNumber, email) values ('" + userName + "', '"
                     + userID + "', '" + userPWD + "', '" + userSex + "', '"
                     + userBirthday + "', '" + userPhoneNumber + "', '" + userCellNumber + "', '"
                     + userEMail + "');");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createDatabase(String name) {
        db = openOrCreateDatabase(name, MODE_WORLD_WRITEABLE, null);
        db.execSQL("create table user ("
                + "name text, " + " id text, "
                + "password text, " + "sex text, "
                + "birthday text, " + "phoneNumber text, "
                + "cellNumber text, " + "email text");
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

        if(code == 1) {
            builder.setMessage("잘못된 ID입니다.");
        }
        else if(code == 2) {
            builder.setMessage("잘못된 비밀번호입니다.");
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}