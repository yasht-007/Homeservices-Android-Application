package com.yash.homeservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yash.homeservice.Global.ConnectionDetector;
import com.yash.homeservice.Help.AboutUsFragment;
import com.yash.homeservice.Help.AccountSettingFragment;
import com.yash.homeservice.Help.BookingServicesFragment;
import com.yash.homeservice.Help.CancelRescheduleFragment;
import com.yash.homeservice.Help.HowToBookingServiceFragment;
import com.yash.homeservice.Help.HowToCancelServiceFragment;
import com.yash.homeservice.Help.HowToContactProfassionalFragment;
import com.yash.homeservice.Help.HowToRescheduleBookingFragment;
import com.yash.homeservice.R;
import com.yash.homeservice.fragments.CartFragment;
import com.yash.homeservice.fragments.HelpFragment;
import com.yash.homeservice.fragments.MyServicesFragment;
import com.yash.homeservice.fragments.ProfileFragment;
import com.yash.homeservice.fragments.VendorHelpFragment;
import com.yash.homeservice.fragments.VendorHomeFragment;
import com.yash.homeservice.fragments.VendorProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class VendorDashboardActivity extends AppCompatActivity {

    private boolean orderFlag,helpFlag,profileFlag,servicesflag;
    private FrameLayout frameLayout;
    private int status;
    private boolean verStatus;
    private Toolbar toolbar;
    private TextView btnSignin,tvAddress;
    private ImageView imageView;
    private ImageView addService;
    private TextView tv_text;
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);

        if (ConnectionDetector.isNetworkConnected(getApplicationContext())) {
            verStatus=getIntent().getBooleanExtra("verstatus",false);
            final FirebaseUser vendor = FirebaseAuth.getInstance().getCurrentUser();
            toolbar = (Toolbar) findViewById(R.id.vtoolbar);
            addService=findViewById(R.id.addservice);
            tv_text = findViewById(R.id.vtext);
            imageView = findViewById(R.id.vimageView);
            frameLayout = findViewById(R.id.frameVendor);
            bottomNavigationView = findViewById(R.id.vendorbottomnvg);
            fragmentList = new ArrayList<>();
            fragmentList.add(new VendorHomeFragment());
            fragmentList.add(new MyServicesFragment());
            fragmentList.add(new VendorHelpFragment());
            fragmentList.add(new VendorProfileFragment());

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId())
                    {
                        case R.id.vorders:
                            if(!orderFlag) {
                                orderFlag=true; servicesflag=false; helpFlag=false; profileFlag=false;
                                status=0;
                                setFragment(new VendorHomeFragment());
                                setTitleBar(status);

                            }
                            return true;

                        case R.id.vservices:
                            if(!servicesflag) {
                                servicesflag=true; orderFlag=false; helpFlag=false; profileFlag=false;
                                status=1;
                                setFragment(new MyServicesFragment());
                                setTitleBar(status);
                            }
                            return true;
                        case R.id.vhelp:
                            if(!helpFlag) {
                                orderFlag=false; servicesflag=false; helpFlag=true; profileFlag=false;
                                status=2;
                                setFragment(new VendorHelpFragment());
                                setTitleBar(status);
                            }
                            return true;
                        case R.id.vprofile:
                            if(!profileFlag) {
                                orderFlag=false; servicesflag=false; helpFlag=false; profileFlag=true;
                                status=3;
                                setFragment(new VendorProfileFragment());
                                setTitleBar(status);

                            }
                            return true;

                    }
                    return false;
                }

            });

            addService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(VendorDashboardActivity.this,SelectCategoryActivity.class));
                }
            });

            if (verStatus==true)
            {
                status=4;
                setFragment(new MyServicesFragment());
            }else {
                setFragment(new VendorHomeFragment());
            }

        }

        else if (!ConnectionDetector.isNetworkConnected(getApplicationContext()))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("No Internet Connection");
            builder.setMessage("You need to have Mobile Data or wifi to access this.Press Connect to provide internet access Or Press Ok to Exit");

            builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }

            }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog dialog=builder.create();
            dialog.show();


        }

    }

    private void setTitleBar(int status) {
        switch (status)
        {
            case 0:
                imageView.setImageResource(R.drawable.ic_home_black_24dp);
                addService.setVisibility(View.VISIBLE);
                tv_text.setText("My Orders");
                tv_text.setTextSize(18);
                break;

            case 1:
                imageView.setImageResource(R.drawable.ic_help_black_24dp);
                addService.setVisibility(View.VISIBLE);
                tv_text.setText("My Services");
                tv_text.setTextSize(20);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_help_black_24dp);
                addService.setVisibility(View.INVISIBLE);
                tv_text.setText("Customer Support");
                tv_text.setTextSize(20);
                break;

            case 3:
                imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
                addService.setVisibility(View.VISIBLE);
                tv_text.setText("My account");
                tv_text.setTextSize(20);
                break;
        }
    }

    public void setFragment(Fragment fragment)
    {

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        if (fragment instanceof AboutUsFragment || fragment instanceof AccountSettingFragment || fragment instanceof BookingServicesFragment || fragment instanceof CancelRescheduleFragment || fragment instanceof HowToRescheduleBookingFragment ||fragment instanceof HowToContactProfassionalFragment ||fragment instanceof HowToCancelServiceFragment ||fragment instanceof HowToBookingServiceFragment ||fragment instanceof CancelRescheduleFragment ||fragment instanceof BookingServicesFragment ||fragment instanceof AccountSettingFragment ||fragment instanceof AboutUsFragment ||fragment instanceof ProfileFragment || fragment instanceof CartFragment || fragment instanceof HelpFragment)
        {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (status==0)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to Exit?");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();



        }else if (status==4)
        {
            orderFlag=true; servicesflag=false; helpFlag=false; profileFlag=false;
            setFragment(new VendorHomeFragment());
            imageView.setImageResource(R.drawable.ic_location);
            tv_text.setText("My Orders");
            tv_text.setTextSize(18);
            status=0;

        }
        else {
            orderFlag=true; servicesflag=false; helpFlag=false; profileFlag=false;
            setFragment(new VendorHomeFragment());
            imageView.setImageResource(R.drawable.ic_location);
            tv_text.setText("My Orders");
            tv_text.setTextSize(18);
            status=0;

        }

    }

}
