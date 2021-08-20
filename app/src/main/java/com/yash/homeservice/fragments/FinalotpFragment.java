package com.yash.homeservice.fragments;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.Registeractivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinalotpFragment extends Fragment {

    private TextView tvPhone;
    private EditText OTP;
    private String Val;
    private ProgressBar progressBar;
    private Button verifybtn,resendbtn,okbtn;
    private Timer timer;
    private int count=0;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth firebaseAuth;
    private String Oldno,Newno;

    public FinalotpFragment(String oldno,String newno)
    {
        this.Oldno=oldno;
        this.Newno=newno;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        firebaseAuth=FirebaseAuth.getInstance();
        sendOtp();
        tvPhone.setText("Verification code has been sent to +91 "+Newno);
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
                firebaseAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseFirestore.getInstance().collection("kaam25_users").document(firebaseAuth.getCurrentUser().getUid()).update("phone",Newno).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getContext(), "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                                        showDialog();
                                    }else {
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
               // signInWithPhoneAuthCredential(credential);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        final LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.content_dialog,null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        Button btnOk = view.findViewById(R.id.btnok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent=new Intent(getActivity(), Registeractivity.class);
                intent.putExtra("Final",Val);
                startActivity(intent);
            }
        });
        dialog.show();
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
                "+91"+ Newno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);
    }

    private void resendOtp()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+ Newno,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,mResendToken);
    }

    private void init(View view) {
        OTP= view.findViewById(R.id.otp);
        progressBar=view.findViewById(R.id.otp_progressbar);
        verifybtn=view.findViewById(R.id.verifybtn);
        resendbtn=view.findViewById(R.id.resendbtn);
        tvPhone=view.findViewById(R.id.tv_phone);
        //okbtn=view.findViewById(R.id.btnok);
    }
}
