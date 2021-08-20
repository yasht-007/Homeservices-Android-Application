package com.yash.homeservice.Help;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yash.homeservice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CancelRescheduleFragment extends Fragment {


    private int status;
    public CancelRescheduleFragment(int status) {
        this.status=status;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cancel_reschedule, container, false);
    }

}
