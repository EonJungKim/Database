package com.example.user.termproject;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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

/**
 * Created by user on 2017-10-02.
 */

public class UserInformationActivity extends AppCompatActivity {

    TextView edtID;
    EditText edtName;
    EditText edtPWDCorrect1, edtPWDCorrect2;
    EditText edtBirthDay;
    EditText edtPhoneNumber, edtHPNumber;
    EditText edtEMail;
    RadioButton rdMan, rdWoman;

    Button btnModification;

    User user, newUser;

    SQLiteDatabase db;

    String password1, password2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        edtID = (TextView) findViewById(R.id.txtIDOutput);
        edtPWDCorrect1 = (EditText) findViewById(R.id.edtPWDCorrect1);
        edtPWDCorrect2 = (EditText) findViewById(R.id.edtPWDCorrect2);
        edtName = (EditText) findViewById(R.id.edtNameOutput);
        edtBirthDay = (EditText) findViewById(R.id.edtBirthDayOutput);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhonNumberOutput);
        edtHPNumber = (EditText) findViewById(R.id.edtHPNumberOutput);
        edtEMail = (EditText) findViewById(R.id.edtEMailOutput);
        rdMan = (RadioButton) findViewById(R.id.rdMan);
        rdWoman = (RadioButton) findViewById(R.id.rdWoman);

        user = new User();

        readDatabase();

        btnModification = (Button) findViewById(R.id.btnUserModification);
        btnModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User Data를 json Format으로 변형해서 Web Server로 전달
                if(inputCompletion()) {
                    // User Data를 json Format으로 변형해서 Web Server로 전달
                    modification();

                    finish();
                }
            }
        });
    }

    private boolean inputCompletion() {                                  // 입력이 완료 되었는지
        newUser = new User();

        password1 = edtPWDCorrect1.getText().toString().trim();
        password2 = edtPWDCorrect2.getText().toString().trim();

        if(!password1.equals(password2) || password1.equals("")) {
            Toast.makeText(this, "비밀번호 설정이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            edtPWDCorrect1.setText("");
            edtPWDCorrect2.setText("");

            return false;
        }
        else
            newUser.password = password1;

        newUser.ID = user.ID;
        newUser.name = edtName.getText().toString().trim();
        newUser.phoneNumber = edtPhoneNumber.getText().toString().trim();
        newUser.cellNumber = edtHPNumber.getText().toString().trim();
        newUser.birthDay = edtBirthDay.getText().toString().trim();
        newUser.eMail = edtEMail.getText().toString().trim();

        if(rdMan.isChecked())
            newUser.sex = "male";
        else if(rdWoman.isChecked())
            newUser.sex = "female";

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

    private void modification() {
        final String tag_string_req = "req_user_modification";

        RequestQueue rq = Volley.newRequestQueue(this);

        String url = "http://localhost:3000/";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    int SUBMIT_CHECK_CODE = json_receiver.getInt("ERROR_CODE");

                    showMessage(SUBMIT_CHECK_CODE);

                    if (SUBMIT_CHECK_CODE == 130)
                        updateDatabase();

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

    private void updateDatabase() {
        if (db != null) {
            String sql = "delete from user;";
            db.execSQL(sql);

            sql = "insert into user(name, id, password, sex, birthday, phoneNumber, cellNumber, email) values(?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {newUser.name, newUser.ID, newUser.password, newUser.sex, newUser.birthDay, newUser.phoneNumber, newUser.cellNumber, newUser.eMail};

            db.execSQL(sql, params);
        }

        readDatabase();
    }

    private void showMessage(int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        if (code == 130) {
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage("회원정보가 수정되었습니다.");
        }
        else if(code == 131) {
            builder.setMessage("회원정보 수정에 실패하셨습니다.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void readDatabase() {
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_WORLD_WRITEABLE, null);

        if(db != null) {
            String sql = "select name, id, sex, birthday, phoneNumber, cellNumber, email from user";
            Cursor cursor = db.rawQuery(sql, null);

            for(int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                user.name = cursor.getString(0);
                user.ID = cursor.getString(1);
                user.password = cursor.getString(2);
                user.sex = cursor.getString(3);
                user.birthDay = cursor.getString(4);
                user.phoneNumber = cursor.getString(5);
                user.cellNumber = cursor.getString(6);
                user.eMail = cursor.getString(7);
            }
        }

        setEditText(user);
    }

    private void setEditText(User user) {
        edtID.setText(user.ID);
        edtName.setText(user.name);
        edtPWDCorrect1.setText("");
        edtPWDCorrect2.setText("");
        edtBirthDay.setText(user.birthDay);
        edtPhoneNumber.setText(user.phoneNumber);
        edtHPNumber.setText(user.cellNumber);
        edtEMail.setText(user.eMail);

        if (user.sex.equals("man"))
            rdMan.setChecked(true);
        else
            rdWoman.setChecked(true);
    }
}
