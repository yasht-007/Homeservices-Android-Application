package com.yash.homeservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.yash.homeservice.R;
import com.yash.homeservice.fragments.ProfileFragment;
import com.yash.homeservice.fragments.UpdateconoldFragment;

public class Updatecontactactivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean status=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatecontactactivity);
        frameLayout=findViewById(R.id.frameupdatecontact);
       setFragment(new UpdateconoldFragment());
    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (status==true)
        {
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        }
    }
}
