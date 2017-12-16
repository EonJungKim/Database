package com.example.user.termproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class UserInfoCorrectActivity extends AppCompatActivity {

    private TextView txtID;
    private EditText edtPWDCorrect1, edtPWDCorrect2;
    private EditText edtName;
    private Spinner spnFavoriteState, spnFavoriteActivity;
    private EditText edtEMail;
    private Button btnCorrectionComplete;

    private ArrayAdapter spnAdapterFavoriteState, spnAdapterFavoriteActivity;

    private User user;

    private String password1, password2;

    private SQLiteDatabase db;

    private void initActivity() {
        txtID = (TextView) findViewById(R.id.txtIDOutput);

        edtPWDCorrect1 = (EditText) findViewById(R.id.edtPWDCorrect1);
        edtPWDCorrect2 = (EditText) findViewById(R.id.edtPWDCorrect2);

        edtName = (EditText) findViewById(R.id.edtNameOutput);

        spnFavoriteState = (Spinner) findViewById(R.id.spnFavoriteState);
        spnFavoriteActivity = (Spinner) findViewById(R.id.spnFavoriteActivity);

        edtEMail = (EditText) findViewById(R.id.txtEMailOutput);

        btnCorrectionComplete = (Button) findViewById(R.id.btnCorrectionComplete);

        spnFavoriteState = (Spinner) findViewById(R.id.spnFavoriteState);
        spnFavoriteActivity = (Spinner) findViewById(R.id.spnFavoriteActivity);

        spnAdapterFavoriteState = ArrayAdapter.createFromResource(this, R.array.user_state_spinner, R.layout.spinner_item);
        spnAdapterFavoriteActivity = ArrayAdapter.createFromResource(this, R.array.user_program_spinner, R.layout.spinner_item);

        spnFavoriteState.setAdapter(spnAdapterFavoriteState);
        spnFavoriteActivity.setAdapter(spnAdapterFavoriteActivity);

        spnFavoriteState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.favoriteState = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnFavoriteActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.favoriteActivity = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_correct);

        user = new User();

        initActivity();

        readDatabase();

        btnCorrectionComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputComplete()) {
                    writeDatabase();

                    correction(user);

                    finish();
                }
            }
        });
    }

    private void readDatabase() {
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        if(db != null) {
            String sql = "select name, id, email from user";

            Cursor cursor = db.rawQuery(sql, null);

            cursor.moveToNext();

            user.setName(cursor.getString(0));
            user.setID(cursor.getString(1));
            user.seteMail(cursor.getString(2));
        }

        setEditText(user);
    }

    private void setEditText(User user) {
        txtID.setText(user.getID());
        edtName.setText(user.getName());
        edtEMail.setText(user.geteMail());
    }

    private boolean inputComplete() {

        user.name = edtName.getText().toString().trim();
        user.eMail = edtEMail.getText().toString().trim();
        password1 = edtPWDCorrect1.getText().toString().trim();
        password2 = edtPWDCorrect2.getText().toString().trim();

        if(!password1.equals(password2) || password1.equals("")) {
            Toast.makeText(this, "비밀번호 설정이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            edtPWDCorrect1.setText("");
            edtPWDCorrect2.setText("");

            return false;
        }
        else
            user.password = password1;

        if(user.name.equals("")) {
            Toast.makeText(this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(user.eMail.equals("")) {
            Toast.makeText(this, "e-Mail 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void writeDatabase() {
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        if(db != null) {
            String sql = "update user set name = \"" + user.getName() + "\", password = \"" + user.getPassword() + "\"" +
                                        ", favoriteState = \"" + user.getFavoriteState() + "\", favoriteActivity = \"" + user.getFavoriteActivity() + "\"" +
                                        ", eMail = \"" + user.geteMail() + "\" where id = \"" + user.getID() + "\";";

            db.execSQL(sql);
        }
    }

    private void correction(final User newUser) {

        final String tag_string_req = "req_user_info_modification";

        RequestQueue rq = Volley.newRequestQueue(this);

        String requestUrl = Splashscreen.url + "userInfoModification";

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json_receiver = new JSONObject(response);

                    int SUBMIT_CHECK_CODE = json_receiver.getInt("ERROR_CODE");

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