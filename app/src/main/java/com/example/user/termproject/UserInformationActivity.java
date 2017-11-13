package com.example.user.termproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by user on 2017-10-02.
 */

public class UserInformationActivity extends AppCompatActivity {

    EditText edtID, edtName;
    EditText edtBirthDay;
    EditText edtPhoneNumber, edtHPNumber;
    EditText edtEMail;
    RadioButton rdMan, rdWoman;

    Button btnModification;

    String sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        edtID = (EditText) findViewById(R.id.edtIDOutput);
        edtName = (EditText) findViewById(R.id.edtNameOutput);
        edtBirthDay = (EditText) findViewById(R.id.edtBirthDayOutput);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhonNumberOutput);
        edtHPNumber = (EditText) findViewById(R.id.edtHPNumberOutput);
        edtEMail = (EditText) findViewById(R.id.edtEMailOutput);
        rdMan = (RadioButton) findViewById(R.id.rdMan);
        rdWoman = (RadioButton) findViewById(R.id.rdWoman);

        User newUser = new User();

        sex = "man";    // 임시 Statement

        edtID.setText("ID");
        edtName.setText("name");

        if(sex.equals("man"))
            rdMan.setChecked(true);
        else
            rdWoman.setChecked(true);

        edtBirthDay.setText("19930207");
        edtPhoneNumber.setText("0631234567");
        edtHPNumber.setText("01012345678");
        edtEMail.setText("email@naver.com");

        btnModification = (Button) findViewById(R.id.btnUserModification);
        btnModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // User Data를 json Format으로 변형해서 Web Server로 전달

                Toast.makeText(UserInformationActivity.this, "회원정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
