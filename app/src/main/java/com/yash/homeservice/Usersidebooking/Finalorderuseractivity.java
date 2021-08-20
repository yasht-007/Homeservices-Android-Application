package com.yash.homeservice.Usersidebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Finalorderuseractivity extends AppCompatActivity {

    public String username,useremail,useraddress,userid,userphone,ucode;
    public String vendorid,vendorname,vendoraddress,vendoremail,vendorprofile,min,max,vendorphone,vendorshop,vendorcharge;
    public String vendorrating;
    public int dcodestatus;
    private FirebaseFirestore firestore;
    private ImageView back;
    private TextView tvaddress,tvshop,tvcharge,tvservice,tvdate,tvtime,tvname,tvnum;
    private CheckBox checkBox;
    private EditText promocode;
    private Button apply,orderconfirm;
    public String subcat,orderdate,ordertime,orderaddtype,promotioncode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalorderuseractivity);

        init();
        username=getIntent().getStringExtra("username");
        useremail=getIntent().getStringExtra("useremail");
        useraddress=getIntent().getStringExtra("useraddress");
        userid=getIntent().getStringExtra("userid");
        userphone=getIntent().getStringExtra("userphone");
        vendorid=getIntent().getStringExtra("vendorid");
        subcat=getIntent().getStringExtra("subcat");
        orderdate=getIntent().getStringExtra("orderdate");
        ordertime=getIntent().getStringExtra("ordertime");
        orderaddtype=getIntent().getStringExtra("orderaddtype");

        FirebaseFirestore.getInstance().collection("kaam25_users").document(vendorid).collection("services").whereEqualTo("subcategory",subcat).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                 for (DocumentSnapshot read: task.getResult())
                 {
                     vendorname=read.get("name").toString();
                     vendoraddress=read.get("address").toString();
                     min=read.get("min charge").toString();
                     max=read.get("max charge").toString();
                     vendorcharge="₹"+min+" - "+"₹"+max;
                     vendorphone=read.get("phone").toString();
                     vendoremail=read.get("email").toString();
                     vendorrating=read.get("rating").toString();
                     vendorshop=read.get("Shop_name").toString();
                     vendorprofile=read.get("profile_url").toString();

                     tvshop.setText(vendorshop);
                     tvshop.setVisibility(View.VISIBLE);
                     tvcharge.setText(vendorcharge);
                     tvcharge.setVisibility(View.VISIBLE);
                     tvaddress.setText(useraddress);
                     tvaddress.setVisibility(View.VISIBLE);
                     tvdate.setText(orderdate);
                     tvdate.setVisibility(View.VISIBLE);
                     tvtime.setText(ordertime);
                     tvtime.setVisibility(View.VISIBLE);
                     tvname.setText(vendorname);
                     tvname.setVisibility(View.VISIBLE);
                     tvnum.setText(vendorphone);
                     tvnum.setVisibility(View.VISIBLE);
                     tvservice.setText(subcat);
                     tvservice.setVisibility(View.VISIBLE);

                 }
                }else {
                    Toast.makeText(Finalorderuseractivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        firestore=FirebaseFirestore.getInstance();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promotioncode=promocode.getText().toString();
                if (promotioncode.equals("FIRST100"))
                {
                    int Min= Integer.parseInt(min);
                    int Max= Integer.parseInt(max);
                    Min=Min-100;
                    Max=Max-100;
                    vendorcharge="₹"+Min+" - "+"₹"+Max;
                    tvcharge.setText(vendorcharge);
                    promocode.setFocusable(false);
                    apply.setClickable(false);
                    dcodestatus=1;

                }else {
                    promocode.setError("Discount code is not valid");
                }
            }
        });

        Random r = new Random();
        final List<Integer> codes = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            int x = r.nextInt(9999);
            while (codes.contains(x))
                x = r.nextInt(9999);
            codes.add(x);
        }
        ucode = String.format("%04d", codes.get(0));

        orderconfirm.setOnClickListener(new View.OnClickListener() {

            String rat=""+vendorrating;
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked())
                {
                    AlertDialog.Builder abuilder = new AlertDialog.Builder(Finalorderuseractivity.this);
                    abuilder.setTitle("Confirm booking")
                            .setMessage("Are you confirm to book order?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String,Object>  map=new HashMap<>();
                                    map.put("userid",userid);
                                    map.put("username",username);
                                    map.put("useraddress",useraddress);
                                    map.put("useremail",useremail);
                                    map.put("userphone",userphone);
                                    map.put("vendorid",vendorid);
                                    map.put("vendorname",vendorname);
                                    map.put("vendoraddress",vendoraddress);
                                    map.put("vendoremail",vendoremail);
                                    map.put("vendorphone",vendorphone);
                                    map.put("vendorcharge",vendorcharge);
                                    map.put("vendorshop",vendorshop);
                                    map.put("vendorrating",vendorrating);
                                    map.put("servicetype",subcat);
                                    map.put("profileurl",vendorprofile);
                                    map.put("orderaddtype",orderaddtype);
                                    map.put("orderdate",orderdate);
                                    map.put("ordertime",ordertime);
                                    map.put("uniquecode",ucode);
                                    map.put("reschedulestatus","no");
                                    map.put("orderstatus","pending");
                                    if (dcodestatus==1)
                                    {
                                        map.put("discount","FIRST100");
                                    }else {
                                        map.put("discount","no");
                                    }
                                    firestore.collection("orders").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful())
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Finalorderuseractivity.this);
                                                builder.setTitle("Order Successful")
                                                        .setMessage("Your order is successful now wait until service provider picks your order. You can check order status in cart")
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent=new Intent(Finalorderuseractivity.this, DashboardActivity.class);
                                                                intent.putExtra("orderstatus",true);
                                                                startActivity(intent);
                                                            }
                                                        }).setCancelable(false).show();

                                            }else {
                                                Toast.makeText(Finalorderuseractivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Finalorderuseractivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).show();

                }else {
                    Toast.makeText(Finalorderuseractivity.this, "Please agree to our terms and conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });

      back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Finalorderuseractivity.super.onBackPressed();
    }
        });
    }

    private void init() {
        back=findViewById(R.id.bkarrow);
        tvaddress=findViewById(R.id.odaddress);
        tvshop=findViewById(R.id.shname);
        tvcharge=findViewById(R.id.odcharge);
        tvdate=findViewById(R.id.oddt);
        tvtime=findViewById(R.id.odtime);
        promocode=findViewById(R.id.dcode);
        apply=findViewById(R.id.btnapply);
        checkBox=findViewById(R.id.cbtnc);
        tvservice=findViewById(R.id.servicetype);
        tvname=findViewById(R.id.odname);
        tvnum=findViewById(R.id.odnum);
        orderconfirm=findViewById(R.id.button2);
    }
}
