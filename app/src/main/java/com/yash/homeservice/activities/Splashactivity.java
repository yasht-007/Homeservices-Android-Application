package com.yash.homeservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SubscriptionPlan;
import android.view.animation.LinearInterpolator;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.homeservice.R;

public class Splashactivity extends AppCompatActivity {

    private Handler mHandler;
    private LazyLoader mloader;
    private String Service;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);

        mloader=findViewById(R.id.loader);
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                preferences=getSharedPreferences("onBoardingScreen",MODE_PRIVATE);

                boolean isFirstTime = preferences.getBoolean("firsttime",true);


                if (isFirstTime)
                {
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putBoolean("firsttime",false);
                    editor.commit();
                    Intent intent = new Intent(Splashactivity.this, onBoarding.class);
                    startActivity(intent);
                    finish();
                }else {
                    LazyLoader loader = new LazyLoader(Splashactivity.this, 30, 20, ContextCompat.getColor(Splashactivity.this, R.color.loader_selected),
                            ContextCompat.getColor(Splashactivity.this, R.color.loader_selected),
                            ContextCompat.getColor(Splashactivity.this, R.color.loader_selected));
                    loader.setAnimDuration(500);
                    loader.setFirstDelayDuration(100);
                    loader.setSecondDelayDuration(200);
                    loader.setInterpolator(new LinearInterpolator());

                    mloader.addView(loader);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                    if (user!= null)
                    {
                      String userrole=preferences.getString("user",null);

                        if (userrole.equals("user"))
                        {
                            Intent intent=new Intent(Splashactivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Intent intent=new Intent(Splashactivity.this, VendorDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Intent intent = new Intent(Splashactivity.this, FirstpageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        },3000);

    }
}
