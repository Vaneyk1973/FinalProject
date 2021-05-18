package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalproject.R;
import com.example.finalproject.service.A;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button register=getView().findViewById(R.id.confirm);
        EditText login=getView().findViewById(R.id.login),
                password=getView().findViewById(R.id.password);
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://m5hw.herokuapp.com/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        A a=retrofit.create(A.class);
        if (MainActivity.player.isRegistered())
            register.setText("Login");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.register(login.getText().toString(), password.getText().toString()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("KKRS", response.body());
                        FragmentTransaction fr=getParentFragmentManager().beginTransaction();
                        fr.remove(getParentFragmentManager().findFragmentById(R.id.registration));
                        fr.add(R.id.chat, new ChatFragment());
                        fr.commit();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("KKRE", t.toString());
                    }
                });
            }
        });
    }
}