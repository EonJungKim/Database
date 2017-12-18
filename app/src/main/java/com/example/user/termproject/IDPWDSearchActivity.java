package com.example.user.termproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by user on 2017-10-03.
 * ID/PWd 찾기 Activity
 */

public class IDPWDSearchActivity extends AppCompatActivity {

    Button btnIDSearch, btnPWDSearch;
    EditText edtIDSearchName, edtIDSearchEMail;
    EditText edtPWDSearchID, edtPWDSearchName, edtPWDSearchEMail;

    String name, ID, eMail;
    String password;

    private void initActivity() {   // Initialize Widget
        // ID Search
        edtIDSearchName = (EditText) findViewById(R.id.edtIDSearchName);
        edtIDSearchEMail = (EditText) findViewById(R.id.edtIDSearchEMail);

        // Password Search
        edtPWDSearchID = (EditText) findViewById(R.id.edtPWDSearchID);
        edtPWDSearchName = (EditText) findViewById(R.id.edtPWDSearchName);
        edtPWDSearchEMail = (EditText) findViewById(R.id.edtPWDSearchEMail);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_pwd_search);

        initActivity();

        btnIDSearch = (Button) findViewById(R.id.btnIDSearch);
        btnIDSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtIDSearchName.getText().toString().trim();
                eMail = edtIDSearchEMail.getText().toString().trim();

                IDSearch(name, eMail);

                edtIDSearchName.setText("");
                edtIDSearchEMail.setText("");
            }
        });

        btnPWDSearch = (Button) findViewById(R.id.btnPWDSearch);
        btnPWDSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID = edtPWDSearchID.getText().toString().trim();
                name = edtPWDSearchName.getText().toString().trim();
                eMail = edtPWDSearchEMail.getText().toString().trim();

                PWDSearch(ID, name, eMail);

                edtPWDSearchID.setText("");
                edtPWDSearchName.setText("");
                edtPWDSearchEMail.setText("");
            }
        });
    }

    private void showSearchMessage(int code) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setTitle("검색결과");

        myBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        if(code == 120) {
            myBuilder.setIcon(android.R.drawable.ic_dialog_info);
            myBuilder.setMessage("회원님의 ID는 \"" + ID + "\" 입니다.");
        }
        else if(code == 121) {
            myBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            myBuilder.setMessage("회원님의 ID가 존재하지 않습니다.");
        }
        else if(code == 122) {
            myBuilder.setIcon(android.R.drawable.ic_dialog_info);
            myBuilder.setMessage(ID + "님의 비밀번호는 \"" + password + "\" 입니다.");
        }
        else if(code == 123) {
            myBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            myBuilder.setMessage("입력하신 회원정보와 일치하는 ID가 존재하지 않습니다.");
        }

        AlertDialog dialog = myBuilder.create();
        dialog.show();
    }

    public void IDSearch(final String name, final String eMail){

        // 전달받은 name과 eMail을 json Format으로 변형해서 Web Server로 전달하고
        // 이에대한 응답으로 boolean 값을 json Format으로 전달받음

        final String tag_string_req = "req_id_search";

        String requestUrl = Splashscreen.url + "idSearch";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    int ID_SEARCH_CODE = json_receiver.getInt("ERROR_CODE");

                    if (ID_SEARCH_CODE == 120)
                        ID = json_receiver.getString("USER_ID");

                    showSearchMessage(ID_SEARCH_CODE);
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
                params.put("USER_NAME", name);
                params.put("USER_EMAIL", eMail);

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
    }

    public void PWDSearch(final String ID, final String name, final String eMail) {

        // ID, name, eMail을 ("key", "value") 형식의 String 값으로 Server에 Request
        // Server는 이에 대한 응답으로 성공여부를 boolean 값으로, 비밀번호는 ("key", "value")
        // 형식을 JSONObject Format으로 한 데 묶어 String 으로 Client에 Response

        final String tag_string_req = "req_pwd_search";

        String requestUrl = Splashscreen.url + "passwordSearch";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    int PWD_SEARCH_CODE = json_receiver.getInt("ERROR_CODE");

                    if (PWD_SEARCH_CODE == 122)
                        password = json_receiver.getString("USER_PASSWORD");

                    showSearchMessage(PWD_SEARCH_CODE);
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
                params.put("USER_NAME", name);
                params.put("USER_EMAIL", eMail);

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
    }
}
