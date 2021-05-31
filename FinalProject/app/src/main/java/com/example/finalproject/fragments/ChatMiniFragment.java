package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.service.A;
import com.example.finalproject.service.Message;
import com.example.finalproject.service.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatMiniFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_mini, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Retrofit chat_server = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).
                baseUrl("https://m5hw.herokuapp.com/").
                build();
        A a = chat_server.create(A.class);
        ArrayList<String> messages = new ArrayList<>();
        RecyclerView chat = getView().findViewById(R.id.chat_list);
        EditText enter_message = getView().findViewById(R.id.message);
        Button back = getView().findViewById(R.id.back_button_chat), register = getView().findViewById(R.id.log_out);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction fr = fm.beginTransaction();
                fr.remove(fm.findFragmentById(R.id.chat_mini));
                fr.commit();
                FragmentTransaction f = getParentFragment().getParentFragmentManager().beginTransaction();
                f.remove(getParentFragment().getParentFragmentManager().findFragmentById(R.id.status));
                f.commit();
                f = getParentFragment().getParentFragmentManager().beginTransaction();
                f.add(R.id.status, new StatusBarFragment());
                f.commit();
            }
        });
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) v).setText("");
                enter_message.setOnClickListener(null);
            }
        };
        Callback<ArrayList<String>> f = new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                messages.clear();
                messages.addAll(response.body());
                chat.setAdapter(new ChatMiniFragment.ChatAdapter(messages));
                chat.scrollToPosition(messages.size() - 1);
                Log.d("KKU", messages.toString());
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("KKD", t.toString());
            }
        };
        a.get_messages().enqueue(f);
        chat.setAdapter(new ChatMiniFragment.ChatAdapter(messages));
        chat.setLayoutManager(new LinearLayoutManager(getContext()));
        enter_message.setOnClickListener(click);
        enter_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("KKK", v.getText().toString());
                Message m = new Message();
                m.message = v.getText().toString();
                m.user = new Gson().toJson(MainActivity.player.getUser());
                m.date = new Date();
                Log.d("KKKR", new Gson().toJson(m));
                a.put_message(new Gson().toJson(m)).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("KKR", response + "");
                        a.get_messages().enqueue(f);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("KKE", t.toString());
                    }
                });
                v.setText("");
                enter_message.setOnClickListener(click);
                return false;
            }
        });
        chat.scrollToPosition(messages.size() - 1);
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatMiniFragment.ChatAdapter.ChatViewHolder> {

        ArrayList<String> data;

        public ChatAdapter(ArrayList<String> messages) {
            data = messages;
        }

        class ChatViewHolder extends RecyclerView.ViewHolder {
            TextView user, message, time;

            public ChatViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                user = itemView.findViewById(R.id.user_list);
                time = itemView.findViewById(R.id.time_list);
                message = itemView.findViewById(R.id.message_list);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ChatMiniFragment.ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new ChatMiniFragment.ChatAdapter.ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ChatMiniFragment.ChatAdapter.ChatViewHolder holder, int position) {
            holder.message.setText(new Gson().fromJson(data.get(position), Message.class).message);
            long time = new Gson().fromJson(data.get(position), Message.class).date.getTime() / 1000 / 60;
            String mins, hrs;
            mins = time % 60 >= 10 ? time % 60 + "" : "0" + time % 60;
            time /= 60;
            hrs = (time % 24 + 3) % 24 + "";
            String date = hrs + ":" + mins;
            holder.time.setText(date);
            holder.user.setText(new Gson().fromJson(new Gson().fromJson(data.get(position), Message.class).user, User.class).getLogin());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}