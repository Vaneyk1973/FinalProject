package com.example.finalproject;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class StatusBar extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_status_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView img = (ImageView) getView().findViewById(R.id.avatar);
        Bitmap bm = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.GREEN);
        img.setImageBitmap(bm);
        ProgressBar mana_bar = (ProgressBar) getView().findViewById(R.id.mana_bar);
        ProgressBar health_bar = (ProgressBar) getView().findViewById(R.id.health_bar);
        health_bar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        health_bar.setProgress(100);
        mana_bar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        mana_bar.setProgress(100);
        super.onViewCreated(view, savedInstanceState);

    }
}