package com.example.user.termproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class SubmitActivity extends AppCompatActivity {

    private Button btnUserSubmit, btnIDDuplicate;

    private EditText edtIDInput, edtPWDInput1, edtPWDInput2;
    private EditText edtName;
    private EditText edtEMail;

    private Spinner spnFavoriteState, spnFavoriteActivity;

    private ArrayAdapter<?> spnAdapterFavoriteState, spnAdapterFavoriteActivity;

    private String password1, password2;

    private Boolean IDCheck = false;    // ID 중복확인 여부

    private User newUser = new User();

    private void initActivity() {
        edtIDInput = (EditText) findViewById(R.id.edtIDInput);

        edtPWDInput1 = (EditText) findViewById(R.id.edtPWDInput1);
        edtPWDInput2 = (EditText) findViewById(R.id.edtPWDInput2);

        edtName = (EditText) findViewById(R.id.edtNameInput);

        edtEMail = (EditText) findViewById(R.id.edtEMail);

        spnFavoriteState = (Spinner) findViewById(R.id.spnFavoriteState);
        spnFavoriteActivity = (Spinner) findViewById(R.id.spnFavoriteActivity);

        spnAdapterFavoriteState = ArrayAdapter.createFromResource(this, R.array.user_state_spinner, R.layout.spinner_item);
        spnAdapterFavoriteActivity = ArrayAdapter.createFromResource(this, R.array.user_program_spinner, R.layout.spinner_item);

        spnFavoriteState.setAdapter(spnAdapterFavoriteState);
        spnFavoriteActivity.setAdapter(spnAdapterFavoriteActivity);

        spnFavoriteState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newUser.favoriteState = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnFavoriteActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newUser.favoriteActivity = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void IDDuplicate(final String ID) { // ID 중복확인 여부를 판별하는 Method
        // ID를 json Format으로 변형하여 Web Server로 전달

        IDCheck = true;

        final String tag_string_req = "req_id_duplicate";

        RequestQueue rq = Volley.newRequestQueue(this);

        String requestUrl = Splashscreen.url + "idDuplicate";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    showMessage(json_receiver.getString("ERROR_CODE"));

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

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
    }

    private boolean inputCompletion() {                         // 입력이 완료 되었는지
        newUser.setName(edtName.getText().toString().trim());       // 확인하는 Method
        newUser.setEMail(edtEMail.getText().toString().trim());
        password1 = edtPWDInput1.getText().toString().trim();
        password2 = edtPWDInput2.getText().toString().trim();

        if(IDCheck == false) {
            Toast.makeText(this, "ID 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();

            return false;
        }

        if(!password1.equals(password2) || password1.equals("")) {
            Toast.makeText(this, "비밀번호 설정이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            edtPWDInput1.setText("");
            edtPWDInput2.setText("");

            return false;
        }
        else
            newUser.setPassword(password1);

        if(newUser.name.equals("")) {
            Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newUser.eMail.equals("")) {
            Toast.makeText(this, "e-Mail 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        initActivity();

        btnIDDuplicate = (Button) findViewById(R.id.btnIDDuplicate);    // ID 중복확인
        btnIDDuplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser.setID(edtIDInput.getText().toString().trim());

                if(newUser.ID.equals(""))
                    Toast.makeText(SubmitActivity.this, "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                else
                    IDDuplicate(newUser.ID);
            }
        });

        btnUserSubmit = (Button) findViewById(R.id.btnUserSubmit);      // 회원가입
        btnUserSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputCompletion())
                {
                    // User Data를 json Format으로 변형해서 Web Server로 전달
                    submit(newUser);

                    edtIDInput.setText("");
                    edtPWDInput1.setText("");
                    edtPWDInput2.setText("");
                    edtName.setText("");
                    edtEMail.setText("");
                }
            }
        });
    }

    private void showMessage(String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");

        if(code.equals("110")) {
            builder.setMessage("사용중인 ID입니다.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            IDCheck = false;

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
        }
        else if(code.equals("111")) {
            builder.setMessage("사용할 수 있는 ID입니다.");
            builder.setIcon(android.R.drawable.ic_dialog_info);
            IDCheck = true;

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
        }
        else if(code.equals("112")) {
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage("회원가입에 성공하셨습니다.");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            });
        }
        else if(code.equals("113")) {
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setMessage("회원가입에 실패하셨습니다.");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void submit(final User newUser) {

        final String tag_string_req = "req_user_submit";

        String requestUrl = Splashscreen.url + "submit";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    showMessage(json_receiver.getString("ERROR_CODE"));

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
                params.put("USER_ID", newUser.ID);
                params.put("USER_PASSWORD", newUser.password);
                params.put("USER_NAME", newUser.name);
                params.put("USER_FAVORITE_STATE", newUser.favoriteState);
                params.put("USER_FAVORITE_ACTIVITY", newUser.favoriteActivity);
                params.put("USER_EMAIL", newUser.eMail);

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
    }
}