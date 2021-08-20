package com.yash.homeservice.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.yash.homeservice.Global.Const;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.Registeractivity;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends Fragment {


    public CreateAccountFragment(String md) {
        val=md;
    }
    private EditText name,phone,email,password,aadharNo,city;
    private ProgressBar progressBar;
    private Button signup;
    private String val;
    private ConstraintLayout constraintlayout;
    private TextView signintext;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        firebaseAuth=FirebaseAuth.getInstance();
        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Registeractivity)getActivity()).setFragment(new LoginFragment(Const.USER));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String City= city.getText().toString().toLowerCase().trim();
                String Password = password.getText().toString().trim();
                String aadhar= aadharNo.getText().toString().trim();


                resetField();

                if (TextUtils.isEmpty(Name))
                {
                    setError(name,"Please enter Name");
                    name.requestFocus();
                }else if(name.length()>30)
                {
                    setError(name, "Name length must not greater than 30 characters ");
                    name.requestFocus();
                }else if(TextUtils.isEmpty(Phone))
                {
                    setError(phone,"Please enter Phone number");
                    phone.requestFocus();
                }else if(Phone.length()!=10)
                {
                    setError(phone,"Phone number length should be 10");
                    phone.requestFocus();
                }else if(TextUtils.isEmpty(Email))
                {
                    setError(email,"Please enter Email");
                    email.requestFocus();
                }else if(!isValidEmail(Email))
                {
                    setError(email,"Please enter valid Email");
                    email.requestFocus();
                }else if(TextUtils.isEmpty(Password))
                {
                    setError(password,"Please enter Password");
                    password.requestFocus();
                }else if(!isValidPassword(Password))
                {
                    setError(password,"Password minimum length should be 8 and maximum length should not exceed 12 characters");
                    password.requestFocus();
                }else if(TextUtils.isEmpty(City))
                {
                    setError(password,"Please enter your city name");
                    city.requestFocus();
                }
                else if (TextUtils.isEmpty(aadhar))
                {
                    setError(aadharNo,"You must enter aadhar card number for verifying your citizenship status");
                    aadharNo.requestFocus();
                }
                else if (!(aadhar.length()==12))
                {
                    setError(aadharNo,"please enter proper aadhar card number");
                    aadharNo.requestFocus();
                }
                else
                {
                    createAccount();
                }

            }
        });

    }

    private void createAccount() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
              if (task.isSuccessful())
              {
                  if (val.equals("user"))
                  {
                      if (task.getResult().getSignInMethods().isEmpty())
                      {
                          ((Registeractivity)getActivity()).setFragment(new OtpFragment(email.getText().toString().trim(),password.getText().toString().trim(),phone.getText().toString().trim(),null,name.getText().toString().trim(),city.getText().toString().toLowerCase().trim(),aadharNo.getText().toString().trim(),val));
                      }else{
                          setError(email,"Email already exists");
                          progressBar.setVisibility(View.INVISIBLE);
                      }
                  }else {
                      if (task.getResult().getSignInMethods().isEmpty())
                      {
                          ((Registeractivity)getActivity()).setFragment(new AddressFragment(email.getText().toString().trim(),password.getText().toString().trim(),phone.getText().toString().trim(),name.getText().toString().trim(),aadharNo.getText().toString().trim(),val));
                      }else{
                          setError(email,"Email already exists");
                          progressBar.setVisibility(View.INVISIBLE);
                      }
                  }

              }
              else {
                 String error=task.getException().getMessage();
                  Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
              }
              progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void resetField() {
        name.setBackgroundResource(R.drawable.bg_input_field);
        phone.setBackgroundResource(R.drawable.bg_input_field);
        email.setBackgroundResource(R.drawable.bg_input_field);
        password.setBackgroundResource(R.drawable.bg_input_field);
    }

    private boolean isValidPassword(String password) {
        if(password.length()<8 || password.length()>12)
        {
            return false;
        }
        else {
            return true;
        }

    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();

    }

    public void setError(EditText editText, String message) {
        editText.setBackgroundResource(R.drawable.bg_input_field_error);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void init(View view) {
        email=view.findViewById(R.id.email);
        name=view.findViewById(R.id.name);
        phone=view.findViewById(R.id.phone);
        password=view.findViewById(R.id.password);
        progressBar=view.findViewById(R.id.rg_progressbar);
        signup=view.findViewById(R.id.createaccountbtn);
        signintext=view.findViewById(R.id.signintext);
        city=view.findViewById(R.id.citydetails);
        aadharNo=view.findViewById(R.id.aadharno);
        constraintlayout=view.findViewById(R.id.constraintLayout);

    }
}
