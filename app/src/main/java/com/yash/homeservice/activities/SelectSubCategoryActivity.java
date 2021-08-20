package com.yash.homeservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.yash.homeservice.adapter.SpinnersubAdapter;
import com.yash.homeservice.modal.Vendorservicelist;

import java.util.ArrayList;
import java.util.List;

public class SelectSubCategoryActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button btnadd;
    private String id,Services;
    private String imageurl;
    private String srid;
    private String catid,cat;
    private String Subcategory;
    private Vendorservicelist vendorservicelist;
    private ArrayList<Vendorservicelist> servicelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_category);
        init();
        catid=getIntent().getStringExtra("Mainid");
        cat=getIntent().getStringExtra("Main");


        servicelist=new ArrayList<>();
        Vendorservicelist v=new Vendorservicelist("defsub","Select sub category","https://firebasestorage.googleapis.com/v0/b/homeservice-1e940.appspot.com/o/icons%2Fhomeservices-removebg-preview.png?alt=media&token=af2d688f-907c-45f5-826a-fcd53ae98377");
        servicelist.add(v);
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        firestore.collection("category").document(catid).collection("subcat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();

                    for (DocumentSnapshot read : queryDocumentSnapshots.getDocuments())
                    {
                        id=read.getId();
                        Services= read.get("title").toString().trim();
                        imageurl=read.get("image").toString();
                        vendorservicelist=new Vendorservicelist(id,Services,imageurl);
                        servicelist.add(vendorservicelist);

                    }
                    SpinnersubAdapter spinnersubAdapter=new SpinnersubAdapter(getApplicationContext(),servicelist);
                    spinner.setAdapter(spinnersubAdapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectSubCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                srid= servicelist.get(position).getId();
                if (parent.getSelectedItem().equals("Select sub category"))
                {
                   Subcategory=parent.getSelectedItem().toString().trim();
                }else {
                    Subcategory=parent.getSelectedItem().toString().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SelectSubCategoryActivity.this, "please select the service that you will provide", Toast.LENGTH_SHORT).show();

            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Subcategory.equals("Select sub category"))
                {
                    Toast.makeText(SelectSubCategoryActivity.this, "Please select the service that you will provide", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(SelectSubCategoryActivity.this,VerificationActivity.class);
                    intent.putExtra("Mainid",catid);
                    intent.putExtra("Main",cat);
                    intent.putExtra("Subid",srid);
                    intent.putExtra("Sub",Subcategory);
                    startActivity(intent);
                }
            }
        });
    }

    private void init()
    {
        spinner=findViewById(R.id.spinner2);
        btnadd=findViewById(R.id.subproceedbtn);
    }
}
