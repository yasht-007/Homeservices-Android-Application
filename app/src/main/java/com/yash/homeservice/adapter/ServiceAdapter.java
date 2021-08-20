package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yash.homeservice.R;
import com.yash.homeservice.Usersidebooking.Serviceinner;

import java.util.ArrayList;

public class ServiceAdapter extends PagerAdapter {

    private ArrayList<Serviceinner> bannerlist;
    private Context context;

    public ServiceAdapter(ArrayList<Serviceinner> bannerlist, Context context) {
        this.bannerlist = bannerlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bannerlist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.service_inner,container,false);
        ImageView svImage=view.findViewById(R.id.serviceinner);
        Serviceinner serviceinner= bannerlist.get(position);
        Glide.with(context)
                .load(serviceinner.getServiceImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder)
                .error(R.color.placeholder)
                .into(svImage);
        //svImage.setImageResource(serviceinner.getServiceImage());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
}
