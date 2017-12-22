package com.example.user.termproject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Random;

public class Splashscreen extends Activity {

    SQLiteDatabase db;

    Thread splashTread;
    ImageView imageView;

    public static String url="http://192.168.0.10:3000/public/";

    boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        imageView = (ImageView) findViewById(R.id.splash);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        int[] ids = new int[]{R.drawable.s_img};

        Random randomGenerator = new Random();

        int rg = randomGenerator.nextInt(ids.length);

        this.imageView.setImageDrawable(getResources().getDrawable(ids[rg]));

        splashTread = new Thread() {
            @Override
            public void run() {
                try {

                    int waited = 0;
                    // Splash screen pause time

                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent;

                    if(isCreated) {
                        intent = new Intent(Splashscreen.this, LogInActivity.class);
                    }
                    else {
                        intent = new Intent(Splashscreen.this, MenuActivity.class);
                    }

                    //intent =

                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                    Splashscreen.this.finish();

                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splashscreen.this.finish();
                }

            }
        };
        splashTread.start();
        try {
            createDatabase();
        } catch (Exception e) {
            isCreated = false;
            e.printStackTrace();
        }

    }

    private void createDatabase() {  // Log In을 위한 Database를 Create하는 Method
        db = openOrCreateDatabase("USER_INFORMATION.db", MODE_PRIVATE, null);

        if (db != null) {
            db.execSQL("create table user (name text, id text, password text, favoriteState text, favoriteActivity text, email text);");
            isCreated = true;
        }
    }
}