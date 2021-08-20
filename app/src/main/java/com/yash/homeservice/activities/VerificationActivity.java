package com.yash.homeservice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.yash.homeservice.R;
import com.yash.homeservice.fragments.DashboardFragment;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {

    private TextView tvCat, tvSubcat, tvMin, tvMax;
    private TextView etAadhar;
    private ProgressBar progressBar;
    private Button finish;
    public String shopname;
    private String name, profileurl, address, phone, email, pincode,city;
    private FirebaseFirestore firebaseFirestore;
    private String cat, subcat, catid, subcatid, min, max, uid, aadhar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        init();

        final FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseAuth.getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        catid = getIntent().getStringExtra("Mainid");
        subcatid = getIntent().getStringExtra("Subid");
        cat = getIntent().getStringExtra("Main");
        subcat = getIntent().getStringExtra("Sub");


        firebaseFirestore.collection("kaam25_users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot queryDocumentSnapshots = task.getResult();

                    min = queryDocumentSnapshots.get("min charge").toString();
                    max = queryDocumentSnapshots.get("max charge").toString();
                    name = queryDocumentSnapshots.get("name").toString();
                    address = queryDocumentSnapshots.get("address").toString();
                    profileurl = queryDocumentSnapshots.get("profile_url").toString();
                    phone = queryDocumentSnapshots.get("phone").toString();
                    email = queryDocumentSnapshots.get("email").toString();
                    aadhar = queryDocumentSnapshots.get("aadhar_no").toString();
                    pincode = queryDocumentSnapshots.get("pincode").toString();
                    city=queryDocumentSnapshots.get("city").toString();
                    shopname=queryDocumentSnapshots.get("Shop name").toString();

                    tvMin.setText(min);
                    tvMin.setVisibility(View.VISIBLE);
                    tvMax.setText(max);
                    tvMax.setVisibility(View.VISIBLE);
                    etAadhar.setText(aadhar);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        tvCat.setText(cat);
        tvCat.setVisibility(View.VISIBLE);
        tvSubcat.setText(subcat);
        tvSubcat.setVisibility(View.VISIBLE);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String aadharno = etAadhar.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                final Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("address", address);
                map.put("email", email);
                map.put("phone", phone);
                map.put("min charge", min);
                map.put("max charge", max);
                map.put("profile_url", profileurl);
                map.put("maincategory", cat);
                map.put("subcategory", subcat);
                map.put("pincode", pincode);
                map.put("city",city);
                map.put("Shop_name",shopname);
                map.put("rating",0);
                map.put("vendor_id",uid);
                firebaseFirestore.collection("kaam25_users").document(uid).collection("services").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                        {
                            map.clear();
                            map.put("facility",subcat);
                            firebaseFirestore.collection("kaam25_users").document(uid).update(map);
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VerificationActivity.this);
                            alertDialogBuilder.setMessage("Congrats! Your Service is added Successfully ");
                            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(VerificationActivity.this, VendorDashboardActivity.class);
                                    intent.putExtra("verstatus", true);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }else {
                            Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                Map<String,Object> nmap=new HashMap<>();
                nmap.put("id",uid);
                firebaseFirestore.collection("category").document(catid).collection("subcat").document(subcatid).collection("service_providers").document(uid).set(nmap);

            }

        });
    }

    private void init() {
        tvCat = findViewById(R.id.tvcat);
        tvSubcat = findViewById(R.id.tvsubcat);
        tvMax = findViewById(R.id.tvmax);
        tvMin = findViewById(R.id.tvmin);
        etAadhar = findViewById(R.id.etaadhar);
        finish = findViewById(R.id.verifyidentitybtn);
        progressBar = findViewById(R.id.progressBarid);
    }

}
