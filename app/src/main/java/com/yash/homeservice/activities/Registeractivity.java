package com.yash.homeservice.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.yash.homeservice.R;
import com.yash.homeservice.fragments.AddressFragment;
import com.yash.homeservice.fragments.CreateAccountFragment;
import com.yash.homeservice.fragments.ForgotPasswordFragment;
import com.yash.homeservice.fragments.LoginFragment;
import com.yash.homeservice.fragments.OtpFragment;
import com.yash.homeservice.fragments.SelectionFragment;

public class Registeractivity extends AppCompatActivity {

    public FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);
        frameLayout=findViewById(R.id.framelayout);
            setFragment(new SelectionFragment());

    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        if(fragment instanceof ForgotPasswordFragment || fragment instanceof OtpFragment || fragment instanceof LoginFragment|| fragment instanceof CreateAccountFragment || fragment instanceof AddressFragment)
        {
           fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }
}
