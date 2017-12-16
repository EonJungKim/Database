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

/**
 * Created by user on 2017-10-02.
 */

public class UserInfoCorrectActivity extends AppCompatActivity {

    private TextView txtID;
    private EditText edtPWDCorrect1, edtPWDCorrect2;
    private EditText edtName;
    private Spinner spnFavoriteState, spnFavoriteActivity, spnUserState;
    private EditText edtEMail;
    private Button btnCorrectionComplete;

    private ArrayAdapter spnAdapterFavoriteState, spnAdapterFavoriteActivity, spnAdapterUserState;

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
        spnUserState = (Spinner) findViewById(R.id.spnUserState);

        edtEMail = (EditText) findViewById(R.id.txtEMailOutput);

        btnCorrectionComplete = (Button) findViewById(R.id.btnCorrectionComplete);

        spnFavoriteState = (Spinner) findViewById(R.id.spnFavoriteState);
        spnFavoriteActivity = (Spinner) findViewById(R.id.spnFavoriteActivity);
        spnUserState = (Spinner) findViewById(R.id.spnUserState);

        spnAdapterFavoriteState = ArrayAdapter.createFromResource(this, R.array.user_state_spinner, R.layout.spinner_item);
        spnAdapterFavoriteActivity = ArrayAdapter.createFromResource(this, R.array.user_program_spinner, R.layout.spinner_item);
        spnAdapterUserState = ArrayAdapter.createFromResource(this, R.array.user_state_spinner, R.layout.spinner_item);

        spnFavoriteState.setAdapter(spnAdapterFavoriteState);
        spnFavoriteActivity.setAdapter(spnAdapterFavoriteActivity);
        spnUserState.setAdapter(spnAdapterUserState);

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

        spnUserState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.state = String.valueOf(parent.getItemAtPosition(position));
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

        //readDatabase();

        btnCorrectionComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputComplete()) {
                    //writeDatabase();

                    //Server로 데이터 전송

                    finish();
                }
            }
        });
    }

    private void readDatabase() {
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        if(db != null) {
            String sql = "select name, id, favoriteState, favoriteActivity, state, email from user";

            Cursor cursor = db.rawQuery(sql, null);

            cursor.moveToNext();

            user.setName(cursor.getString(0));
            user.setID(cursor.getString(1));
            user.setFavoriteState(cursor.getString(2));
            user.setFavoriteActivity(cursor.getString(3));
            user.setState(cursor.getString(4));
            user.seteMail(cursor.getString(5));
        }
        setEditText(user);
    }

    private void setEditText(User user) {
        txtID.setText(user.getID());
        edtName.setText(user.getName());
        edtEMail.setText(user.geteMail());
    }

    private boolean inputComplete() {

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
                                        ", state = \"" + user.getState() + "\", eMail = \"" + user.geteMail() + "\" where id = \"" + user.getID() + "\";";

            db.execSQL(sql);
        }
    }
}