package com.yash.homeservice.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yash.homeservice.Global.Const;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.activities.Registeractivity;
import com.yash.homeservice.activities.VendorDashboardActivity;

import java.util.List;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public LoginFragment(int user) {
        key=user;
    }
    private int key;
    private String fac;
    private EditText emailorphone,password;
    private TextView forgotpass,signuptv;
    private String md;
    private Button login;
    private ProgressBar progressbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);

        init(view);

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Registeractivity)getActivity()).setFragment(new ForgotPasswordFragment());
            }
        });

        if (key== Const.USER)
        {
            md="user";
        }else
        {
            md="vendor";
            
        }

        SharedPreferences preferences=getContext().getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("user",md);
        editor.commit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String EmailorPhone= emailorphone.getText().toString().trim();
                String Password= password.getText().toString().trim();

                resetField();

                if (true)
                {
                    if (TextUtils.isEmpty(EmailorPhone))
                    {
                        setError(emailorphone,"Please enter Email or Phone");
                        emailorphone.requestFocus();
                    }
                    else if (TextUtils.isEmpty(Password))
                    {
                        setError(password,"Please enter Password");
                        password.requestFocus();
                    } else {
                        if (isValidEmail(EmailorPhone))
                        {
                            progressbar.setVisibility(View.VISIBLE);
                            FirebaseFirestore.getInstance().collection("kaam25_users").whereEqualTo("email",EmailorPhone).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                   if(task.isSuccessful())
                                   {
                                       List<DocumentSnapshot> document=task.getResult().getDocuments();
                                       if (document.isEmpty())
                                       {
                                           setError(emailorphone,"Phone number is not registered");
                                           emailorphone.requestFocus();
                                           progressbar.setVisibility(View.INVISIBLE);
                                       }else {
                                           fac= document.get(0).get("facility").toString().trim();
                                           login(EmailorPhone);
                                       }

                                   }
                                   else {
                                       String error = task.getException().getMessage();
                                       Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                       progressbar.setVisibility(View.INVISIBLE);
                                   }
                                }
                            });

                        }else if(emailorphone.getText().toString().trim().matches("\\d{10}")) {
                            progressbar.setVisibility(View.VISIBLE);

                                FirebaseFirestore.getInstance().collection("kaam25_users").whereEqualTo("phone", EmailorPhone).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            List<DocumentSnapshot> document= task.getResult().getDocuments();

                                            if (document.isEmpty())
                                            {
                                                setError(emailorphone,"Phone number is not registered");
                                                emailorphone.requestFocus();
                                                progressbar.setVisibility(View.INVISIBLE);
                                            }else {
                                                String mail= document.get(0).get("email").toString().trim();
                                                fac= document.get(0).get("facility").toString().trim();
                                                // String phone=document.get(0).get("phone").toString().trim();
                                                login(mail);
                                            }

                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            progressbar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                        }

                        else {
                            setError(emailorphone,"Please Enter valid Email or Phone");
                            emailorphone.requestFocus();
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    }
                }


            }
        });

        signuptv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Registeractivity)getActivity()).setFragment(new CreateAccountFragment(md));
            }
        });
    }

    private void login(String emailorPhone) {
        if (fac.equals("user") && key==0)
        {
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(emailorPhone,password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Intent mainIntent= new Intent(getContext(), DashboardActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }else if (!fac.equals("user") && key==1)
        {
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(emailorPhone,password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Intent mainIntent= new Intent(getContext(), VendorDashboardActivity.class);
                        startActivity(mainIntent);
                        getActivity().finish();
                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }else {
            Toast.makeText(getContext(),"Phone number or Email is not registered", Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.INVISIBLE);
        }

    }


    private boolean isValidEmail(String emailorPhone) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_PATTERN).matcher(emailorPhone).matches();
    }


    private void setError(EditText editText, String message) {
        editText.setBackgroundResource(R.drawable.bg_input_field_error);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void resetField() {
        emailorphone.setBackgroundResource(R.drawable.bg_input_field);
        password.setBackgroundResource(R.drawable.bg_input_field);
    }

    private void init(View view) {

        emailorphone=view.findViewById(R.id.emailorphone);
        password=view.findViewById(R.id.password);
        forgotpass=view.findViewById(R.id.forgotpass);
        progressbar=view.findViewById(R.id.lg_progressbar);
        login=view.findViewById(R.id.loginbtn);
        signuptv=view.findViewById(R.id.signuptext);
    }
}
