package com.yash.homeservice.Usersidebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.R;
import com.yash.homeservice.adapter.RecycleServiceDetails;
import com.yash.homeservice.modal.CustomItemAnimator;
import com.yash.homeservice.modal.ServiceShow;
import com.yash.homeservice.modal.Servicedetailsid;

import java.util.ArrayList;

public class ServicedetailsActivity extends AppCompatActivity implements RecycleServiceDetails.OnServicesideClickListener {

    private RecyclerView recyclerView;
    public String catid, subcatid,min,max;
    private ArrayList<ServiceShow> servicelist;
    public String city, subcat,vendorid,profileurl;
    private ImageView back;
    private FirebaseFirestore firestore;
    private ImageView imageView;
    private TextView title, description,text;
    private RecycleServiceDetails adapter;
    public String charge,rating,name,address,email,shopname,phone;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicedetails);

        init();

        refreshLayout.setRefreshing(true);
        firestore = FirebaseFirestore.getInstance();
        catid = getIntent().getStringExtra("Catid");
        subcatid = getIntent().getStringExtra("Subcategoryid");
        subcat = getIntent().getStringExtra("subcategory");
        text.setText(subcat+" home services");
        RecycleServiceDetails.status=false;
        firestore.collection("kaam25_users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                city = documentSnapshot.get("city").toString().toLowerCase();
                getData(city);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicedetailsActivity.super.onBackPressed();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestore.collection("kaam25_users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        city = documentSnapshot.get("city").toString().toLowerCase();
                        getData(city);
                    }
                });
            }
        });

    }

    public void getData(final String city) {

        firestore.collection("category").document(catid).collection("subcat").document(subcatid).collection("service_providers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                refreshLayout.setRefreshing(false);
                servicelist = new ArrayList<>();
                int size=queryDocumentSnapshots.getDocuments().size();
                if (size==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                }

                for (DocumentSnapshot read : queryDocumentSnapshots) {
                    String id = read.getId();


                    firestore.collection("kaam25_users").document(id).collection("services").whereEqualTo("city", city).whereEqualTo("subcategory", subcat).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                recyclerView.setVisibility(View.VISIBLE);
                                for (DocumentSnapshot read : task.getResult()) {

                                     max = read.get("max charge").toString();
                                     min = read.get("min charge").toString();
                                    charge = "₹" + min + " - " + "₹" + max;
                                    rating= read.get("rating").toString();
                                    name=read.get("name").toString();
                                    address=read.get("address").toString();
                                    profileurl=read.get("profile_url").toString();
                                    email=read.get("email").toString();
                                    vendorid=read.get("vendor_id").toString();
                                    shopname=read.get("Shop_name").toString();
                                    phone=read.get("phone").toString();
                                    Float rat=Float.parseFloat(rating);
                                    ServiceShow serviceShow = new ServiceShow(read.getId(), profileurl.toString(), read.get("name").toString(), read.get("subcategory").toString(), rat,charge,city,address,shopname,subcat,phone,email,min,max,vendorid);
                                    servicelist.add(serviceShow);

                                }
                                  adapter = new RecycleServiceDetails(ServicedetailsActivity.this, servicelist,ServicedetailsActivity.this);
                                    LinearLayoutManager Manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(Manager);
                                    recyclerView.setItemAnimator(new CustomItemAnimator());
                                    recyclerView.setAdapter(adapter);


                            }else {
                                imageView.setVisibility(View.VISIBLE);
                                title.setVisibility(View.VISIBLE);
                                description.setVisibility(View.VISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            imageView.setVisibility(View.VISIBLE);
                            title.setVisibility(View.VISIBLE);
                            description.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                Toast.makeText(ServicedetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void init() {
        recyclerView = findViewById(R.id.recycleserviceuser);
        refreshLayout = findViewById(R.id.swipeservice);
        imageView = findViewById(R.id.imageView10);
        title = findViewById(R.id.textView30);
        description = findViewById(R.id.textView32);
        text=findViewById(R.id.text1);
        back=findViewById(R.id.sdbk);
    }

    @Override
    public void onServiceClick(ServiceShow show) {
        Intent intent=new Intent(getApplicationContext(),Vendordetailuser.class);
        intent.putExtra("city",show.getCity());
        intent.putExtra("name",show.getName());
        intent.putExtra("address",show.getAddress());
        intent.putExtra("rating",show.getRating());
        intent.putExtra("shopname",show.getShopname());
        intent.putExtra("charge",show.getServicecharge());
        intent.putExtra("subcategory",subcat);
        intent.putExtra("phone",show.getPhone());
        intent.putExtra("email",show.getEmail());
        intent.putExtra("catid",catid);
        intent.putExtra("subcatid",subcatid);
        intent.putExtra("vendorid",show.getVendorid());
        intent.putExtra("profileurl",profileurl);
        intent.putExtra("min",show.getMincharge());
        intent.putExtra("max",show.getMaxcharge());
        startActivity(intent);

    }
}
