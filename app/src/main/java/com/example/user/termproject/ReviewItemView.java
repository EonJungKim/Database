package com.example.user.termproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by beeup on 2017-11-19.
 */

public class ReviewItemView extends LinearLayout {
    RatingBar textView_rating; //점수
    TextView textView_id;//아이디
    TextView textView_title;//리뷰 제목
    TextView textView_date;//날짜

    public ReviewItemView(Context context) {
        super(context);
    }

    public ReviewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_item, this, true);

        textView_rating = (RatingBar) findViewById(R.id.Review_Rating);
        textView_id = (TextView) findViewById(R.id.Review_ID);
        textView_title = (TextView) findViewById(R.id.Review_Title);
        textView_date = (TextView) findViewById(R.id.Review_Date);
    }

    public void setRating(Float rating) {
        textView_rating.setRating (rating);
    }

    public void setId(String id){
        textView_id.setText(id);
    }

    public void setText(String text){
        textView_title.setText(text);
    }

    public void setDate(String date) {
        textView_date.setText(date);
    }
}
