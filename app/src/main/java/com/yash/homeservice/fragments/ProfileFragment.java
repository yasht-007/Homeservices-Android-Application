package com.yash.homeservice.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.activities.Registeractivity;
import com.yash.homeservice.activities.Updatecontactactivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment{


    //private Bitmap bitmap;


    public ProfileFragment() {

    }
    private ImageView edit;
    private ProgressDialog pd;
    private CircleImageView profileImageview;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private StorageReference storage;
    private FirebaseStorage fstorage;
    private TextView pf_email, pf_name, pf_phone, pf_address,pf_detail, editProfile,pf_service;
    private Uri uri;
    private RelativeLayout relativeLayout,relativeaddress;
    private Button signoutbtn;
    private String url;
    private String Name, Email, Address, Phone,Service;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        init(view);
        firebaseAuth = FirebaseAuth.getInstance();
        loadProfileImage();
        fstorage = FirebaseStorage.getInstance();
        editProfile.setOnClickListener(new View.OnClickListener() {
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
            FirebaseFirestore.getInstance().collection("kaam25_users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    Service=documentSnapshot.get("facility").toString().trim();

                    if (Service.equals("user"))
                    {
                        pf_detail.setText("@user");

                    }else {
                        Address=documentSnapshot.get("address").toString().trim();

                        pf_address.setText(Address);
                        pf_address.setVisibility(View.VISIBLE);
                        pf_detail.setText("@vendor");
                        pf_service.setText(Service);
                        relativeaddress.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);


                    }
                    Name=documentSnapshot.get("name").toString().trim();
                    pf_name.setText(Name);
                    pf_name.setVisibility(View.VISIBLE);
                    Phone=documentSnapshot.get("phone").toString().trim();
                    pf_phone.setText(Phone);
                    pf_phone.setVisibility(View.VISIBLE);
                    Email=documentSnapshot.get("email").toString().trim();
                    pf_email.setText(Email);
                    pf_email.setVisibility(View.VISIBLE);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error=e.getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            });

    edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getActivity(), Updatecontactactivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    });
    signoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent=new Intent(getContext(), Registeractivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void init(View view) {

        profileImageview = view.findViewById(R.id.profile_image);
        editProfile = view.findViewById(R.id.editprofile);
        edit=view.findViewById(R.id.igedit);
        pf_email = view.findViewById(R.id.profileemail);
        pf_phone = view.findViewById(R.id.profilephone);
        relativeaddress=view.findViewById(R.id.rl_address);
        pf_address = view.findViewById(R.id.profileaddress);
        pf_name = view.findViewById(R.id.profilename);
        pf_detail=view.findViewById(R.id.detail);
        signoutbtn=view.findViewById(R.id.signoutbtn);

    }

    private void loadProfileImage() {

        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        String imageName = firebaseAuth.getUid() + ".png";
        StorageReference mStorageRef = mStorage.getReference("profiles/"+ imageName);

        mStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful())
                {
                    Uri uri =  task.getResult();

                    Glide.with(getActivity())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.userprofile)
                            .error(R.drawable.userprofile)
                            .into(profileImageview);
                }else {

                }
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
                .start(getContext(), ProfileFragment.this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();

                Glide
                        .with(this)
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.userprofile)
                        .into(profileImageview);
                         uploadProfileImage();


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void uploadProfileImage() {
        pd = ProgressDialog.show(getContext(), "Wait", "Uploading Image");

        final String fileName = FirebaseAuth.getInstance().getUid() + ".png";
        final StorageReference ref = fstorage.getReference("profiles/"+fileName);
        UploadTask uploadTask = ref.putFile(uri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    String error=task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url=uri.toString();
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                        Map<String,Object> map=new HashMap<>();
                        map.put("profile_url",url);
                        firebaseFirestore.collection("kaam25_users").document(firebaseAuth.getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getContext(), "profile photo uploaded successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), "failed url", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                 else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}

