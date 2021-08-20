package com.yash.homeservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yash.homeservice.R;
import com.yash.homeservice.adapter.SliderAdapter;

public class onBoarding extends AppCompatActivity {

    private ViewPager viewPager;
    private SliderAdapter adapter;
    private LinearLayout dotslayout;
    private Button lets;
    private int currentposition;
    private Animation animation;
    private TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        init();

        adapter=new SliderAdapter(this);
        viewPager.setAdapter(adapter);

        addDots(0);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        lets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(onBoarding.this,FirstpageActivity.class);
                intent.putExtra("firstime",true);
                startActivity(intent);
                finish();
            }
        });

    }
    private void init()
    {
        viewPager=findViewById(R.id.slider);
        dotslayout=findViewById(R.id.dots);
        lets=findViewById(R.id.letstartbtn);
    }

    private void addDots(int position)
    {
       dots=new TextView[3];
       dotslayout.removeAllViews();
       for (int i=0;i<dots.length;i++)
       {
           dots[i]=new TextView(this);
           dots[i].setText(Html.fromHtml("&#8226;"));
           dots[i].setTextSize(35);

           dotslayout.addView(dots[i]);

       }

       if (dots.length>0)
       {
           dots[position].setTextColor(getResources().getColor(R.color.colorAccent));
       }
    }

    ViewPager.OnPageChangeListener onPageChangeListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {


            addDots(position);
            currentposition=position;
            if (position==0)
            {
                lets.setVisibility(View.INVISIBLE);

            }else if (position==1)
            {
                lets.setVisibility(View.INVISIBLE);

            }else {
                animation= AnimationUtils.loadAnimation(onBoarding.this,R.anim.bottom);
                animation.setDuration(1500);
                lets.setAnimation(animation);
                lets.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void skip(View view)
    {
        Intent intent= new Intent(getApplicationContext(),FirstpageActivity.class);
        intent.putExtra("firstime",true);
       startActivity(intent);
    }

    public void next(View view)
    {
       viewPager.setCurrentItem(currentposition + 1);
    }
}
