package com.yash.homeservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
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
import com.yash.homeservice.fragments.DashboardFragment;
import com.yash.homeservice.fragments.HelpFragment;
import com.yash.homeservice.fragments.ProfileFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DashboardActivity extends AppCompatActivity {

    private boolean homeFlag, cartFlag,helpFlag,profileFlag;
    private FrameLayout frameLayout;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private int status;
    public double latitude,longitude;
    public String add;
    private Toolbar toolbar;
    public  boolean userstatus=false;
    private boolean isFirstTime;
    public boolean orderstatus=false;
    private TextView btnSignin,tvAddress;
    private ImageView imageView;
    private TextView tv_text;
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ConnectionDetector.isNetworkConnected(getApplicationContext()))
        {

            userstatus=getIntent().getBooleanExtra("userstatus",false);
            orderstatus=getIntent().getBooleanExtra("orderstatus",false);
            isFirstTime=getIntent().getBooleanExtra("firstime1",false);
            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(DashboardActivity.this);

            if (checkPermisson()==true)
            {
                getLocation();
            }else {
                Dexter.withActivity(DashboardActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                getLocation();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied())
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

                                    builder.setTitle("Permission Denied");
                                    builder.setMessage("Permission to access device location is permanently denied. you need to go to settings to allow the permission.");

                                    builder.setNegativeButton("Cancel",null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent=new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package",getPackageName(),null));
                                                }
                                            }).show();
                                }else {
                                    Toast.makeText(DashboardActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
            final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            toolbar= (Toolbar) findViewById(R.id.toolbar);
            tv_text=findViewById(R.id.text);
            btnSignin=findViewById(R.id.btndsignin);
            imageView=findViewById(R.id.imageView4);
            frameLayout= findViewById(R.id.frameHome);
            bottomNavigationView=findViewById(R.id.bottomnvg);
            fragmentList=new ArrayList<>();
            fragmentList.add(new DashboardFragment());
            fragmentList.add(new CartFragment());
            fragmentList.add(new HelpFragment());
            fragmentList.add(new ProfileFragment());


            if (user==null)
            {
                bottomNavigationView.getMenu().removeItem(R.id.profile);
                btnSignin.setVisibility(View.VISIBLE);

             }
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    switch (item.getItemId())
                    {
                        case R.id.homeicon:
                            if(!homeFlag) {
                                homeFlag=true; cartFlag=false; helpFlag=false; profileFlag=false;
                                status=0;
                                setFragment(new DashboardFragment());
                                setTitleBar(status);

                            }
                            return true;
                        case R.id.bookings:
                            if(!cartFlag) {
                                homeFlag=false; cartFlag=true; helpFlag=false; profileFlag=false;
                                status=1;
                                setFragment(new CartFragment());
                                setTitleBar(status);
                            }
                            return true;
                        case R.id.help:
                            if(!helpFlag) {
                                homeFlag=false; cartFlag=false; helpFlag=true; profileFlag=false;
                                status=2;
                                setFragment(new HelpFragment());
                                setTitleBar(status);
                            }
                            return true;
                        case R.id.profile:
                            if(!profileFlag) {
                                homeFlag=false; cartFlag=false; helpFlag=false; profileFlag=true;
                                status=3;
                                setFragment(new ProfileFragment());
                                setTitleBar(status);

                            }
                            return true;

                    }
                    return false;
                }

            });
            btnSignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(DashboardActivity.this, Registeractivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            tv_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userstatus==true)
                    {
                        Toast.makeText(DashboardActivity.this, "Please Sign in first", Toast.LENGTH_SHORT).show();
                    }else {
                        checkPermisson();
                        if (checkPermisson() == true)
                        {
                            startActivity(new Intent(DashboardActivity.this,Mapsactivity.class));

                        }else {
                            Dexter.withActivity(DashboardActivity.this)
                                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {
                                            startActivity(new Intent(DashboardActivity.this,Mapsactivity.class));

                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            if (response.isPermanentlyDenied())
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

                                                builder.setTitle("Permission Denied");
                                                builder.setMessage("Permission to access device location is permanently denied. you need to go to settings to allow the permission.");

                                                builder.setNegativeButton("Cancel",null)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                Intent intent=new Intent();
                                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                                intent.setData(Uri.fromParts("package",getPackageName(),null));
                                                            }

                                                        }).show();

                                            }
                                            else {
                                                Toast.makeText(DashboardActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            token.continuePermissionRequest();
                                        }
                                    }).check();
                    }

                    }

                }
            });
            if (orderstatus==true)
            {
                status=5;
                setFragment(new CartFragment());
                imageView.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
                tv_text.setText("Cart");
                tv_text.setTextSize(20);
            }else {
                setFragment(new DashboardFragment());
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

    private void getLocation() {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location=task.getResult();

                    if (location!=null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                            add = address.get(0).getAddressLine(0);
                            tv_text.setText(add);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    else
                    {
                        add="Kaam 25 Home Services";
                    }

                }
            });


    }

    private void setTitleBar(int status) {
        switch (status)
        {
            case 0:
                imageView.setImageResource(R.drawable.ic_signs);
                tv_text.setText(add);
                tv_text.setTextSize(15);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
                tv_text.setText("Cart");
                tv_text.setTextSize(20);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_help_black_24dp);
                tv_text.setText("Customer Support");
                tv_text.setTextSize(20);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
                tv_text.setText("My account");
                tv_text.setTextSize(20);
                break;
        }
    }


    public void setFragment(Fragment fragment)
    {

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        if (fragment instanceof HowToRescheduleBookingFragment ||fragment instanceof HowToContactProfassionalFragment ||fragment instanceof HowToCancelServiceFragment ||fragment instanceof HowToBookingServiceFragment ||fragment instanceof CancelRescheduleFragment ||fragment instanceof BookingServicesFragment ||fragment instanceof AccountSettingFragment ||fragment instanceof AboutUsFragment ||fragment instanceof ProfileFragment || fragment instanceof CartFragment || fragment instanceof HelpFragment)
        {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

       if (!ConnectionDetector.isNetworkConnected(getApplicationContext()))
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

        }else {

       }

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



        }else  if (status==4)
        {
            homeFlag=true; cartFlag=false; helpFlag=false; profileFlag=false;
            status=0;
            setFragment(new DashboardFragment());
            setTitleBar(status);
        }else if (status==5)
        {
            homeFlag=false; cartFlag=true; helpFlag=false; profileFlag=false;
            status=1;
            setFragment(new CartFragment());
            setTitleBar(status);
        }
        else {
            homeFlag=true; cartFlag=false; helpFlag=false; profileFlag=false;
            setFragment(new DashboardFragment());
            imageView.setImageResource(R.drawable.ic_signs);
            tv_text.setText(add);
            tv_text.setTextSize(15);
            status=0;

        }

    }

    private boolean checkPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
