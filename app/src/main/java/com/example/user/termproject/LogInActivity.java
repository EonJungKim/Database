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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
                    showMessage("100");
                else if(PWD.equals("")) // Password를 입력하지 않은경우
                    showMessage("101");
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

        final String requestUrl = Splashscreen.url + "login";  // IP Address : localhost, Port Number : 3000

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {   // Server가 Data를 보내면 응답하는 Method

                try {
                    JSONObject json_receiver = new JSONObject().getJSONObject(response);

                    String ERROR_CODE = json_receiver.getString("ERROR_CODE");

                     if(ERROR_CODE.equals("100")|| ERROR_CODE == "101") {}   // ID or Password mismatch
                        //showMessage(ERROR_CODE);

                    else {
                        Toast.makeText(LogInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                         //Log in success


                        //Toast.makeText(LogInActivity.this, "userName = " + userName + "\nuserID = " + userID + "\nuserPWD = " + userPWD + "\nuserFavoriteState = " + userFavoriteState +
                        //         "\nuserFavoriteActivity = " + userFavoriteProgram + "\nuserEMail = " + userEMail, Toast.LENGTH_SHORT).show();

                        //saveUserInfo(json_receiver.getString("USER_NAME"), json_receiver.getString("USER_ID"), json_receiver.getString("USER_PASSWORD"), json_receiver.getString("USER_FAVORITE_SATE"), json_receiver.getString("USER_FAVORITE_PROGRAM"), json_receiver.getString("USER_EMAIL")); // User Database에 User Information을 저장

                         try {
                             db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

                             db.execSQL("create table user (name text, id text, password text, favoriteState text, favoriteActivity text, email text);");

                         } catch (Exception e) {
                             e.printStackTrace();
                             Toast.makeText(LogInActivity.this, "create쪽에러", Toast.LENGTH_SHORT).show();
                         }

                         try {
                                 String sql = "insert into user(name, id PRIMARY KEY, password, favoriteState, favoriteActivity, email) values(?, ?, ?, ?, ?, ?);";
                                 Object[] params = {json_receiver.getString("USER_NAME"), json_receiver.getString("USER_ID"), json_receiver.getString("USER_PASSWORD"), json_receiver.getString("USER_FAVORITE_SATE"), json_receiver.getString("USER_FAVORITE_PROGRAM"), json_receiver.getString("USER_EMAIL")};

                                 Toast.makeText(LogInActivity.this, "insert", Toast.LENGTH_SHORT).show();

                                 db.execSQL(sql, params);
                         } catch (Exception e) {
                             e.printStackTrace();
                             Toast.makeText(LogInActivity.this, "try catch 에러", Toast.LENGTH_SHORT).show();
                         }

                        Toast.makeText(LogInActivity.this, ID + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                        Intent myIntent = new Intent(getApplicationContext(), MenuActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(myIntent);    // SearchActivity으로 화면 전환
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LogInActivity.this, "에러발생", Toast.LENGTH_SHORT).show();
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
                params.put("USER_ID", ID);
                params.put("USER_PASSWORD", PWD);

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 Add
    }

    private void saveUserInfo(String userName,String userID, String userPWD, String userFavoriteState, String userFavoriteProgram, String userEMail) {
            // Server로부터 전달받은 User Information을 내부 Database에 저장하는 Method

    }

    private void showMessage(String code) {
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

        if(code.equals("100")) {
            builder.setMessage("잘못된 ID입니다.");
        }
        else if(code.equals("101")) {
            builder.setMessage("잘못된 비밀번호입니다.");
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}