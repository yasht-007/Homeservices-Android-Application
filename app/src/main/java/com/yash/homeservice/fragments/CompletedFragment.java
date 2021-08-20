package com.yash.homeservice.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedFragment extends Fragment {

    private Button btnbook;
    private TextView message;
    private int status;

    public CompletedFragment(int status) {
        this.status=status;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        if (status==0) {


            btnbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DashboardActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
        else {
            btnbook.setVisibility(View.INVISIBLE);

        }
    }

    private void init(View view)
    {
        btnbook=view.findViewById(R.id.bookservice);
        message=view.findViewById(R.id.tv_nmessage);
    }
}
