package com.yash.homeservice.Usersidebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.R;
import com.yash.homeservice.adapter.ServiceAdapter;

import java.util.ArrayList;

public class Bookingactivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<Serviceinner> innerList;
    private TextView Services;
    private FirebaseFirestore firestore;
    private Button btnBook;
    private String id,title,image,min,max;
    private SwipeRefreshLayout refreshLayout;
    public Float rating;
    public String vendorid,subcat, catid, subcatid;
    private ServiceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingactivity);
        init();
        firestore=FirebaseFirestore.getInstance();

        catid=getIntent().getStringExtra("Catid");
        subcatid=getIntent().getStringExtra("Subcategoryid");
        vendorid=getIntent().getStringExtra("vendorid");
        subcat=getIntent().getStringExtra("subcat");

        refreshLayout.setRefreshing(true);
        getFromDatabase();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getFromDatabase();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Bookingactivity.this, Orderdetailsactivity.class);
                intent.putExtra("Catid",catid);
                intent.putExtra("Subcategoryid",subcatid);
                intent.putExtra("vendorid",vendorid);
                intent.putExtra("subcat",subcat);
                startActivity(intent);
                finish();

            }
        });

    }

    private void getFromDatabase() {
        firestore.collection("category").document(catid).collection("subcat").document(subcatid).collection("Banners").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                refreshLayout.setRefreshing(false);
                innerList=new ArrayList<>();
                for (DocumentSnapshot snapshot: queryDocumentSnapshots)
                {
                    id=snapshot.getId();
                    title=snapshot.get("title").toString();
                    image=snapshot.get("image").toString();
                }

                Services.setText(title);
                innerList.add(new Serviceinner(id,image));
                adapter=new ServiceAdapter(innerList,Bookingactivity.this);
                viewPager.setAdapter(adapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error=e.getMessage();
                Toast.makeText(Bookingactivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init()
    {
        Services=findViewById(R.id.tv_services);
        viewPager=findViewById(R.id.vp_service);
        btnBook=findViewById(R.id.btnbook);
        refreshLayout=findViewById(R.id.refreshlayoutsub);
    }


}
