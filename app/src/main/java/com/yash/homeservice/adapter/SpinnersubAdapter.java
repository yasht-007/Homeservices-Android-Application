package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yash.homeservice.R;
import com.yash.homeservice.modal.Vendorservicelist;

import java.util.ArrayList;


public class SpinnersubAdapter extends ArrayAdapter<Vendorservicelist> {

    private Context context;

    public SpinnersubAdapter(@NonNull Context context, @NonNull ArrayList<Vendorservicelist> vendorservicelists) {
        super(context, 0, vendorservicelists);
        this.context=context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customview(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return customview(position, convertView, parent);
    }

    public View customview(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        if (convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subcategorycustom, parent, false);
        }
        Vendorservicelist serlist = getItem(position);
        ImageView spinnerimage = convertView.findViewById(R.id.ivcustomimg);
        TextView spinnertext = convertView.findViewById(R.id.tvcustomtitle);
        if (serlist != null)
        {
            Glide.with(context)
                    .load(serlist.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(spinnerimage);

            spinnertext.setText(serlist.getService());
        }


        return convertView;
    }
}
