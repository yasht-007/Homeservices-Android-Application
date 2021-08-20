package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.homeservice.Global.Myservices;
import com.yash.homeservice.R;

import java.util.ArrayList;

public class Myservicesadapter extends RecyclerView.Adapter<Myservicesadapter.MyserviceViewHoldern>{
    private Context context;
    private ArrayList<Myservices> mylist;
    private LayoutInflater layoutInflater;

    public Myservicesadapter(Context context, ArrayList<Myservices> mylist) {
        this.context = context;
        this.mylist = mylist;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public MyserviceViewHoldern onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.layout_myservice,parent,false);
        return new MyserviceViewHoldern(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyserviceViewHoldern holder, int position) {

        final Myservices myservices= mylist.get(position);
        holder.mainCat.setText(mylist.get(position).getMaincat());
        holder.subCat.setText(mylist.get(position).getSubcat());
        holder.serviceCharge.setText(mylist.get(position).getServicecharge());

    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class MyserviceViewHoldern extends RecyclerView.ViewHolder{

        private TextView mainCat,subCat,serviceCharge;
        public MyserviceViewHoldern(@NonNull View itemView) {
            super(itemView);

            mainCat=itemView.findViewById(R.id.mycat);
            subCat=itemView.findViewById(R.id.mysubcat);
            serviceCharge=itemView.findViewById(R.id.myserch);
        }
    }

}
