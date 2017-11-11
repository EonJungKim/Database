package com.example.user.termproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

    Button btnUserSubmit, btnIDDuplicate;

    EditText edtIDInput, edtPWDInput1, edtPWDInput2;
    EditText edtName, edtBirthDay;
    EditText edtPhoneNumber, edtHPNumber;
    EditText edtEMail;

    RadioButton rdMan, rdWoman;

    String password1, password2;

    Boolean IDCheck = false;    // ID 중복확인 여부

    User newUser = new User();

    private void IDDuplicate(final String ID) { // ID 중복확인 여부를 판별하는 Method
        // ID를 json Format으로 변형하여 Web Server로 전달

        final String tag_string_req = "req_id_duplicate";

        RequestQueue rq = Volley.newRequestQueue(this);

        String url = "http://localhost:3000/";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    int ID_CHECK_CODE = json_receiver.getInt("ERROR_CODE");

                    if (ID_CHECK_CODE == 110)    // ID mismatch
                    {
                        IDCheck = false;
                        showMessage(110);
                    }

                    else if(ID_CHECK_CODE == 111)    // PWD mismatch
                    {
                        IDCheck = true;
                        showMessage(111);
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
                params.put("REQUEST_CODE", tag_string_req);
                params.put("USER_ID", ID);

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
    }

    private boolean inputCompletion() {                                  // 입력이 완료 되었는지
        edtPWDInput1 = (EditText) findViewById(R.id.edtPWDInput1);      // 확인하는 Method
        edtPWDInput2 = (EditText) findViewById(R.id.edtPWDInput2);

        edtName = (EditText) findViewById(R.id.edtNameInput);

        edtPhoneNumber = (EditText) findViewById(R.id.edtPhonNumber);
        edtHPNumber = (EditText) findViewById(R.id.edtHPNumber);

        edtBirthDay = (EditText) findViewById(R.id.edtBirthDay);

        edtEMail = (EditText) findViewById(R.id.edtEMail);

        rdMan = (RadioButton) findViewById(R.id.rdMan);
        rdWoman = (RadioButton) findViewById(R.id.rdWoman);

        newUser.ID = edtName.getText().toString().trim();
        newUser.phoneNumber = edtPhoneNumber.getText().toString().trim();
        newUser.cellNumber = edtHPNumber.getText().toString().trim();
        newUser.birthDay = edtBirthDay.getText().toString().trim();
        newUser.eMail = edtEMail.getText().toString().trim();
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
            newUser.password = password1;

        if(rdMan.isChecked())
            newUser.sex = "male";
        else if(rdWoman.isChecked())
            newUser.sex = "female";
        else {
            Toast.makeText(this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(newUser.name.equals("")) {
            Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newUser.birthDay.equals("")) {
            Toast.makeText(this, "생일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newUser.phoneNumber.equals("")) {
            Toast.makeText(this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newUser.cellNumber.equals("")) {
            Toast.makeText(this, "휴대폰 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newUser.eMail.equals("")) {
            Toast.makeText(this, "e-Mail 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        btnIDDuplicate = (Button) findViewById(R.id.btnIDDuplicate);    // ID 중복확인
        btnIDDuplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtIDInput = (EditText) findViewById(R.id.edtIDInput);
                newUser.ID = edtIDInput.getText().toString().trim();
                // txtID를 Json 객체에 넣어서 Server로 전송

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
                }
            }
        });
    }

    private void showMessage(int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        if(code == 110) {
            builder.setMessage("사용중인 ID입니다.");
            IDCheck = false;
        }
        else if(code == 111) {
            builder.setMessage("사용할 수 있는 ID입니다.");
            IDCheck = true;
        }
        else if(code == 112)
            Toast.makeText(SubmitActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
        else if(code == 113)
            Toast.makeText(SubmitActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void submit(final User newUser) {
        final String tag_string_req = "req_user_submit";

        RequestQueue rq = Volley.newRequestQueue(this);

        String url = "http://localhost:3000/";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    int SUBMIT_CHECK_CODE = json_receiver.getInt("ERROR_CODE");

                    if (SUBMIT_CHECK_CODE == 112) { // Submit success
                        edtIDInput.setText("");
                        edtPWDInput1.setText("");
                        edtPWDInput2.setText("");
                        edtName.setText("");
                        edtBirthDay.setText("");
                        edtPhoneNumber.setText("");
                        edtHPNumber.setText("");
                        edtEMail.setText("");

                        showMessage(112);
                    }
                    else if(SUBMIT_CHECK_CODE == 113)    // Submit fail
                        showMessage(113);

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
                params.put("REQUEST_CODE", tag_string_req);
                params.put("USER_ID", newUser.ID);
                params.put("USER_PASSWORD", newUser.password);
                params.put("USER_NAME", newUser.name);
                params.put("USER_SEX", newUser.sex);
                params.put("USER_BIRTHDAY", newUser.birthDay);
                params.put("USER_PHONE_NUMBER", newUser.phoneNumber);
                params.put("USER_CELL_NUMBER", newUser.cellNumber);
                params.put("USER_EMAIL", newUser.eMail);

                return params;
            }

        };

        SingleTon.getInstance(this).addToRequestQueue(strReq,tag_string_req);
        // Request를 Request Queue에 추가
    }
}