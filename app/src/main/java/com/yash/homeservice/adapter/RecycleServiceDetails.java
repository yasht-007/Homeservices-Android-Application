package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.yash.homeservice.R;
import com.yash.homeservice.modal.ServiceShow;

import java.util.ArrayList;

public class RecycleServiceDetails extends RecyclerView.Adapter<RecycleServiceDetails.Myholder> {

    private Context context;
    private ConstraintLayout cons;
    private ArrayList<ServiceShow> servicelist;
    private LayoutInflater layoutInflater;
    private Animation animation;
    private  OnServicesideClickListener listener;
    public static boolean status;
    private final int limit=5;

    public RecycleServiceDetails(Context context,ArrayList<ServiceShow> servicelist,OnServicesideClickListener listner)
    {
        this.context=context;
        this.servicelist=servicelist;
        this.listener=listner;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    public interface OnServicesideClickListener{
        void onServiceClick(ServiceShow show);
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.usersideservice,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        /*animation= AnimationUtils.loadAnimation(context,R.anim.fade_animation);
        cons.setAnimation(animation);*/
        final ServiceShow serviceShow=servicelist.get(position);
        holder.title.setText(serviceShow.getName());
        holder.service.setText(serviceShow.getService());
        String f= ""+serviceShow.getRating();
        if (f.equals("0.0"))
        {
            holder.rating.setText("0");
        }else {
            holder.rating.setText(f);
        }

        holder.ratingBar.setRating(serviceShow.getRating());
        holder.servicecharge.setText(serviceShow.getServicecharge());

        Glide.with(context)
                .load(serviceShow.getProfile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(),new RoundedCorners(16))
                .placeholder(R.drawable.userprofile)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onServiceClick(serviceShow);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (status==true)
        {
            if(servicelist.size() > limit){
                return limit;
            }
            else
            {
                return servicelist.size();
            }
        }else {
            return servicelist.size();
        }



    }

    public class Myholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        RatingBar ratingBar;
        TextView title,service,rating,servicecharge;


        public Myholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView13);
            title=itemView.findViewById(R.id.ser_name);
            service=itemView.findViewById(R.id.ser_service);
            rating=itemView.findViewById(R.id.ser_score);
            servicecharge=itemView.findViewById(R.id.ser_charge);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            cons=itemView.findViewById(R.id.constservice);
        }
    }
}
