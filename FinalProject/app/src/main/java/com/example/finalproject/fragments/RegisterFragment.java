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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.finalproject.R;
import com.example.finalproject.service.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back = getView().findViewById(R.id.register_back_button),
                register = getView().findViewById(R.id.register_button);
        EditText login_view = getView().findViewById(R.id.login_reg),
                email_view = getView().findViewById(R.id.email_reg),
                password_view = getView().findViewById(R.id.password_reg),
                confirm_password_view = getView().findViewById(R.id.confirm_password_reg);
        String[] login = new String[1], email = new String[1], password = new String[1], confirm_password = new String[1];
        ProgressBar t=getView().findViewById(R.id.register_loading);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.remove(getParentFragmentManager().findFragmentById(R.id.register));
                fr.add(R.id.map, new MapFragment());
                fr.add(R.id.status, new StatusBarFragment());
                fr.add(R.id.menu, new MenuFragment());
                fr.commit();
            }
        });
        View.OnClickListener clickListener[]= new View.OnClickListener[1];
        clickListener[0]=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login[0] = login_view.getText().toString();
                email[0] = email_view.getText().toString();
                password[0] = password_view.getText().toString();
                confirm_password[0] = confirm_password_view.getText().toString();
                if (login[0].isEmpty()) {
                    login_view.setError("Login is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email[0]).matches()) {
                    email_view.setError("Enter a valid email");
                } else if (password[0].isEmpty()) {
                    password_view.setError("Password is required");
                } else if (password[0].length() < 8) {
                    password_view.setError("Password should be longer than 7 characters");
                } else if (confirm_password[0] == password[0]) {
                    confirm_password_view.setError("Passwords should match");
                } else {
                    t.setVisibility(View.VISIBLE);
                    t.animate();
                    register.setOnClickListener(null);
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email[0], password[0].hashCode() + "")
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                        MainActivity.player.setUser(new User(login[0], email[0], password[0]));
                                        ref.child(FirebaseAuth.getInstance().getUid()).setValue(MainActivity.player.getUser());
                                        ref.child(FirebaseAuth.getInstance().getUid()).child("password").setValue(password[0].hashCode());
                                        MainActivity.player.getUser().log_in();
                                        FragmentTransaction fr=getParentFragmentManager().beginTransaction();
                                        fr.add(R.id.chat, new ChatFragment());
                                        fr.remove(getParentFragmentManager().findFragmentById(R.id.register));
                                        fr.commit();
                                    }
                                    register.setOnClickListener(clickListener[0]);
                                    t.setVisibility(View.GONE);
                                }
                            });
                }
            }
        };
        register.setOnClickListener(clickListener[0]);
    }
}