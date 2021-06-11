package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.TextView;

import com.example.finalproject.R;

import org.jetbrains.annotations.NotNull;

public class SettingsMenuFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView settings=getView().findViewById(R.id.settings_button),
                help=getView().findViewById(R.id.help_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getParentFragmentManager().beginTransaction();
                fr.remove(getParentFragmentManager().findFragmentById(R.id.settings_menu));
                fr.add(R.id.settings, new TutorialFragment());
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getParentFragmentManager().beginTransaction();
                fr.remove(getParentFragmentManager().findFragmentById(R.id.settings_menu));
                fr.add(R.id.tutorial, new TutorialFragment());
            }
        });
    }
}