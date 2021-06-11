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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("JJJJJJJ", MainActivity.player.getUser().isLogged_in()+"");
        Button back = getView().findViewById(R.id.log_in_back_button),
                log_in = getView().findViewById(R.id.sign_in);
        EditText email = getView().findViewById(R.id.email),
                password = getView().findViewById(R.id.password);
        TextView restore_password = getView().findViewById(R.id.restore_password_link),
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
                    log_in.setOnClickListener(null);
                    FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(email.getText().toString(), password.getText().toString().hashCode()+"").
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Users").
                                                child(FirebaseAuth.getInstance().getUid()).child("logged_in").setValue(true)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            FragmentTransaction fr=getParentFragmentManager().beginTransaction();
                                                            fr.add(R.id.chat, new ChatFragment());
                                                            fr.remove(getParentFragmentManager().findFragmentById(R.id.log_in));
                                                            fr.commit();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Wrong email and/or password", Toast.LENGTH_SHORT).show();
                                    }
                                    t.setVisibility(View.GONE);
                                    log_in.setOnClickListener(clickListener[0]);
                                }
                            });
                }
            }
        };
        log_in.setOnClickListener(clickListener[0]);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.remove(getParentFragmentManager().findFragmentById(R.id.log_in));
                fr.add(R.id.register, new RegisterFragment());
                fr.commit();
            }
        });
        restore_password.setOnClickListener(new View.OnClickListener() {
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
                fr.add(R.id.map, new MapFragment());
                fr.add(R.id.status, new StatusBarFragment());
                fr.add(R.id.menu, new MenuFragment());
                fr.commit();
            }
        });
    }
}