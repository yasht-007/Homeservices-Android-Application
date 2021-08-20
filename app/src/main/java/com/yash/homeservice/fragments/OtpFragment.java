package com.yash.homeservice.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.activities.VendorDashboardActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends Fragment {


    private TextView tvPhone;
    private PinView OTP;
    private String url;
    private FirebaseStorage fstorage;
    private ProgressBar progressBar;
    private Button verifybtn,resendbtn;
    private String email,password,phone,Name;
    private Timer timer;
    private int count=0;
    private String fulladdress,Val,Service,pincode;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth firebaseAuth;
    private Uri uri;
    private String aadharno,cityname,city;
    private String min,max,shopname;

    public OtpFragment(String email, String password, String phone, String fulladdress, String name, String value, Uri uri,String min,String max,String shopname,String aadharno,String pincode,String city) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.fulladdress=fulladdress;
        this.Name=name;
        this.Val=value;
        this.uri=uri;
        this.min=min;
        this.max=max;
        this.shopname=shopname;
        this.aadharno=aadharno;
        this.pincode=pincode;
        this.city=city;

    }

    public OtpFragment(String email, String password, String phone, String fulladdress,String name,String cityname,String aadharno,String value) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.fulladdress=fulladdress;
        this.Name=name;
        this.cityname=cityname;
        this.aadharno=aadharno;
        this.Val=value;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        firebaseAuth=FirebaseAuth.getInstance();
        sendOtp();
        tvPhone.setText("Verification code has been sent to +91 "+phone);
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
              getActivity().runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      if (count==0)
                      {
                        resendbtn.setText("RESEND");
                        resendbtn.setEnabled(true);
                        resendbtn.setAlpha(1f);

                      }else {
                        resendbtn.setText("Resend code in "+count);
                        count--;
                      }
                  }
              });
            }
        },0,1000);
        resendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
                resendbtn.setEnabled(false);
                resendbtn.setAlpha(0.5f);
                count=60;
            }
        });
        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OTP.getText() == null || OTP.getText().toString().trim().isEmpty())
                {
                    return;
                }
                OTP.setError(null);
                String code= OTP.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init(View view) {
        OTP= view.findViewById(R.id.otp);
        progressBar=view.findViewById(R.id.otp_progressbar);
        verifybtn=view.findViewById(R.id.verifybtn);
        resendbtn=view.findViewById(R.id.resendbtn);
        tvPhone=view.findViewById(R.id.tv_phone);

    }

    private void sendOtp()
    {
        mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            //  Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                OTP.setError(e.getMessage());
            } else if (e instanceof FirebaseTooManyRequestsException) {
                OTP.setError(e.getMessage());
            }
                progressBar.setVisibility(View.INVISIBLE);
            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            //Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            Toast.makeText(getContext(), "Otp sent successfully", Toast.LENGTH_SHORT).show();

        }
        };


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);
    }

    private void resendOtp()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,mResendToken);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            AuthCredential authCredential= EmailAuthProvider.getCredential(email,password);
                            user.linkWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                     {
                                         SharedPreferences preferences=getContext().getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                                         SharedPreferences.Editor editor= preferences.edit();
                                         editor.putString("user",Val);
                                         editor.commit();

                                         if (Val.equals("user"))
                                         {
                                             Service="user";
                                             String add="Your address is empty please update address by pressing change address button";
                                             FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                                             Map<String,Object> map=new HashMap<>();
                                             map.put("email",email);
                                             map.put("phone",phone);
                                             map.put("name",Name);
                                             map.put("facility",Service);
                                             map.put("city",cityname);
                                             map.put("aadhar_no",aadharno);
                                             map.put("address",add);
                                             firebaseFirestore.collection("kaam25_users").document(firebaseAuth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful())
                                                                                         {
                                                                 Intent mainIntent= new Intent(getContext(), DashboardActivity.class);
                                                         startActivity(mainIntent);
                                                         getActivity().finish();

                                                     }else {
                                                         String error=task.getException().getMessage();
                                                         Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                         progressBar.setVisibility(View.INVISIBLE);
                                                     }
                                                 }
                                             });
                                         }else {

                                             Service="vendor";
                                             final FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                                             final Map<String,Object> nmap=new HashMap<>();
                                             nmap.put("email",email);
                                             nmap.put("phone",phone);
                                             nmap.put("address",fulladdress);
                                             nmap.put("name",Name);
                                             nmap.put("min charge",min);
                                             nmap.put("max charge",max);
                                             nmap.put("Shop name",shopname);
                                             nmap.put("aadhar_no",aadharno);
                                             nmap.put("pincode",pincode);
                                             nmap.put("city",city);
                                             nmap.put("facility",Service);
                                             firebaseFirestore.collection("kaam25_users").document(firebaseAuth.getCurrentUser().getUid()).set(nmap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful())
                                                     {
                                                         uploadProfileimage();
                                                         Intent mainIntent= new Intent(getContext(), VendorDashboardActivity.class);
                                                         startActivity(mainIntent);
                                                         getActivity().finish();

                                                     }else {
                                                         String error=task.getException().getMessage();
                                                         Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                         progressBar.setVisibility(View.INVISIBLE);
                                                     }
                                                 }
                                             });


                                         }

                                    }else
                                    {
                                        String error=task.getException().getMessage();
                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        } else {
                            // Sign in failed, display a message and update the UI
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                OTP.setError("Invalid OTP");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private void uploadProfileimage() {
        fstorage=FirebaseStorage.getInstance();
        final String fileName = FirebaseAuth.getInstance().getUid() + ".png";
        final StorageReference ref = fstorage.getReference("profiles/"+fileName);
        if (uri!=null)
        {
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

                                url = uri.toString();

                        }
                    });
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                        Map<String,Object> map=new HashMap<>();

                        map.put("profile_url",url);

                        firebaseFirestore.collection("kaam25_users").document(firebaseAuth.getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                }else{
                                }
                            }
                        });
                    }


                }

            });
        }

        else {
            FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
            Map<String,Object> gmap=new HashMap<>();

            String ur="https://firebasestorage.googleapis.com/v0/b/kaam25-73e62.appspot.com/o/subcategory%2Fuserprofile.png?alt=media&token=0b9b5cc3-dfd1-4906-9d43-09041a7039d1";
            gmap.put("profile_url",ur);

            firebaseFirestore.collection("kaam25_users").document(firebaseAuth.getCurrentUser().getUid()).update(gmap);
        }

    }
}