package com.example.user.termproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 2017-10-02.
 */

public class UserInformationActivity extends AppCompatActivity {

    TextView txtID,txtName;
    TextView txtFavoriteState, txtFavoriteActivity;
    TextView txtEMail;
    Button btnUserInfoCorrection;

    User user;

    SQLiteDatabase db;

    private void initActivity() {
        txtID = (TextView) findViewById(R.id.txtIDOutput);
        txtName = (TextView) findViewById(R.id.txtNameOutput);
        txtFavoriteState = (TextView) findViewById(R.id.txtFavoriteState);
        txtFavoriteActivity = (TextView) findViewById(R.id.txtFavoriteActivity);
        txtEMail = (TextView) findViewById(R.id.txtEMailOutput);

        btnUserInfoCorrection = (Button) findViewById(R.id.btnUserInfoCorrection);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        user = new User();

        readDatabase();

        btnUserInfoCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), UserInfoCorrectActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void readDatabase() {
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        if(db != null) {
            String sql = "select name, id, password, favoriteState, favoriteActivity, email from user";

            Cursor cursor = db.rawQuery(sql, null);

            cursor.moveToNext();

            user.setName(cursor.getString(0));
            user.setID(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setFavoriteState(cursor.getString(3));
            user.setFavoriteActivity(cursor.getString(4));
            user.setEMail(cursor.getString(5));
        }

        setEditText(user);
    }

    private void setEditText(User user) {
        txtID.setText(user.getID());
        txtName.setText(user.getName());
        txtFavoriteState.setText(user.getFavoriteState());
        txtFavoriteActivity.setText(user.getFavoriteActivity());
        txtEMail.setText(user.getEMail());
    }
}
