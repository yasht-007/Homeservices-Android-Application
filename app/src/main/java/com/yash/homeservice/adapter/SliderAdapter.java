package com.yash.homeservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.yash.homeservice.R;


public class SliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    int images[] =
            {
                    R.drawable.athome,
                    R.drawable.sit_back_and_relax,
                    R.drawable.abstractdoor
            };
    int headings[] =
            {
                    R.string.first_title,
                    R.string.second_title,
                    R.string.third_title
            };
    int descriptions[] =
            {
                    R.string.first_descripton,
                    R.string.second_descripton,
                    R.string.third_descripton
            };


    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.slides_layout,container,false);

        ImageView imageView=view.findViewById(R.id.sliderimage);
        TextView heading=view.findViewById(R.id.slidertextview);
        TextView desc =view.findViewById(R.id.sliderdesc);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }
}
