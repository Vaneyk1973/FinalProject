package com.example.finalproject.fragments;

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

import com.example.finalproject.R;

public class StatusBarFragment extends Fragment {

    private static TextView health, mana;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView exp_img = getView().findViewById(R.id.exp_image),
                gold_img = getView().findViewById(R.id.gold_image),
                health_img = getView().findViewById(R.id.health_image),
                mana_img = getView().findViewById(R.id.mana_image),
                avatar = getView().findViewById(R.id.avatar);
        TextView lvl = getView().findViewById(R.id.level),
                gold = getView().findViewById(R.id.gold),
                exp = getView().findViewById(R.id.exp);
        health = (TextView) getView().findViewById(R.id.health);
        mana = (TextView) getView().findViewById(R.id.mana);
        String txt = MainActivity.player.getExperience() + "/" + MainActivity.player.getExperience_to_next_level_required();
        txt = MainActivity.player.getLevel() + "";
        lvl.setText(txt);
        txt = MainActivity.player.getGold() + "";
        gold.setText(txt);
        txt = Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMax_health());
        health.setText(txt);
        txt = Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMax_mana());
        mana.setText(txt);
        txt = MainActivity.player.getExperience() + "/" + MainActivity.player.getExperience_to_next_level_required();
        exp.setText(txt);
        Bitmap bm = Bitmap.createScaledBitmap
                (MainActivity.b[5][5], MainActivity.avatar_width, MainActivity.avatar_width, false);
        avatar.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[5][3], MainActivity.status_images_width, MainActivity.status_images_width, false);
        gold_img.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[5][2], MainActivity.status_images_width, MainActivity.status_images_width, false);
        health_img.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[1][3], MainActivity.status_images_width, MainActivity.status_images_width, false);
        mana_img.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[4][3], MainActivity.status_images_width, MainActivity.status_images_width, false);
        exp_img.setImageBitmap(Bitmap.createBitmap(bm));
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.player.setChat_mode(!MainActivity.player.getChat_mode());
            }
        });
        Button chat = getView().findViewById(R.id.chat_button);
        FragmentManager fm = getParentFragmentManager();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if (MainActivity.player.getChat_mode()) {
                    if (MainActivity.player.getUser().isLogged_in() && MainActivity.player.isRegistered()) {
                        fragmentTransaction.add(R.id.chat, new ChatFragment());
                    } else {
                        fragmentTransaction.add(R.id.registration, new RegistrationFragment());
                    }
                    fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                    fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                } else {
                    if (MainActivity.player.getUser().isLogged_in()) {
                        FragmentTransaction f = getChildFragmentManager().beginTransaction();
                        f.add(R.id.chat_mini, new ChatMiniFragment());
                        f.commit();
                        chat.setVisibility(View.GONE);
                    } else {
                        fragmentTransaction.add(R.id.registration, new RegistrationFragment());
                        fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                        fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                        fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                    }
                }
                fragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public static void update() {
        String txt;
        txt = Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMax_health());
        health.setText(txt);
        txt = Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMax_mana());
        mana.setText(txt);
    }
}
