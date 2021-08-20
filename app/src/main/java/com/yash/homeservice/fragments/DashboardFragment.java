package com.yash.homeservice.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.activities.Servicesmainactivity;
import com.yash.homeservice.activities.Searchactivity;
import com.yash.homeservice.adapter.MyRecyclerViewAdapter;
import com.yash.homeservice.adapter.RecycletopAdapter;
import com.yash.homeservice.modal.Servicebanner;
import com.yash.homeservice.modal.Servicerecycle;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements MyRecyclerViewAdapter.OnServiceClickListener , RecycletopAdapter.OnBannerClickListener {


    public DashboardFragment() {
        // Required empty public constructor
    }

    private SwipeRefreshLayout refreshLayout;
    private TextView tv_name,tv_email,tv_address,tv_phone;
    private FirebaseFirestore firestore;
    private Button btnSearch,btnSignin;
    private MyRecyclerViewAdapter recycleadapter;
    private RecycletopAdapter adapter;
    private ArrayList<Servicebanner> bannerlist;
    private RecyclerView recyclerView ,recyclertop;
    private ArrayList<Servicerecycle> recyclelist;
    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

       refreshLayout.setRefreshing(true);
        getBannerFromDatabase();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Searchactivity.class);
                startActivity(intent);
            }
        });


        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager rManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(rManager);
          firestore=FirebaseFirestore.getInstance();


         refreshLayout.setRefreshing(true);
          loadDatafromFirebase();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshLayout.setRefreshing(true);
                getBannerFromDatabase();
                refreshLayout.setRefreshing(true);
                loadDatafromFirebase();

            }
        });

    }

    private void getBannerFromDatabase() {
       firestore =FirebaseFirestore.getInstance();

        firestore.collection("bannersurl").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                refreshLayout.setRefreshing(false);
                bannerlist=new ArrayList<>();
                for (DocumentSnapshot snapshot :querySnapshot)
                {
                    Servicebanner servicebanner=new Servicebanner(snapshot.get("image").toString(),snapshot.getId());
                    bannerlist.add(servicebanner);
                }
                adapter=new RecycletopAdapter(getContext(),bannerlist,DashboardFragment.this);
                LinearLayoutManager Manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclertop.setLayoutManager(Manager);
                recyclertop.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDatafromFirebase() {

        firestore.collection("category").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                recyclelist = new ArrayList<>();
                refreshLayout.setRefreshing(false);
                for (DocumentSnapshot snapshot :querySnapshot)
                {
                    Servicerecycle servicerecycle=new Servicerecycle(snapshot.getId(),snapshot.get("title1").toString(),snapshot.get("title2").toString(),snapshot.get("description").toString(),snapshot.get("image").toString());
                    recyclelist.add(servicerecycle);
                }
                recycleadapter=new MyRecyclerViewAdapter(getContext(),recyclelist,DashboardFragment.this);
                recyclerView.setAdapter(recycleadapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void init(View view) {

        recyclertop=(RecyclerView)view.findViewById(R.id.rctop);
        tv_name=view.findViewById(R.id.profilename);
        tv_phone=view.findViewById(R.id.profilephone);
        tv_email=view.findViewById(R.id.profileemail);
        tv_address=view.findViewById(R.id.profileaddress);
        btnSearch=view.findViewById(R.id.btnsearch);
        refreshLayout=view.findViewById(R.id.refreshlayout);
    }

    @Override
    public void onServiceClick(Servicerecycle recycle) {

           String catId = recycle.getId();
            Intent pecIntent=new Intent(getContext(), Servicesmainactivity.class);
            pecIntent.putExtra("Categoryid",catId);
            startActivity(pecIntent);


    }

    @Override
    public void onBannerClick(Servicebanner banner) {
        Toast.makeText(getContext(), "welcome", Toast.LENGTH_SHORT).show();
    }

}
