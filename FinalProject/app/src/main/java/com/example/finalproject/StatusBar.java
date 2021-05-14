package com.example.finalproject;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StatusBar extends Fragment {
    private static ProgressBar mana_bar;
    private static ProgressBar health_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        ImageView img = (ImageView) getView().findViewById(R.id.avatar);
        TextView exp = (TextView) getView().findViewById(R.id.experience_amount);
        TextView lvl = (TextView) getView().findViewById(R.id.level_value);
        TextView gld = (TextView) getView().findViewById(R.id.gold_amount);
        String txt = MainActivity.player.getExperience() + "/" + MainActivity.player.getExperience_to_next_level_required();
        exp.setText(txt);
        txt = MainActivity.player.getLevel() + "";
        lvl.setText(txt);
        txt = MainActivity.player.getGold() + "";
        gld.setText(txt);
        Bitmap bm = Bitmap.createBitmap((width - width / 4 - width / 5) / 2, (width - width / 4 - width / 5) / 2, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.GREEN);
        img.setImageBitmap(bm);
        mana_bar = (ProgressBar) getView().findViewById(R.id.mana_bar);
        health_bar = (ProgressBar) getView().findViewById(R.id.health_bar);
        health_bar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        health_bar.setMax(MainActivity.player.getMax_health());
        health_bar.setProgress(MainActivity.player.getHealth());
        mana_bar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        mana_bar.setProgress(MainActivity.player.getMana());
        mana_bar.setMax(MainActivity.player.getMax_mana());
        Button chat = getView().findViewById(R.id.chat_button);
        FragmentManager fm = getParentFragmentManager();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.chat, new Chat());
                fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                fragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public static void update() {
        health_bar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        health_bar.setMax(MainActivity.player.getMax_health());
        health_bar.setProgress(MainActivity.player.getHealth());
        mana_bar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        mana_bar.setProgress(MainActivity.player.getMana());
        mana_bar.setMax(MainActivity.player.getMax_mana());
    }
}