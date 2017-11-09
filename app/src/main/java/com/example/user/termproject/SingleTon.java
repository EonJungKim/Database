package com.example.user.termproject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.content.ContentValues.TAG;

class SingleTon {
    private static com.example.user.termproject.SingleTon mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private SingleTon(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized com.example.user.termproject.SingleTon getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new com.example.user.termproject.SingleTon(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
}
