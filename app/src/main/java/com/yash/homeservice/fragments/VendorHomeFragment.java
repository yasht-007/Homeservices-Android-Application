package com.yash.homeservice.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.yash.homeservice.R;
import com.yash.homeservice.adapter.Pageradapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorHomeFragment extends Fragment {

    private int status=1;
    public VendorHomeFragment() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);


        Pageradapter pageradapter=new Pageradapter(getContext(),getChildFragmentManager(),status);
        viewPager.setAdapter(pageradapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init(View view)
    {
        tabLayout = (TabLayout)view.findViewById(R.id.vtablayout);
        viewPager = (ViewPager)view.findViewById(R.id.vpager);
    }
}
