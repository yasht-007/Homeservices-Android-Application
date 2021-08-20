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
import com.yash.homeservice.Global.Myservices;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.SelectCategoryActivity;
import com.yash.homeservice.adapter.Myservicesadapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyServicesFragment extends Fragment {

    private Button addService;
    private ImageView imageView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv1,tv2;
    private Button btn;
    private ArrayList<Myservices> myserviceslist;
    private RecyclerView recyclerView;;
    private String uid,category,subcategory,mincharge,maxcharge;
    private FirebaseFirestore firestore;


    public MyServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_services, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        init(view);
        firestore=FirebaseFirestore.getInstance();
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firestore.collection("kaam25_users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {

                            swipeRefreshLayout.setRefreshing(false);
                            DocumentSnapshot read=task.getResult();
                            subcategory=read.get("facility").toString();

                            if (subcategory.equals("vendor"))
                            {
                                recyclerView.setVisibility(View.INVISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                tv1.setVisibility(View.VISIBLE);
                                tv2.setVisibility(View.VISIBLE);
                                addService.setVisibility(View.VISIBLE);

                            }else {
                                firestore.collection("kaam25_users").document(uid).collection("services").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        swipeRefreshLayout.setRefreshing(false);
                                        myserviceslist=new ArrayList<>();

                                        for (DocumentSnapshot read: queryDocumentSnapshots)
                                        {

                                            category= read.get("maincategory").toString();
                                            mincharge=read.get("min charge").toString();
                                            maxcharge=read.get("max charge").toString();
                                            subcategory= read.get("subcategory").toString();

                                            String ser= mincharge+" - "+maxcharge;
                                            Myservices myservices=new Myservices(category,subcategory,ser);
                                            myserviceslist.add(myservices);
                                        }


                                        Myservicesadapter adapter=new Myservicesadapter(getContext(),myserviceslist);
                                        LinearLayoutManager rManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                        recyclerView.setLayoutManager(rManager);
                                        recyclerView.setAdapter(adapter);
                                    }
                                });

                            }

                        }else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
        firestore.collection("kaam25_users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {

                    swipeRefreshLayout.setRefreshing(false);
                    DocumentSnapshot read=task.getResult();
                     subcategory=read.get("facility").toString();

                     if (subcategory.equals("vendor"))
                     {
                         recyclerView.setVisibility(View.INVISIBLE);
                         imageView.setVisibility(View.VISIBLE);
                         tv1.setVisibility(View.VISIBLE);
                         tv2.setVisibility(View.VISIBLE);
                         addService.setVisibility(View.VISIBLE);


                     }else {
                     firestore.collection("kaam25_users").document(uid).collection("services").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                         @Override
                         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                             swipeRefreshLayout.setRefreshing(false);
                             myserviceslist=new ArrayList<>();

                             for (DocumentSnapshot read: queryDocumentSnapshots)
                             {

                                 category= read.get("maincategory").toString();
                                 mincharge=read.get("min charge").toString();
                                 maxcharge=read.get("max charge").toString();
                                 subcategory= read.get("subcategory").toString();

                                 String ser= mincharge+" - "+maxcharge;

                                 Myservices myservices=new Myservices(category,subcategory,ser);
                                 myserviceslist.add(myservices);
                             }


                             Myservicesadapter adapter=new Myservicesadapter(getContext(),myserviceslist);
                             LinearLayoutManager rManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                             recyclerView.setLayoutManager(rManager);
                             recyclerView.setAdapter(adapter);
                         }
                     });

                     }

                }else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectCategoryActivity.class));
            }
        });
    }

    private void init(View view)
    {
        addService=view.findViewById(R.id.buttonaddser);
        recyclerView=view.findViewById(R.id.rcmyservices);
        imageView=view.findViewById(R.id.imageView8);
        tv1=view.findViewById(R.id.textView24);
        tv2=view.findViewById(R.id.textView27);
        swipeRefreshLayout=view.findViewById(R.id.swipemyservice);
    }
}
