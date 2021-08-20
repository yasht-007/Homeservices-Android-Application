package com.yash.homeservice.modal;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.yash.homeservice.R;

public class CustomItemAnimator extends DefaultItemAnimator {
    private Animation animation;

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        return super.animateRemove(holder);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        animation= AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.viewholder_addanim);
        animation.setDuration(500);
        return super.animateAdd(holder);
    }
}
