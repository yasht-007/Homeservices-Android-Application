package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yash.homeservice.R;
import com.yash.homeservice.modal.Subcategory;

import java.util.ArrayList;

public class Subcategoryadapter extends RecyclerView.Adapter<Subcategoryadapter.Subholder> {

    private Context context;
    private ArrayList<Subcategory> sublist;
    private LayoutInflater layoutInflater;

    public interface OnSubCategoryListner{
        void onSubCategoryClick(Subcategory category);
    }

    private OnSubCategoryListner listner;
    public Subcategoryadapter(Context context, ArrayList<Subcategory> sublist,OnSubCategoryListner listner) {
        this.context = context;
        this.sublist = sublist;
        this.listner=listner;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public Subholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.subcategory,parent,false);
        return new Subholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Subholder holder, int position) {
        final Subcategory subcategory=sublist.get(position);
        holder.title.setText(subcategory.getTitle());
        Glide.with(context)
                .load(subcategory.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder)
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onSubCategoryClick(subcategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sublist.size();
    }

    public class Subholder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        public Subholder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.iv_image);
            title=itemView.findViewById(R.id.tv_titlee);
        }
    }
}
