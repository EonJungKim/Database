package com.example.user.termproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by user on 2017-10-03.
 * ID/PWd 찾기 Activity
 */

public class IDPWDSearchActivity extends AppCompatActivity {

    Button btnIDSearch, btnPWDSearch;
    EditText edtIDSearchName, edtIDSearchEMail;
    EditText edtPWDSearchID, edtPWDSearchName, edtPWDSearchEMail;

    String name, ID, eMail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_pwd_search);

        btnIDSearch = (Button) findViewById(R.id.btnIDSearch);
        btnIDSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtIDSearchName = (EditText) findViewById(R.id.edtIDSearchName);
                edtIDSearchEMail = (EditText) findViewById(R.id.edtIDSearchEMail);

                name = edtIDSearchName.getText().toString().trim();
                eMail = edtIDSearchEMail.getText().toString().trim();

                IDSearch(name, eMail);
            }
        });

        btnPWDSearch = (Button) findViewById(R.id.btnPWDSearch);
        btnPWDSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPWDSearchID = (EditText) findViewById(R.id.edtPWDSearchID);
                edtPWDSearchName = (EditText) findViewById(R.id.edtPWDSearchName);
                edtPWDSearchEMail = (EditText) findViewById(R.id.edtPWDSearchEMail);

                ID = edtPWDSearchID.getText().toString().trim();
                name = edtPWDSearchName.getText().toString().trim();
                eMail = edtPWDSearchEMail.getText().toString().trim();

                PWDSearch(ID, name, eMail);
            }
        });
    }

    private void showSearchMessage(int code) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setTitle("검색결과");
        myBuilder.setIcon(android.R.drawable.ic_dialog_info);

        myBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        String ID = "ID";
        String password = "PWD";

        if(code == 1) {
            myBuilder.setMessage("회원님의 ID는 \"" + ID + "\" 입니다.");
        }
        else if(code == 2) {
            myBuilder.setMessage("회원님의 ID가 존재하지 않습니다.");
        }
        else if(code == 3) {
            myBuilder.setMessage(ID + "님의 비밀번호는 \"" + password + "\" 입니다.");
        }
        else if(code == 4) {
            myBuilder.setMessage("입력하신 회원정보와 일치하는 ID가 존재하지 않습니다.");
        }

        AlertDialog dialog = myBuilder.create();
        dialog.show();
    }

    public void IDSearch(String name, String eMail){

        // 전달받은 name과 eMail을 json Format으로 변형해서 Web Server로 전달하고
        // 이에대한 응답으로 boolean 값을 json Format으로 전달받음

        if(true) {
            showSearchMessage(1);
        }
        else {
            showSearchMessage(2);
        }

        edtIDSearchName.setText("");
        edtIDSearchEMail.setText("");
    }

    public void PWDSearch(String ID, String name, String eMail) {

        // 전달받은 ID, name, eMail을 json Format으로 변형해서 Web Server로 전달하고
        // 이에대한 응답으로 boolean 값을 json Format으로 전달받음

        if (true) {
            showSearchMessage(3);
        }
        else {
            showSearchMessage(4);
        }

        edtPWDSearchID.setText("");
        edtPWDSearchName.setText("");
        edtPWDSearchEMail.setText("");
    }
}
