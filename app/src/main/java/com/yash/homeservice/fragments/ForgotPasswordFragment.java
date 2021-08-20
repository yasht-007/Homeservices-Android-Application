package com.yash.homeservice.fragments;


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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.yash.homeservice.R;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }
    private EditText email;
    private ProgressBar progressBar;
    private Button resetbtn;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(email.getText().toString().trim()))
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    email.setError("Please enter email");
                    email.requestFocus();
                }
                else if (isValidEmail(email.getText().toString().trim()))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth= FirebaseAuth.getInstance();
                    resetbtn.setEnabled(false);
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressBar.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "Password reset link sent to email successfully", Toast.LENGTH_LONG).show();
                                getActivity().onBackPressed();

                            }else {
                                String error=task.getException().getMessage();
                                email.setError(error);
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                            resetbtn.setEnabled(true);
                        }
                    });

                }else {
                    email.setError(null);
                    email.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private boolean isValidEmail(String mail) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
    }

    private void init(View view) {
        email=view.findViewById(R.id.Email);
        progressBar=view.findViewById(R.id.fp_progressbar);
        resetbtn= view.findViewById(R.id.resetbtn);
    }
}
