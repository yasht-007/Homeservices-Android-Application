package com.yash.homeservice.Usersidebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.activities.Mapsactivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Orderdetailsactivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public TextView tvaddress,tvdate;
    public boolean clickstatus=false;
    public EditText flat,street,city,pincode,state;
    public String Fulladdress,sflat,scity,sstate,spincode,sstreet,uid,subcat,vendorid;
    private Button btnbook,choosedate,proceed,btnchangeadd,btnsaveadd;
    private RadioGroup addtype,timing;
    private RadioButton add,time;
    private ImageView back;
    public  String a,t,username,useremail,userphone;
    public Float rating;
    public String catid, subcatid;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetailsactivity);
        init();


        catid=getIntent().getStringExtra("Catid");
        subcat=getIntent().getStringExtra("subcat");
        vendorid=getIntent().getStringExtra("vendorid");
        subcatid=getIntent().getStringExtra("Subcategoryid");



        firestore=FirebaseFirestore.getInstance();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("kaam25_users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    scity=task.getResult().get("city").toString();
                    useremail=task.getResult().get("email").toString();
                    username=task.getResult().get("name").toString();
                    userphone=task.getResult().get("phone").toString();
                   Fulladdress=task.getResult().get("address").toString();
                   tvaddress.setText(Fulladdress);
                }else {
                    Toast.makeText(Orderdetailsactivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Orderdetailsactivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnchangeadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Orderdetailsactivity.this);
                dialog.setContentView(R.layout.dialogaddress);
                Button cancel;
                flat=dialog.findViewById(R.id.dno);
                street=dialog.findViewById(R.id.dstreet);
                city=dialog.findViewById(R.id.dcity);
                pincode=dialog.findViewById(R.id.dpincode);
                state=dialog.findViewById(R.id.dstate);
                proceed=dialog.findViewById(R.id.submitadd);
                cancel=dialog.findViewById(R.id.dcancel);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sflat=flat.getText().toString().trim();
                        sstreet=street.getText().toString().trim();
                        scity=city.getText().toString().toLowerCase().trim();
                        spincode=pincode.getText().toString().trim();
                        sstate=state.getText().toString().trim();

                        if (TextUtils.isEmpty(sflat))
                        {
                            Toast.makeText(Orderdetailsactivity.this, "Please enter house number", Toast.LENGTH_SHORT).show();
                            flat.requestFocus();
                        }
                        else if(TextUtils.isEmpty(sstreet))
                        {
                            Toast.makeText(Orderdetailsactivity.this, "Please enter street ", Toast.LENGTH_SHORT).show();
                            street.requestFocus();
                        }else if (TextUtils.isEmpty(scity))
                        {
                            Toast.makeText(Orderdetailsactivity.this, "Please enter your city", Toast.LENGTH_SHORT).show();
                            city.requestFocus();
                        }else if (TextUtils.isEmpty(spincode))
                        {
                            Toast.makeText(Orderdetailsactivity.this, "Please enter pincode number", Toast.LENGTH_SHORT).show();
                            pincode.requestFocus();
                        }else if (spincode.length()!=6)
                        {
                            Toast.makeText(Orderdetailsactivity.this, "Pincode length must be 6", Toast.LENGTH_SHORT).show();
                            pincode.requestFocus();
                        }else if (TextUtils.isEmpty(sstate))
                        {
                            Toast.makeText(Orderdetailsactivity.this, "Please select your state", Toast.LENGTH_SHORT).show();
                            state.requestFocus();
                        }else {
                            clickstatus=true;
                            Fulladdress= (flat.getText().toString().trim()+","+street.getText().toString().trim()+","+city.getText().toString().trim()+","+state.getText().toString().trim()+"-"+pincode.getText().toString().trim());
                            dialog.dismiss();
                            tvaddress.setText(Fulladdress);
                        }

                    }

                });
                dialog.setCancelable(false);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            }
        });

        btnsaveadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickstatus==true)
                {
                    Map<String,Object> map=new HashMap<>();
                    map.put("address",tvaddress.getText().toString().trim());
                    map.put("city",scity);
                    firestore.collection("kaam25_users").document(uid).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Orderdetailsactivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(Orderdetailsactivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Orderdetailsactivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(Orderdetailsactivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addtype.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(Orderdetailsactivity.this, "Please select address type", Toast.LENGTH_SHORT).show();
                }else {
                    int selectedid= addtype.getCheckedRadioButtonId();
                    add=findViewById(selectedid);
                    a= add.getText().toString();
                }

                if (timing.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(Orderdetailsactivity.this, "Please select timing for service delivery", Toast.LENGTH_SHORT).show();
                }else {
                    int timeid= timing.getCheckedRadioButtonId();
                    time=findViewById(timeid);
                    t= time.getText().toString();
                }

                if (tvdate.getText().equals(null) || tvdate.getText().equals("Date"))
                {
                    Toast.makeText(Orderdetailsactivity.this, "Please select delivery date", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(Orderdetailsactivity.this,Finalorderuseractivity.class);
                    intent.putExtra("vendorid",vendorid);
                    intent.putExtra("subcat",subcat);
                    intent.putExtra("userid",uid);
                    intent.putExtra("username",username);
                    intent.putExtra("useremail",useremail);
                    intent.putExtra("userphone",userphone);
                    intent.putExtra("useraddress",tvaddress.getText().toString());
                    intent.putExtra("orderdate",tvdate.getText().toString());
                    intent.putExtra("orderaddtype",a);
                    intent.putExtra("ordertime",t);
                    startActivity(intent);
                }

            }
        });
    }

    private void init() {
        tvaddress=findViewById(R.id.fsaddress);
        btnbook=findViewById(R.id.btnbk);
        back=findViewById(R.id.bkarrow);
        tvdate=findViewById(R.id.tvdate);
        btnsaveadd=findViewById(R.id.btnsvadd);
        btnchangeadd=findViewById(R.id.btnchadd);
        choosedate=findViewById(R.id.btnsel);
        addtype=findViewById(R.id.rgadd);
        timing=findViewById(R.id.rgtime);
    }

    private void showDatePickerDialog()
    {
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog datePickerDialog=new DatePickerDialog(Orderdetailsactivity.this,Orderdetailsactivity.this,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, 0);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DATE, 8);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date= dayOfMonth +"-"+month+"-"+year;
        tvdate.setText(date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
