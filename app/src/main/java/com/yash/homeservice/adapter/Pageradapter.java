package com.yash.homeservice.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yash.homeservice.R;
import com.yash.homeservice.fragments.CompletedFragment;
import com.yash.homeservice.fragments.InprogressFragment;

public class Pageradapter extends FragmentPagerAdapter {

    private int status;
    private static final int[] TAB_TITLES =
            new int[] { R.string.tab_text_1, R.string.tab_text_2};
    private  Context mContext;
    public Pageradapter(Context context, FragmentManager fm,int Status)
    {
        super(fm);
       mContext=context;
       status=Status;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                InprogressFragment inprogressFragment=new InprogressFragment(status);
                return inprogressFragment;

            case 1:
                CompletedFragment completedFragment=new CompletedFragment(status);
                return completedFragment;

                default:
                    return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);

    }

    @Override
    public int getCount() {
        return 2;
    }
}
