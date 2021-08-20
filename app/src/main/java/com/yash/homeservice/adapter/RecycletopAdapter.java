package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yash.homeservice.R;
import com.yash.homeservice.modal.Servicebanner;

import java.util.ArrayList;

public class RecycletopAdapter extends RecyclerView.Adapter<RecycletopAdapter.BannerViewHolder>{
    private Context context;
    private ArrayList<Servicebanner> recyclelist;
    LayoutInflater layoutInflater;


    public interface OnBannerClickListener{
        void onBannerClick(Servicebanner banner);
    }
    private OnBannerClickListener listener;
    public RecycletopAdapter(Context context, ArrayList<Servicebanner> recyclelist,OnBannerClickListener listener) {
        this.context = context;
        this.recyclelist = recyclelist;
        this.listener=listener;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.custom_serviceslist,parent,false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {

        final Servicebanner servicebanner= recyclelist.get(position);


        Glide.with(context)
                .load(servicebanner.getBanner())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBannerClick(servicebanner);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclelist.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.img_service);

        }
    }
}
