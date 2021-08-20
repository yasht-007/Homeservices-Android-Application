package com.yash.homeservice.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yash.homeservice.R;

public class FirstpageActivity extends AppCompatActivity {

    private Button btnBrowse,btnSignin;
    private boolean status=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        init();
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstpageActivity.this,DashboardActivity.class);
                intent.putExtra("userstatus",true);
                intent.putExtra("firstime1",status);
                startActivity(intent);
                finish();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirstpageActivity.this,Registeractivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init()
    {
        btnBrowse=findViewById(R.id.btnbrowse);
        btnSignin=findViewById(R.id.btnsignin);

        status=getIntent().getBooleanExtra("firstime",false);
    }
}
