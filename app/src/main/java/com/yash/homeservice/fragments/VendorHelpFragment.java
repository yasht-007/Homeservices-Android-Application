package com.yash.homeservice.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yash.homeservice.Help.AboutUsFragment;
import com.yash.homeservice.Help.AccountSettingFragment;
import com.yash.homeservice.Help.BookingServicesFragment;
import com.yash.homeservice.Help.CancelRescheduleFragment;
import com.yash.homeservice.R;
import com.yash.homeservice.activities.VendorDashboardActivity;

public class VendorHelpFragment extends Fragment {

    private Button manageBooking,cancelReschedule,accSetting,bookingService,aboutUs;
    private final int status=1;

    public VendorHelpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        manageBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((VendorDashboardActivity)getActivity()).setFragment(new CancelRescheduleFragment(status));

            }
        });

        accSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((VendorDashboardActivity)getActivity()).setFragment(new AccountSettingFragment(status));
            }
        });

        bookingService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VendorDashboardActivity)getActivity()).setFragment(new BookingServicesFragment(status));
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VendorDashboardActivity)getActivity()).setFragment(new AboutUsFragment(status));
            }
        });

    }

    private void init(View view) {

        manageBooking=view.findViewById(R.id.vmanage_booking);
        cancelReschedule=view.findViewById(R.id.vcancel_reschedule);
        accSetting=view.findViewById(R.id.vacc_setting);
        bookingService=view.findViewById(R.id.vbooking_service);
        aboutUs=view.findViewById(R.id.vabout_us);
    }
}
