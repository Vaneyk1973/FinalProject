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

public class ChatFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Retrofit chat_server=new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).
                baseUrl("https://m5hw.herokuapp.com/").
                build();
        Button log_out=getView().findViewById(R.id.log_out);
        A a=chat_server.create(A.class);
        ArrayList<Message> messages=new ArrayList<>();
        RecyclerView chat=getView().findViewById(R.id.chat_list);
        EditText enter_message=getView().findViewById(R.id.message);
        Button back=getView().findViewById(R.id.back_button_chat), register=getView().findViewById(R.id.log_out);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                FragmentTransaction fr=fm.beginTransaction();
                fr.add(R.id.map, new MapFragment());
                fr.add(R.id.menu, new MenuFragment());
                fr.add(R.id.status, new StatusBarFragment());
                fr.remove(fm.findFragmentById(R.id.chat));
                fr.commit();
            }
        });
        View.OnClickListener click=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)v).setText("");
                enter_message.setOnClickListener(null);
            }
        };
        Callback<ArrayList<Message>> f=new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                messages.clear();
                messages.addAll(response.body());
                chat.setAdapter(new ChatAdapter(messages));
                chat.scrollToPosition(messages.size()-1);
                Log.d("KKU", messages.toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                Log.d("KKD", t.toString());
            }
        };
        a.get_messages().enqueue(f);
        chat.setAdapter(new ChatAdapter(messages));
        chat.setLayoutManager(new LinearLayoutManager(getContext()));
        enter_message.setOnClickListener(click);
        enter_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("KKK", v.getText().toString());
                Message m=new Message();
                m.message=v.getText().toString();
                m.user=new Gson().toJson(MainActivity.player.getUser());
                m.date=new Date();
                Log.d("KKKR", m+"");
                a.put_message(new Gson().toJson(m)).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        Log.d("KKR", response.body()+"");
                        a.get_messages().enqueue(f);
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Log.d("KKE", t.toString());
                    }
                });
                v.setText("");
                enter_message.setOnClickListener(click);
                return false;
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.log_out(MainActivity.player.getUser().getLogin()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        MainActivity.player.getUser().log_in();
                        FragmentManager fm=getParentFragmentManager();
                        FragmentTransaction fr=fm.beginTransaction();
                        fr.remove(fm.findFragmentById(R.id.chat));
                        fr.add(R.id.registration, new RegistrationFragment());
                        fr.commit();
                        Log.d("KKRE", response.body()+"");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("KKRE", t.toString());
                    }
                });
            }
        });
        chat.scrollToPosition(messages.size()-1);
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

        ArrayList<Message> data;

        public ChatAdapter(ArrayList<Message> messages){
            data=messages;
        }

        class ChatViewHolder extends RecyclerView.ViewHolder{
            TextView user, message, time;
            public ChatViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                user=itemView.findViewById(R.id.user_list);
                time=itemView.findViewById(R.id.time_list);
                message=itemView.findViewById(R.id.message_list);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ChatFragment.ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ChatFragment.ChatAdapter.ChatViewHolder holder, int position) {
            holder.message.setText(data.get(position).message);
            Date date=data.get(position).date;
            holder.time.setText(date.toString());
            holder.user.setText(new Gson().fromJson(data.get(position).user, User.class).getLogin());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}