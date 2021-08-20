package com.yash.homeservice.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.Registeractivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private CircleImageView imageView;
    private Uri uri=null;
    private String shopName,pincode;
    private EditText mincharge,maxcharge,shopname;
    private ProgressDialog pd;
    private String email,password,phone,Name,Val,address,aadharno,city;
    private Button proceed;
    private TextView selectoredit;

    public DetailsFragment(String email, String password, String phone, String address, String name, String aadharno, String pincode,String city, String val) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address=address;
        this.Name=name;
        this.aadharno=aadharno;
        this.pincode=pincode;
        this.city=city;
        Val=val;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init(view);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String min=mincharge.getText().toString().trim();
                String max=maxcharge.getText().toString().trim();
                shopName= shopname.getText().toString().trim();

                if (TextUtils.isEmpty(min))
                {
                    Toast.makeText(getContext(), "Please enter your minimum service charge", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(max))
                {
                    Toast.makeText(getContext(), "Please enter your maximum service charge", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(shopName))
                {
                    Toast.makeText(getContext(), "Please enter your shop name or company name", Toast.LENGTH_SHORT).show();
                }
                else {

                    ((Registeractivity)getActivity()).setFragment(new OtpFragment(email,password,phone,address,Name,Val,uri,min,max,shopName,aadharno,pincode,city));
                }
            }
        });
        selectoredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else {
                            Toast.makeText(getContext(), "Please allow storage permissions", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });
    }

    private void selectImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityMenuIconColor(getResources().getColor(R.color.colorAccent))
                .setActivityTitle("Profile Photo")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(getContext(), DetailsFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectoredit.setText("Edit profile");
                uri = result.getUri();


                Glide
                        .with(this)
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.userprofile)
                        .into(imageView);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void init(View view)
    {
        imageView=view.findViewById(R.id.userplace);
        selectoredit=view.findViewById(R.id.selectprofile);
        proceed=view.findViewById(R.id.btnproceed);
        mincharge=view.findViewById(R.id.mincharge);
        maxcharge=view.findViewById(R.id.maxcharge);
        shopname=view.findViewById(R.id.shopname);
    }

}
