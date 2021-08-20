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
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.adapter.UsercartAdapter;
import com.yash.homeservice.adapter.VendorcartAdapter;
import com.yash.homeservice.modal.Usercart;
import com.yash.homeservice.modal.Vendorcart;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InprogressFragment extends Fragment {

    private int status;
    public ArrayList<Vendorcart> vcartlist;
    public SwipeRefreshLayout refreshLayout;
    private UsercartAdapter adapter;
    private ArrayList<Usercart> cartlist;
    private FirebaseFirestore firestore;
    private ImageView imageView;
    public String uid;
    private RecyclerView recyclerView;
    private TextView message,noorders;

    public InprogressFragment(int status) {
        this.status=status;
        // Required empty public constructor
    }
     private Button btnbook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inprogress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refreshLayout.setRefreshing(true);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore=FirebaseFirestore.getInstance();
        if (status==0) {

            firestore.collection("orders").whereEqualTo("userid",uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    cartlist=new ArrayList<>();
                    int size= queryDocumentSnapshots.size();
                    if (size==0)
                    {
                        refreshLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        message.setVisibility(View.VISIBLE);
                        noorders.setVisibility(View.VISIBLE);
                        btnbook.setVisibility(View.VISIBLE);
                    }

                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DocumentSnapshot read : queryDocumentSnapshots) {
                        String image=read.get("profileurl").toString();
                        String name=read.get("vendorname").toString();
                        String service=read.get("servicetype").toString();
                        String number=read.get("vendorphone").toString();
                        String ucode=read.get("uniquecode").toString();
                        String timing=read.get("ordertime").toString();
                        String date=read.get("orderdate").toString();
                        String charge=read.get("vendorcharge").toString();
                        String orderstatus=read.get("orderstatus").toString();
                        String id=read.getId();

                        Usercart usercart=new Usercart(image,name,service,number,id,timing,date,ucode,charge,orderstatus);
                        cartlist.add(usercart);
                    }

                    adapter=new UsercartAdapter(getContext(),cartlist);
                    LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(manager);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                    noorders.setVisibility(View.VISIBLE);
                    btnbook.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            btnbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DashboardActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
        else {
            firestore.collection("orders").whereEqualTo("vendorid",uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    vcartlist=new ArrayList<>();
                    int size= queryDocumentSnapshots.size();
                    if (size==0)
                    {
                        refreshLayout.setRefreshing(false);
                        message.setVisibility(View.VISIBLE);
                        message.setTextSize(18f);
                        message.setText(" You have no orders currently\n    wait for sometime until \n        an order appears");
                        noorders.setVisibility(View.VISIBLE);
                    }else {
                        refreshLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        for (DocumentSnapshot read : queryDocumentSnapshots) {
                            String name=read.get("username").toString();
                            String service=read.get("servicetype").toString();
                            String phone=read.get("userphone").toString();
                            String time=read.get("ordertime").toString();
                            String date=read.get("orderdate").toString();
                            String code=read.get("uniquecode").toString();
                            String orderstatus=read.get("orderstatus").toString();
                            String address=read.get("useraddress").toString();
                            String charge=read.get("vendorcharge").toString();
                            String id=read.getId();

                            Vendorcart vendorcart=new Vendorcart(name,service,id,time,date,phone,code,charge,orderstatus,address);
                            vcartlist.add(vendorcart);
                        }
                        VendorcartAdapter adapter=new VendorcartAdapter(getContext(),vcartlist);
                        LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(manager);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    refreshLayout.setRefreshing(false);
                    message.setVisibility(View.VISIBLE);
                    message.setTextSize(18f);
                    message.setText(" You have no orders currently\n    wait for sometime until \n        an order appears");
                    noorders.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    private void refresh() {
        if (status==0)
        {

            firestore.collection("orders").whereEqualTo("userid",uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    cartlist=new ArrayList<>();
                    int size= queryDocumentSnapshots.size();
                    if (size==0)
                    {
                        refreshLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        message.setVisibility(View.VISIBLE);
                        noorders.setVisibility(View.VISIBLE);
                        btnbook.setVisibility(View.VISIBLE);
                    }

                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DocumentSnapshot read : queryDocumentSnapshots) {
                        String image=read.get("profileurl").toString();
                        String name=read.get("vendorname").toString();
                        String service=read.get("servicetype").toString();
                        String number=read.get("vendorphone").toString();
                        String rating=read.get("vendorrating").toString();
                        String ucode=read.get("uniquecode").toString();
                        String timing=read.get("ordertime").toString();
                        String date=read.get("orderdate").toString();
                        String charge=read.get("vendorcharge").toString();
                        String orderstatus=read.get("orderstatus").toString();
                        String id=read.getId();

                        Usercart usercart=new Usercart(image,name,service,number,id,timing,date,ucode,charge,orderstatus);
                        cartlist.add(usercart);
                    }

                    adapter=new UsercartAdapter(getContext(),cartlist);
                    LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    refreshLayout.setRefreshing(false);
                    recyclerView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                    noorders.setVisibility(View.VISIBLE);
                    btnbook.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            firestore.collection("orders").whereEqualTo("vendorid",uid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    vcartlist=new ArrayList<>();
                    int size= queryDocumentSnapshots.size();
                    if (size==0)
                    {
                        refreshLayout.setRefreshing(false);
                        message.setVisibility(View.VISIBLE);
                        message.setTextSize(18f);
                        message.setText(" You have no orders currently\n    wait for sometime until \n        an order appears");
                        noorders.setVisibility(View.VISIBLE);
                    }else {
                        refreshLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.VISIBLE);
                        for (DocumentSnapshot read : queryDocumentSnapshots) {
                            String name=read.get("username").toString();
                            String service=read.get("servicetype").toString();
                            String phone=read.get("userphone").toString();
                            String time=read.get("ordertime").toString();
                            String date=read.get("orderdate").toString();
                            String code=read.get("uniquecode").toString();
                            String orderstatus=read.get("orderstatus").toString();
                            String address=read.get("useraddress").toString();
                            String charge=read.get("vendorcharge").toString();
                            String id=read.getId();

                            Vendorcart vendorcart=new Vendorcart(name,service,id,time,date,phone,code,charge,orderstatus,address);
                            vcartlist.add(vendorcart);
                        }
                        VendorcartAdapter adapter=new VendorcartAdapter(getContext(),vcartlist);
                        LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(manager);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    refreshLayout.setRefreshing(false);
                    message.setVisibility(View.VISIBLE);
                    message.setTextSize(18f);
                    message.setText(" You have no orders currently\n    wait for sometime until \n        an order appears");
                    noorders.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    private void init(View view)
    {
        imageView=view.findViewById(R.id.imageView5);
        recyclerView=view.findViewById(R.id.recyclercart);
        btnbook=view.findViewById(R.id.bookservice);
        message=view.findViewById(R.id.tv_message);
        noorders=view.findViewById(R.id.tv_noorders);
        refreshLayout=view.findViewById(R.id.swcart);
    }
}
