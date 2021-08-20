package com.yash.homeservice.Usersidebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.yash.homeservice.R;
import com.yash.homeservice.adapter.RecycleServiceDetails;
import com.yash.homeservice.modal.CustomItemAnimator;
import com.yash.homeservice.modal.ServiceShow;

import java.util.ArrayList;

public class Vendordetailuser extends AppCompatActivity implements RecycleServiceDetails.OnServicesideClickListener {

    public String name,min,max,vendorid,profileurl, address, charge, subcat, city, shopname, phone, currentemail, email, catid, subcatid;
    public float rating;
    private ImageView back;
    public String vndrid;
    private RecyclerView recyclerView;
    private ArrayList<ServiceShow> servicelist;
    private FirebaseFirestore firestore;
    private RecycleServiceDetails adapter;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvname, tvadd, tvservice, tvcharge, tvshop, ratingb, tvemail;
    private RatingBar ratingBar;
    private Button btncall, btnbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendordetailuser);

        init();
        firestore = FirebaseFirestore.getInstance();
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        charge = getIntent().getStringExtra("charge");
        rating = getIntent().getFloatExtra("rating", 0);
        subcat = getIntent().getStringExtra("subcategory");
        shopname = getIntent().getStringExtra("shopname");
        phone = getIntent().getStringExtra("phone");
        currentemail = getIntent().getStringExtra("email");
        vendorid=getIntent().getStringExtra("vendorid");
        catid = getIntent().getStringExtra("catid");
        subcatid = getIntent().getStringExtra("subcatid");
        profileurl=getIntent().getStringExtra("profileurl");
        min=getIntent().getStringExtra("min");
        max=getIntent().getStringExtra("max");

        tvname.setText(name);
        tvadd.setText(address);
        tvcharge.setText(charge);
        if (rating == 0.0) {
            ratingb.setText("0");
        } else {
            ratingb.setText("" + rating);
        }
        tvshop.setText(shopname);
        if (rating > 4.2) {
            tvservice.setText(subcat + "- Expert");
        } else {
            tvservice.setText(subcat);
        }

        ratingBar.setRating(rating);
        tvemail.setText(currentemail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vendordetailuser.super.onBackPressed();
            }
        });
        RecycleServiceDetails.status = true;
        refreshLayout.setRefreshing(true);
        getData(city);
        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(Vendordetailuser.this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                if (ActivityCompat.checkSelfPermission(Vendordetailuser.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                    return;
                                }
                                startActivity(callIntent);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(Vendordetailuser.this, "Please allow call permission", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getData(city);
            }
        });

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Bookingactivity.class);
                intent.putExtra("Catid",catid);
                intent.putExtra("Subcategoryid",subcatid);
                intent.putExtra("vendorid",vendorid);
                intent.putExtra("subcat",subcat);
                startActivity(intent);

            }
        });
    }


    private void init() {
        back = findViewById(R.id.vdback);
        tvname = findViewById(R.id.vdname);
        tvadd = findViewById(R.id.shpadd);
        tvservice = findViewById(R.id.vdservice);
        ratingBar = findViewById(R.id.vdratingBar);
        tvcharge = findViewById(R.id.vdcharge);
        tvshop = findViewById(R.id.namesh);
        ratingb = findViewById(R.id.vdscore);
        btnbook = findViewById(R.id.vndbook);
        btncall = findViewById(R.id.btncall);
        tvemail = findViewById(R.id.vdemail);
        refreshLayout = findViewById(R.id.rfla);
        recyclerView = findViewById(R.id.vdrecycler);
    }

    private void getData(final String city) {

        firestore.collection("category").document(catid).collection("subcat").document(subcatid).collection("service_providers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                refreshLayout.setRefreshing(false);
                servicelist = new ArrayList<>();
                int size = queryDocumentSnapshots.getDocuments().size();
                if (size == 0) {
                    recyclerView.setVisibility(View.GONE);

                }

                for (DocumentSnapshot read : queryDocumentSnapshots) {
                    String id = read.getId();


                    firestore.collection("kaam25_users").document(id).collection("services").whereEqualTo("city", city).whereEqualTo("subcategory", subcat).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                recyclerView.setVisibility(View.VISIBLE);
                                for (DocumentSnapshot read : task.getResult()) {
                                    charge = "₹" + read.get("min charge") + " - " + "₹" + read.get("max charge");
                                    String rating = read.get("rating").toString();
                                    name = read.get("name").toString();
                                    address = read.get("address").toString();
                                    email = read.get("email").toString();
                                    shopname = read.get("Shop_name").toString();
                                    phone = read.get("phone").toString();
                                    Float rat = Float.parseFloat(rating);
                                    vndrid=read.get("vendor_id").toString();
                                    ServiceShow serviceShow = new ServiceShow(read.getId(), read.get("profile_url").toString(), read.get("name").toString(), read.get("subcategory").toString(), rat, charge, city, address, shopname, subcat, phone, email,read.get("min charge").toString(),read.get("max charge").toString(),vndrid);
                                    if (currentemail.equals(email)) {
                                        continue;
                                    }
                                    servicelist.add(serviceShow);

                                }

                                adapter = new RecycleServiceDetails(Vendordetailuser.this, servicelist, Vendordetailuser.this);
                                LinearLayoutManager Manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(Manager);
                                recyclerView.setItemAnimator(new CustomItemAnimator());
                                recyclerView.setAdapter(adapter);


                            } else {

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Vendordetailuser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onServiceClick(ServiceShow show) {
        Intent intent = new Intent(getApplicationContext(), Vendordetailuser.class);
        intent.putExtra("city", show.getCity());
        intent.putExtra("name", show.getName());
        intent.putExtra("address", show.getAddress());
        intent.putExtra("rating", show.getRating());
        intent.putExtra("shopname", show.getShopname());
        intent.putExtra("charge", show.getServicecharge());
        intent.putExtra("subcategory", show.getSubcategory());
        intent.putExtra("phone", show.getPhone());
        intent.putExtra("email", show.getEmail());
        intent.putExtra("catid", catid);
        intent.putExtra("subcatid", subcatid);
        intent.putExtra("vendorid",show.getVendorid());
        intent.putExtra("profileurl",profileurl);
        intent.putExtra("min",show.getMincharge());
        intent.putExtra("max",show.getMaxcharge());

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
