package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yash.homeservice.R;
import com.yash.homeservice.modal.Servicerecycle;
import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder> {

    private Context context;
    private ArrayList<Servicerecycle> recyclelist;
    LayoutInflater layoutInflater;


   public interface OnServiceClickListener{
       void onServiceClick(Servicerecycle recycle);
   }
    private OnServiceClickListener listener;

    public MyRecyclerViewAdapter(Context context, ArrayList<Servicerecycle> recyclelist,OnServiceClickListener listener) {
        this.context = context;
        this.recyclelist = recyclelist;
        this.listener=listener;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.custom_servicelist,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
       holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_animation));
        final Servicerecycle servicerecycle=recyclelist.get(position);
        holder.Title1.setText(recyclelist.get(position).getTitle1());
        holder.Title2.setText(recyclelist.get(position).getTitle2());
        holder.Description.setText(recyclelist.get(position).getDescription());
        Glide.with(context)
                .load(servicerecycle.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholderrc)
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onServiceClick(servicerecycle);
            }
        });
    }


    @Override
    public int getItemCount() {
        return recyclelist.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView img;
        CardView cardView;
        TextView Title1;
        TextView Title2;
         TextView Description;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            cardView=itemView.findViewById(R.id.cvservice);
            Title1=itemView.findViewById(R.id.ser1);
            Title2=itemView.findViewById(R.id.ser2);
            Description=itemView.findViewById(R.id.ser3);
            img=itemView.findViewById(R.id.imageser);
        }
    }
}
