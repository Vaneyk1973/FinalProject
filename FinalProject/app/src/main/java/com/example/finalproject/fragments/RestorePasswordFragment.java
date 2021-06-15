package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class RestorePasswordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restore_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back = getView().findViewById(R.id.reset_password_back_button),
                resetPassword = getView().findViewById(R.id.reset_password_button);
        EditText email = getView().findViewById(R.id.restore_password_email);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getText().toString();
                if (email_txt.isEmpty())
                    email_txt = MainActivity.player.getUser().getEmail();
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.add(R.id.log_in, new SignInFragment());
                fr.remove(getParentFragmentManager().findFragmentById(R.id.restore_password));
                fr.commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.add(R.id.log_in, new SignInFragment());
                fr.remove(getParentFragmentManager().findFragmentById(R.id.restore_password));
                fr.commit();
            }
        });
    }
}