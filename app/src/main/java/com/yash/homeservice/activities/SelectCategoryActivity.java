package com.yash.homeservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.R;
import com.yash.homeservice.modal.Spinneradapter;
import com.yash.homeservice.modal.Vendorservicelist;

import java.util.ArrayList;


public class SelectCategoryActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button btnadd;
    private String id,Services;
    private boolean selectionStatus=false;
    private String srid;
    private String imageurl;
    private String Maincategory;
    private Vendorservicelist vendorservicelist;
    private ArrayList<Vendorservicelist> servicelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        init();

        servicelist=new ArrayList<>();
        Vendorservicelist v=new Vendorservicelist("def","Select any category","https://firebasestorage.googleapis.com/v0/b/homeservice-1e940.appspot.com/o/category%2Fhomeservices.jpg?alt=media&token=521d61b0-e3f0-4fa7-b28f-186c8b200b70");
        servicelist.add(v);
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore.collection("category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();

                    for (DocumentSnapshot read : queryDocumentSnapshots.getDocuments())
                    {
                        id=read.getId();
                        Services= read.get("title2").toString().trim();
                        imageurl=read.get("icon").toString();
                        vendorservicelist=new Vendorservicelist(id,Services,imageurl);
                        servicelist.add(vendorservicelist);

                    }
                    Spinneradapter spinneradapter=new Spinneradapter(getApplicationContext(),servicelist);
                    spinner.setAdapter(spinneradapter);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SelectCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                srid= servicelist.get(position).getId();
                if (parent.getSelectedItem().equals("Select any category"))
                {

                }else {
                  Maincategory=parent.getSelectedItem().toString().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectionStatus=true;
                Toast.makeText(SelectCategoryActivity.this, "please select the service that you will provide", Toast.LENGTH_SHORT).show();

            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Maincategory.equals("Select any category"))
                {
                    Toast.makeText(SelectCategoryActivity.this, "Please select the service that you will provide", Toast.LENGTH_SHORT).show();
                }else {
                    if (selectionStatus==true)
                    {
                        Toast.makeText(SelectCategoryActivity.this, "Please select the service that you will provide", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(SelectCategoryActivity.this, SelectSubCategoryActivity.class);
                        intent.putExtra("Mainid", srid);
                        intent.putExtra("Main", Maincategory);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private void init()
    {
        spinner=findViewById(R.id.spinner1);
        btnadd=findViewById(R.id.addnproceedbtn);
    }
}
