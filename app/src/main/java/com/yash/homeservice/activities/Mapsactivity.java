package com.yash.homeservice.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.homeservice.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapsactivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap nmap;
    private FirebaseFirestore firestore;
    public String uid;
    private View mapView;
    public String Fulladdress,sflat,scity,sstate,spincode,sstreet;
    public EditText flat,street,city,pincode,state;
    public Button proceed,saveloc;
    private ImageView imageView,chooseloc;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<AutocompletePrediction> predictionList;
    private LocationCallback locationCallback;
    private Location lastlocation;
    public TextView tvaddress;
    private com.google.android.gms.maps.model.LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsactivity);

        tvaddress=findViewById(R.id.etaddress);
        imageView=findViewById(R.id.mdback);
        chooseloc=findViewById(R.id.btnchoose);
        saveloc=findViewById(R.id.btnsaveadd);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mapsactivity.super.onBackPressed();
            }
        });

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore=FirebaseFirestore.getInstance();
          mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.googlemap);
           mapView=mapFragment.getView();
        mapFragment.getMapAsync(Mapsactivity.this);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Mapsactivity.this);

            chooseloc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(Mapsactivity.this);
                    dialog.setContentView(R.layout.dialogaddress);
                     Button cancel;
                    flat=dialog.findViewById(R.id.dno);
                    street=dialog.findViewById(R.id.dstreet);
                    city=dialog.findViewById(R.id.dcity);
                    pincode=dialog.findViewById(R.id.dpincode);
                    state=dialog.findViewById(R.id.dstate);
                    proceed=dialog.findViewById(R.id.submitadd);
                    cancel=dialog.findViewById(R.id.dcancel);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sflat=flat.getText().toString().trim();
                            sstreet=street.getText().toString().trim();
                            scity=city.getText().toString().toLowerCase().trim();
                            spincode=pincode.getText().toString().trim();
                            sstate=state.getText().toString().trim();

                            if (TextUtils.isEmpty(sflat))
                            {
                                Toast.makeText(Mapsactivity.this, "Please enter house number", Toast.LENGTH_SHORT).show();
                                flat.requestFocus();
                            }
                            else if(TextUtils.isEmpty(sstreet))
                            {
                                Toast.makeText(Mapsactivity.this, "Please enter street ", Toast.LENGTH_SHORT).show();
                                street.requestFocus();
                            }else if (TextUtils.isEmpty(scity))
                            {
                                Toast.makeText(Mapsactivity.this, "Please enter your city", Toast.LENGTH_SHORT).show();
                                city.requestFocus();
                            }else if (TextUtils.isEmpty(spincode))
                            {
                                Toast.makeText(Mapsactivity.this, "Please enter pincode number", Toast.LENGTH_SHORT).show();
                                pincode.requestFocus();
                            }else if (spincode.length()!=6)
                            {
                                Toast.makeText(Mapsactivity.this, "Pincode length must be 6", Toast.LENGTH_SHORT).show();
                                pincode.requestFocus();
                            }else if (TextUtils.isEmpty(sstate))
                            {
                                Toast.makeText(Mapsactivity.this, "Please select your state", Toast.LENGTH_SHORT).show();
                                state.requestFocus();
                            }else {
                                Fulladdress= (flat.getText().toString().trim()+","+street.getText().toString().trim()+","+city.getText().toString().trim()+","+state.getText().toString().trim()+"-"+pincode.getText().toString().trim());
                                dialog.dismiss();
                                tvaddress.setText(Fulladdress);
                            }

                        }

                    });
                    dialog.setCancelable(false);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                }
            });

                saveloc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("address",tvaddress.getText().toString().trim());
                        map.put("city",scity);
                    firestore.collection("kaam25_users").document(uid).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Mapsactivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Mapsactivity.this,DashboardActivity.class));

                            }else {
                                Toast.makeText(Mapsactivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Mapsactivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    }
                });
    }

    @SuppressLint("Missing Permission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nmap=googleMap;
        nmap.setMyLocationEnabled(true);
        nmap.getUiSettings().setMapToolbarEnabled(true);

        if (mapView!=null && mapView.findViewById(Integer.parseInt("1"))!=null)
        {
            View locationbutton=((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams)locationbutton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            layoutParams.setMargins(0,0,40,180);

            LocationRequest locationRequest=LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder= new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            SettingsClient settingsClient= LocationServices.getSettingsClient(Mapsactivity.this);
            Task<LocationSettingsResponse> task=settingsClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener(Mapsactivity.this,new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                  getdeviceLocation();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException)
                    {
                        ResolvableApiException resolvableApiException= (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(Mapsactivity.this,51);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });



            nmap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    double latitude=location.getLatitude();
                    double longitude=location.getLongitude();
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {
                        List<Address> address=geocoder.getFromLocation(latitude,longitude,1);
                        String add = address.get(0).getAddressLine(0);
                        tvaddress.setText(add);
                        scity=address.get(0).getLocality();
                        nmap.addMarker(new MarkerOptions().position(latLng)).setTitle(add);
                        nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==51)
        {
            if (resultCode== RESULT_OK)
            {
                    getdeviceLocation();
            }
        }
    }
    @SuppressLint("Missing Permission")
    private void getdeviceLocation() {
   fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
       @Override
       public void onComplete(@NonNull Task<Location> task) {
            if (task.isSuccessful())
            {
                lastlocation=task.getResult();
                if (lastlocation!=null)
                {
                    latLng=new LatLng(lastlocation.getLatitude(),lastlocation.getLongitude());
                    nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                    nmap.addMarker(new MarkerOptions().position(latLng).title("Current location"));
                    double latitude=lastlocation.getLatitude();
                    double longitude=lastlocation.getLongitude();
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {
                        List<Address> address=geocoder.getFromLocation(latitude,longitude,1);
                        String add = address.get(0).getAddressLine(0);
                            tvaddress.setText(add);
                        scity=address.get(0).getLocality();
                        nmap.addMarker(new MarkerOptions().position(latLng)).setTitle(add);
                        nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else {
                    LocationRequest locationRequest=new LocationRequest().create();
                    locationRequest.setInterval(10000);
                    locationRequest.setFastestInterval(5000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                   locationCallback=new LocationCallback(){
                       @Override
                       public void onLocationResult(LocationResult locationResult) {
                           super.onLocationResult(locationResult);
                           if (locationResult==null)
                           {
                               return;
                           }
                           lastlocation=locationResult.getLastLocation();
                           latLng=new LatLng(lastlocation.getLatitude(),lastlocation.getLongitude());
                           nmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
                           nmap.addMarker(new MarkerOptions().position(latLng).title("Current location"));
                           fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                       }
                   };
                   fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
                }
            }else {
                Toast.makeText(Mapsactivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
            }
       }
   });
    }
}
