package com.yash.homeservice.fragments;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.Updatecontactactivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateconoldFragment extends Fragment {

    private EditText etold,etnew;
    private Button btnupdate;
    private String old,New;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String PHONE_PATTERN= "[0-9]{10}";
    public UpdateconoldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_updateconold, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Updatecontactactivity.status=true;
        init(view);
        firebaseAuth=FirebaseAuth.getInstance();

        resetField();
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                old= etold.getText().toString().trim();
                New= etnew.getText().toString().trim();

                if (!old.matches(PHONE_PATTERN))
                {
                    etold.setError("Please enter correct number");
                    etold.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                }else if (!New.matches(PHONE_PATTERN))
                {
                    etnew.setError("Please enter correct number");
                    etold.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                }else {

                    old= "+91"+old;
                    New= "+91"+New;
                    String user= firebaseAuth.getCurrentUser().getPhoneNumber();
                    if (old.contentEquals(user))
                    {
                        old=old.replace("+91","");
                        New=New.replace("+91","");
                        ((Updatecontactactivity)getActivity()).setFragment(new FinalotpFragment(old,New));
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Entered old number is not linked with our app", Toast.LENGTH_SHORT).show();
                        etold.requestFocus();
                    }
                }
            }
        });

    }

    private void init(View view) {
        etold=view.findViewById(R.id.oldnumber);
        etnew=view.findViewById(R.id.updatenewnumber);
        btnupdate=view.findViewById(R.id.updatecontactbtn);
        progressBar=view.findViewById(R.id.up_progressbar);
    }

    private void resetField() {
        etold.setBackgroundResource(R.drawable.bg_input_field);
        etnew.setBackgroundResource(R.drawable.bg_input_field);
    }
    
}
