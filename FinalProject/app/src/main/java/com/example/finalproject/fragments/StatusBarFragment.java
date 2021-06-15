package com.example.finalproject.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
        ImageView expImg = getView().findViewById(R.id.exp_image),
                goldImg = getView().findViewById(R.id.gold_image),
                healthImg = getView().findViewById(R.id.health_image),
                manaImg = getView().findViewById(R.id.mana_image),
                avatar = getView().findViewById(R.id.avatar);
        TextView lvl = getView().findViewById(R.id.level),
                gold = getView().findViewById(R.id.gold),
                exp = getView().findViewById(R.id.exp);
        health = (TextView) getView().findViewById(R.id.health);
        mana = (TextView) getView().findViewById(R.id.mana);
        String txt;
        txt = MainActivity.player.getLevel() + "";
        lvl.setText(txt);
        txt = MainActivity.player.getGold() + "";
        gold.setText(txt);
        txt = Math.round(MainActivity.player.getHealth()) + "/" + Math.round(MainActivity.player.getMax_health());
        health.setText(txt);
        txt = Math.round(MainActivity.player.getMana()) + "/" + Math.round(MainActivity.player.getMax_mana());
        mana.setText(txt);
        txt = MainActivity.player.getExperience() + "/" + MainActivity.player.getExperienceToNextLevelRequired();
        exp.setText(txt);
        Bitmap bm = Bitmap.createScaledBitmap
                (MainActivity.b[5][5], MainActivity.avatarWidth, MainActivity.avatarWidth, false);
        avatar.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[3][5], MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false);
        goldImg.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[2][5], MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false);
        healthImg.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[3][1], MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false);
        manaImg.setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[3][4], MainActivity.statusImagesWidth, MainActivity.statusImagesWidth, false);
        expImg.setImageBitmap(Bitmap.createBitmap(bm));
        Button chat = getView().findViewById(R.id.chat_button);
        chat.setVisibility(View.VISIBLE);
        FragmentManager fm = getParentFragmentManager();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if (MainActivity.player.getUser().isLoggedIn())
                {
                    if (MainActivity.player.getChatMode()){
                        fragmentTransaction.add(R.id.chat_mini, new ChatMiniFragment());
                        chat.setVisibility(View.GONE);
                    }
                    else {
                        fragmentTransaction.add(R.id.chat, new ChatFragment());
                        fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                        fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                        fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                    }
                }
                else {
                    fragmentTransaction.add(R.id.log_in, new SignInFragment());
                    fragmentTransaction.remove(fm.findFragmentById(R.id.map));
                    fragmentTransaction.remove(fm.findFragmentById(R.id.menu));
                    fragmentTransaction.remove(fm.findFragmentById(R.id.status));
                }
                fragmentTransaction.commit();
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = fm.beginTransaction();
                fr.remove(fm.findFragmentById(R.id.map));
                fr.remove(fm.findFragmentById(R.id.menu));
                fr.remove(fm.findFragmentById(R.id.status));
                fr.add(R.id.settings_menu, new SettingsMenuFragment());
                fr.commit();
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
