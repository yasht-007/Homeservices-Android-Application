package com.yash.homeservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.R;
import com.yash.homeservice.Usersidebooking.Bookingactivity;
import com.yash.homeservice.Usersidebooking.ServicedetailsActivity;
import com.yash.homeservice.adapter.Subcategoryadapter;
import com.yash.homeservice.modal.Subcategory;

import java.util.ArrayList;

public class Servicesmainactivity extends AppCompatActivity implements Subcategoryadapter.OnSubCategoryListner {

    private ArrayList<Subcategory> sublist;
    private Subcategoryadapter adapter;
    private ImageView bsArrow;
    private String catid,subcatid;
    private FirebaseFirestore firestore;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    public FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_main);
        init();
        user= FirebaseAuth.getInstance().getCurrentUser();
        catid=getIntent().getStringExtra("Categoryid");
        firestore=FirebaseFirestore.getInstance();
        bsArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshLayout.setRefreshing(true);
        getFromDatabase();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getFromDatabase();
            }
        });

    }

    private void getFromDatabase() {
        firestore.collection("category").document(catid).collection("subcat").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                refreshLayout.setRefreshing(false);
                sublist=new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots)
                {
                    Subcategory subcategory=new Subcategory(snapshot.getId(),snapshot.get("image").toString(),snapshot.get("title").toString());
                    sublist.add(subcategory);
                }
                 adapter=new Subcategoryadapter(getApplicationContext(),sublist, Servicesmainactivity.this);
                LinearLayoutManager Manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(Manager);
                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                refreshLayout.setRefreshing(false);
                String error=e.getMessage();
                Toast.makeText(Servicesmainactivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init()
    {
        bsArrow=findViewById(R.id.bs_arrow);
        recyclerView=findViewById(R.id.recyclerbeautysubcategory);
        refreshLayout=findViewById(R.id.refreshmain);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onSubCategoryClick(Subcategory category) {
        if (user!=null)
        {
            Intent intent=new Intent(Servicesmainactivity.this, ServicedetailsActivity.class);
            intent.putExtra("Catid",catid);
            intent.putExtra("Subcategoryid",category.getId());
            intent.putExtra("subcategory",category.getTitle());
            startActivity(intent);
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Servicesmainactivity.this);
            alertDialogBuilder.setMessage("Please Log in or register to continue")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

        }

    }
}
