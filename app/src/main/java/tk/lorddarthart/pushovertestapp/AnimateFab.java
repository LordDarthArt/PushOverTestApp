package tk.lorddarthart.pushovertestapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class AnimateFab {
    Animation fabClose, fabOpen, rotateForward, rotateBackward, tvOpen, tvClose;

    @SuppressLint("RestrictedApi")
    protected boolean animateFab(Context context, boolean isOpen, ConstraintLayout clTransp, FloatingActionButton fabMain, FloatingActionButton fabSendPush, FloatingActionButton fabScanQR, ConstraintLayout tvSendPush, ConstraintLayout tvScanQR) {
        fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        rotateForward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(context, R.anim.rotate_backward);
        tvOpen = AnimationUtils.loadAnimation(context, R.anim.tv_open);
        tvClose = AnimationUtils.loadAnimation(context, R.anim.tv_close);
        if (isOpen) {
            clTransp.animate().alpha(0.00001F).setDuration(300);
            fabScanQR.startAnimation(fabClose);
            tvScanQR.startAnimation(tvClose);
            fabSendPush.startAnimation(fabClose);
            tvSendPush.startAnimation(tvClose);
            fabMain.startAnimation(rotateForward);
            isOpen = false;
        } else {
            clTransp.animate().alpha(0.3F).setDuration(300);
            fabScanQR.setVisibility(View.VISIBLE);
            fabSendPush.setVisibility(View.VISIBLE);
            tvScanQR.setVisibility(View.VISIBLE);
            tvSendPush.setVisibility(View.VISIBLE);
            fabScanQR.startAnimation(fabOpen);
            tvScanQR.startAnimation(tvOpen);
            fabSendPush.startAnimation(fabOpen);
            tvSendPush.startAnimation(tvOpen);
            fabMain.startAnimation(rotateBackward);
            isOpen = true;
        }
        return isOpen;
    }
}
