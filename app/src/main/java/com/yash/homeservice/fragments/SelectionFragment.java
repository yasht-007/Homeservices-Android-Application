package com.yash.homeservice.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yash.homeservice.Global.Const;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.Registeractivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectionFragment extends Fragment {



    public SelectionFragment() {
        // Required empty public constructor
    }

    private Button btnuser,btnservice;
    private int key;
    private FrameLayout frameLayout;
    private TextView dontknow;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);


              ((Registeractivity)getActivity()).setFragment(new LoginFragment(Const.USER));

            }
        });
        
        btnservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

             ((Registeractivity)getActivity()).setFragment(new LoginFragment(Const.SERVICE_PROVIDER));


            }
        });
        
        dontknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Choose button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(View view) {
        btnuser=view.findViewById(R.id.userbtn);
        btnservice=view.findViewById(R.id.servicebtn);
        dontknow=view.findViewById(R.id.dontknowtext);
        progressBar=view.findViewById(R.id.sl_progressbar);
        frameLayout=view.findViewById(R.id.framelayout);

    }
}
