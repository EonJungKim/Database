package com.example.user.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by user on 2017-10-02.
 */

public class SearchActivity extends AppCompatActivity {

    Button btnSearch;
    Button btnUserInformation, btnLogOut;
    Spinner spnProvince, spnCity;

    String province, city;

    String[] provinces = { "상관없음", "경기도", "경상북도", "경상남도", "전라북도", "전라남도", "강원도", "충청북도", "충청남도", "제주도"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button btnSearch;

        btnUserInformation = (Button) findViewById(R.id.btnUserInformation);
        btnUserInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
                myIntent.putExtra("REQUEST_CODE", 2);
                startActivity(myIntent);
            }
        });

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent myIntent = new Intent(getApplicationContext(), LogInActivity.class);
                //startActivity(myIntent);
            }
        });

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // province와 city를 json Format으로 변형해서 Web Server로 전달
                // Web Server에서 json Format으로 전달해준 Relation을 ListView에 Parsing

            }
        });

        spnProvince = (Spinner) findViewById(R.id.spnProvince);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinces);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnProvince.setAdapter(provinceAdapter);

        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                province = provinces[position];

                final String[] cities;

                switch (position) {
                    case 0:
                        cities = new String[]{"상관없음"};
                        break;

                    case 1:
                        cities = new String[]{"상관없음", "수원시", "성남시", "의정부시", "안양시",
                                            "부천시", "광명시", "평택시", "동두천시", "안산시",
                                            "고양시", "과천시", "구리시", "남양주시", "오산시",
                                            "시흥시", "군포시", "의왕시", "하남시", "용인시",
                                            "파주시", "이천시", "안성시", "김포시", "화성시",
                                            "광주시", "양주시", "포천시", "여주시", "연천군",
                                            "가평군", "양평군"};
                        break;

                    case 2:
                        cities = new String[]{"상관없음", "포항시", "경주시", "김천시", "안동시",
                                            "구미시", "영주시", "영천시", "상주시", "문경시",
                                            "경산시", "군위군", "의성군", "청송군", "영양군",
                                            "영덕군", "청도군", "고령군", "성주군", "칠곡군",
                                            "예천군", "봉화군", "울진군", "울릉군"};
                        break;

                    case 3:
                        cities = new String[]{"상관없음", "창원시", "진주시", "통영시", "사천시",
                                            "김해시", "밀양시", "거제시", "양산시", "의령군",
                                            "함안군", "창녕군", "고성군", "남해군", "하동군",
                                            "산청군", "함양군", "거창군", "합천군"};
                        break;

                    case 4:
                        cities = new String[]{"상관없음", "전주시", "군산시", "익산시", "정읍시",
                                            "남원시", "김제시", "완주군", "진안군", "무주군",
                                            "장수군", "임실군", "순창군", "고창군", "부안군"};
                        break;

                    case 5:
                        cities = new String[]{"상관없음", "목포시", "여수시", "순천시", "나주시",
                                            "광양시", "담양군", "곡성군", "구례군", "고흥군",
                                            "보성군", "화순군", "장흥군", "강진군", "해남군",
                                            "영암군", "무안군", "함평군", "영광군", "장성군",
                                            "완도군", "진도군", "신안군"};
                        break;

                    case 6:
                        cities = new String[]{"상관없음", "춘천시", "원주시", "강릉시", "동해시",
                                            "태백시", "속초시", "삼척시", "홍천군", "횡성군",
                                            "영월군", "평창군", "정선군", "철원군", "화천군",
                                            "양구군", "인제군", "고성군", "양양군"};
                        break;

                    case 7:
                        cities = new String[]{"상관없음", "청주시", "충주시", "제천시", "보은군",
                                            "옥천군", "영동군", "증평군", "진천군", "괴산군",
                                            "음성군", "단양군"};
                        break;

                    case 8:
                        cities = new String[]{"상관없음", "천안시", "공주시", "보령시", "아산시",
                                            "서산시", "논산시", "계룡시", "당진시", "금산군",
                                            "부여군", "서천군", "청양군", "홍성군", "예산군",
                                            "태안군"};
                        break;

                    default:
                        cities = new String[]{"상관없음", "제주시", "서귀포시"};
                        break;
                }

                spnCity = (Spinner) findViewById(R.id.spnCity);
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_spinner_item, cities);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spnCity.setAdapter(cityAdapter);

                spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        city = cities[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}