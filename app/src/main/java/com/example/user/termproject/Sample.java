package com.example.user.termproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017-12-20.
 */

public class Sample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final TextView textView = (TextView) findViewById(R.id.textView);


        final String tag_string_req = "req_login";

        String requestUrl = Splashscreen.url + "login";  // IP Address : localhost, Port Number : 3000

        StringRequest strReq = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {   // Server가 Data를 보내면 응답하는 Method

                try {
                    JSONObject json_receiver = new JSONObject(response);

                    textView.append(json_receiver.getString("USER_NAME"));
                    textView.append(json_receiver.getString("USER_PASSWORD"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {    // Error가 발생하는 경우
                Toast.makeText(getApplicationContext(), 123 + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() { // Server에 보내는 Data를 지정
                Map<String, String> params = new HashMap<String, String>();
                params.put("USER_ID", "jek888");
                params.put("USER_PASSWORD", "123");

                return params;
            }
        };

        SingleTon.getInstance(this).addToRequestQueue(strReq, tag_string_req);
        // Request를 Request Queue에 Add

    }
}
