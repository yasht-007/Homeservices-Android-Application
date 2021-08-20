package com.yash.homeservice.Usersidebooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yash.homeservice.R;

public class userbook extends AppCompatActivity {

    private String title,name,address1,city,street,pincode,state,note;
    private TextView tvFlservice;
    private ImageView finalArrow;
    private Button btnBook;
    private String deliveryTime;
    private RadioButton rbHome,rbOffice;
    private EditText fname,faddress1,fcity,fstreet,fpincode,fstate,fnote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalservice);
        /*init();
        title=getIntent().getStringExtra("Title");
        tvFlservice.setText(title);

        finalArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=fname.getText().toString().trim();
                address1=faddress1.getText().toString().trim();
                street=fstreet.getText().toString().trim();
                city=fcity.getText().toString().trim();
                pincode=fpincode.getText().toString().trim();
                state=fstate.getText().toString().trim();
                note=fnote.getText().toString().trim();


                if (TextUtils.isEmpty(name))
                {
                    fname.setError("Please enter Name");
                    fname.requestFocus();
                }else if(name.length()>30)
                {
                    fname.setError("Name length must not greater than 30 characters ");
                    fname.requestFocus();
                }
                else if (TextUtils.isEmpty(address1))
                {

                    faddress1.setError("Please enter house number");
                    faddress1.requestFocus();
                }
                else if(TextUtils.isEmpty(street))
                {
                    fstreet.setError("Please enter street ");
                    fstreet.requestFocus();
                }else if (TextUtils.isEmpty(city))
                {
                    fcity.setError("Please enter your city");
                    fcity.requestFocus();
                }else if (TextUtils.isEmpty(pincode))
                {
                    fpincode.setError("Please enter pincode number");
                    fpincode.requestFocus();
                }else if (pincode.length()!=6)
                {
                    fpincode.setError("Pincode length must be 6");
                    fpincode.requestFocus();
                }else if (TextUtils.isEmpty(state))
                {
                    fstate.setError("Please select your state");
                    fstate.requestFocus();
                }else {

                    if (rbOffice.isChecked())
                    {
                        deliveryTime="office";
                    }else {
                        deliveryTime="home";
                    }
                    Intent intent=new Intent(userbook.this,Finalbook.class);
                    startActivity(intent);
                    finish();
                }


            }
        });*/
    }
    private void init()
    {/*
        tvFlservice=findViewById(R.id.tv_flservice);
        fname=findViewById(R.id.fetname);
        faddress1=findViewById(R.id.fetaddress1);
        fstreet=findViewById(R.id.fetstreet);
        fcity=findViewById(R.id.fetcity);
        fstate=findViewById(R.id.fetstate);
        fpincode=findViewById(R.id.fetpincode);
        fnote=findViewById(R.id.anynote);
        finalArrow=findViewById(R.id.final_arrow);
        btnBook=findViewById(R.id.btnbook);
        rbHome=findViewById(R.id.rb_home);
        rbOffice=findViewById(R.id.rb_office);*/
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to Exit?");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
