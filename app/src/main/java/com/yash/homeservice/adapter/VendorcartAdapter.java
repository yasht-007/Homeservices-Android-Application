package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.homeservice.R;
import com.yash.homeservice.modal.Vendorcart;

import java.util.ArrayList;

public class VendorcartAdapter extends RecyclerView.Adapter<VendorcartAdapter.Vendorviewholder> {
    private Context context;
    private LayoutInflater inflater;
    public ArrayList<Vendorcart> cartlist;

    public VendorcartAdapter(Context context,ArrayList<Vendorcart> cartlist)
    {
        this.context=context;
        this.cartlist=cartlist;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public Vendorviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.vendororder,parent,false);
        return new Vendorviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vendorviewholder holder, int position) {
        Vendorcart vendorcart=cartlist.get(position);
        holder.name.setText(vendorcart.getName());
        holder.name.setVisibility(View.VISIBLE);
        holder.service.setText(vendorcart.getService());
        holder.service.setVisibility(View.VISIBLE);
        String nm=vendorcart.getNumber();
        holder.phone.setText(vendorcart.getNumber());
        holder.phone.setVisibility(View.VISIBLE);
        holder.timing.setText(vendorcart.getTiming());
        holder.orderstatus.setText(vendorcart.getOrderstatus());
        holder.timing.setVisibility(View.VISIBLE);
        holder.date.setText(vendorcart.getDate());
        holder.date.setVisibility(View.VISIBLE);
        holder.address.setText(vendorcart.getAddress());
        holder.charge.setText(vendorcart.getServicecharge());
        holder.charge.setVisibility(View.VISIBLE);
        holder.code.setText(vendorcart.getUniquecode());
        holder.code.setVisibility(View.VISIBLE);

        boolean isExpanded = cartlist.get(position).isExpanded();

        holder.expandablelayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class Vendorviewholder extends RecyclerView.ViewHolder{

        public TextView name,service,phone,orderstatus,timing,date,code,address,charge;
        public Button approve,cancel;
        public LinearLayout expandablelayout;
        public RelativeLayout relativeLayout;
        public Vendorviewholder(@NonNull View itemView) {
            super(itemView);

            relativeLayout=itemView.findViewById(R.id.vrlcart);
            name=itemView.findViewById(R.id.voname);
            service=itemView.findViewById(R.id.voservice);
            phone=itemView.findViewById(R.id.vophone);
            orderstatus=itemView.findViewById(R.id.vorderstatus);
            timing=itemView.findViewById(R.id.votiming);
            date=itemView.findViewById(R.id.vodate);
            address=itemView.findViewById(R.id.vodaddress);
            charge=itemView.findViewById(R.id.vcharge);
            approve=itemView.findViewById(R.id.btnapprove);
            cancel=itemView.findViewById(R.id.btnordercancel);
            code=itemView.findViewById(R.id.vocode);
            expandablelayout=itemView.findViewById(R.id.expandableview1);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vendorcart usercart= cartlist.get(getAdapterPosition());
                    usercart.setExpanded(!usercart.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
