package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.finalproject.R;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwitchCompat chat_mode=getView().findViewById(R.id.chat_mode_switch);
        chat_mode.setChecked(MainActivity.player.getChat_mode());
        chat_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.player.setChat_mode(isChecked);
            }
        });
        Button back=getView().findViewById(R.id.settings_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getParentFragmentManager().beginTransaction();
                fr.add(R.id.settings_menu, new SettingsMenuFragment());
                fr.remove(getParentFragmentManager().findFragmentById(R.id.settings));
                fr.commit();
            }
        });
    }
}