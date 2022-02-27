package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.service.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SignInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back = getView().findViewById(R.id.log_in_back_button),
                logIn = getView().findViewById(R.id.sign_in);
        EditText email = getView().findViewById(R.id.email),
                password = getView().findViewById(R.id.password);
        TextView restorePassword = getView().findViewById(R.id.restore_password_link),
                register = getView().findViewById(R.id.register_link);
        ProgressBar t = getView().findViewById(R.id.sign_in_loading);
        View.OnClickListener[] clickListener = new View.OnClickListener[1];
        clickListener[0] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                    email.setError("Enter a valid email");
                else if (password.getText().toString().isEmpty())
                    password.setError("Enter a valid password");
                else {
                    t.setVisibility(View.VISIBLE);
                    t.animate();
                    logIn.setOnClickListener(null);
                    FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email.getText().toString(), password.getText().toString().hashCode()+"").
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        MainActivity.player.setUser(new User("", email.getText().toString()));
                                        MainActivity.player.getUser().setUID(FirebaseAuth.getInstance().getUid());
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(MainActivity.player.getUser().getUID()).child("login")
                                                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    MainActivity.player.getUser().setLogin(task.getResult().getValue(String.class));
                                                    FragmentTransaction fr=getParentFragmentManager().beginTransaction();
                                                    fr.add(R.id.chat, new ChatFragment());
                                                    fr.remove(getParentFragmentManager().findFragmentById(R.id.log_in));
                                                    fr.commit();
                                                }
                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference("Users").
                                                child(MainActivity.player.getUser().getUID()).child("loggedIn").setValue(true)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            MainActivity.player.getUser().setLoggedIn(true);
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Wrong email and/or password", Toast.LENGTH_SHORT).show();
                                        logIn.setOnClickListener(clickListener[0]);
                                    }
                                    t.setVisibility(View.GONE);
                                }
                            });
                }
            }
        };
        logIn.setOnClickListener(clickListener[0]);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.remove(getParentFragmentManager().findFragmentById(R.id.log_in));
                fr.add(R.id.register, new RegisterFragment());
                fr.commit();
            }
        });
        restorePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.remove(getParentFragmentManager().findFragmentById(R.id.log_in));
                fr.add(R.id.restore_password, new RestorePasswordFragment());
                fr.commit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.remove(getParentFragmentManager().findFragmentById(R.id.log_in));
                fr.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                fr.add(R.id.status, new StatusBarFragment());
                fr.add(R.id.menu, new MenuFragment());
                fr.commit();
            }
        });
    }
}