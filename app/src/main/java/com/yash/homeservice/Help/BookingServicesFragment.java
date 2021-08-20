package com.yash.homeservice.Help;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yash.homeservice.R;
import com.yash.homeservice.activities.DashboardActivity;
import com.yash.homeservice.activities.VendorDashboardActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingServicesFragment extends Fragment {

    private int status;


    public BookingServicesFragment(int status) {
        this.status=status;
        // Required empty public constructor
    }

    private Button hwToBookSrvc,hwToCnclSrv,hwToRescheduleBooking,hwToCntctPro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        hwToBookSrvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status==0) {
                    ((DashboardActivity) getActivity()).setFragment(new HowToBookingServiceFragment());
                }
                else {
                    ((VendorDashboardActivity) getActivity()).setFragment(new HowToBookingServiceFragment());
                }
            }
        });

        hwToCnclSrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status==0) {
                    ((DashboardActivity) getActivity()).setFragment(new HowToCancelServiceFragment());

                }
                else {
                    ((VendorDashboardActivity) getActivity()).setFragment(new HowToCancelServiceFragment());

                }
            }
        });

        hwToRescheduleBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status==0)
                {
                    ((DashboardActivity)getActivity()).setFragment(new HowToRescheduleBookingFragment());

                }else {
                    ((VendorDashboardActivity)getActivity()).setFragment(new HowToRescheduleBookingFragment());

                }

            }
        });

        hwToCntctPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status==0)
                {
                    ((DashboardActivity)getActivity()).setFragment(new HowToContactProfassionalFragment());

                }else {
                    ((VendorDashboardActivity)getActivity()).setFragment(new HowToContactProfassionalFragment());

                }

            }
        });
    }
    private void init(View view) {

        hwToBookSrvc=view.findViewById(R.id.hw_to_book_service);
        hwToCnclSrv=view.findViewById(R.id.hw_to_cancel_booking);
        hwToRescheduleBooking=view.findViewById(R.id.hw_to_reschedule_booking);
        hwToCntctPro=view.findViewById(R.id.hw_to_contact_prof);
    }
}
