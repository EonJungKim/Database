package com.example.user.termproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 2017-11-20.
 */

public class MenuActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnCity = (Button) findViewById(R.id.btnCity);
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ListViewActivity.class);
                myIntent.putExtra("REQUEST_CODE", "city");
                startActivity(myIntent);
            }
        });

        Button btnProgram = (Button) findViewById(R.id.btnProgram);
        btnProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ListViewActivity.class);
                myIntent.putExtra("REQUEST_CODE", "activity");
                startActivity(myIntent);
            }
        });

        Button btnFavoriteState = (Button) findViewById(R.id.btnFavoriteState);
        btnFavoriteState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ListViewActivity.class);
                myIntent.putExtra("REQUEST_CODE", "favorite_state");
                startActivity(myIntent);
            }
        });

        Button btnFavoriteActivity = (Button) findViewById(R.id.btnFavoriteActivity);
        btnFavoriteActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ListViewActivity.class);
                myIntent.putExtra("REQUEST_CODE", "favorite_activity");
                startActivity(myIntent);
            }
        });

        Button btnMargin = (Button) findViewById(R.id.btnMargin);
        btnMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MarginActivity.class);
                startActivity(myIntent);
            }
        });

        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        Intent myIntent;

        switch (curId) {
            case R.id.menu_logout:
                db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

                String sql = "drop table user;";

                if(db != null)
                    try {
                        db.execSQL(sql);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                myIntent = new Intent(getApplicationContext(), LogInActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);

                break;

            case R.id.menu_user_info:
                myIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
                startActivity(myIntent);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
