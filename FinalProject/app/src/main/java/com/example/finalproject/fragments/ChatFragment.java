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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.service.A;
import com.example.finalproject.service.Message;
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
        if (MainActivity.player.isLogged_in()){

        }
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
        Callback<String> d=new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("KKR", new Gson().fromJson(response.body(), Message.class).toString());
                a.get_messages().enqueue(f);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("KKE", t.toString());
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
                m.user="a";
                m.date=new Date().getTime();
                a.put_message(new Gson().toJson(m)).enqueue(d);
                v.setText("");
                enter_message.setOnClickListener(click);
                return false;
            }
        });
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

        ArrayList<Message> data;

        public ChatAdapter(ArrayList<Message> messages){
            data=messages;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView user, message, time;
            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                user=itemView.findViewById(R.id.user_list);
                time=itemView.findViewById(R.id.time_list);
                message=itemView.findViewById(R.id.message_list);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ChatFragment.ChatAdapter.ViewHolder holder, int position) {
            holder.message.setText(data.get(position).message);
            long date=data.get(position).date;
            String ms=date%1000+"", s, min, h;
            date/=1000;
            s=date%60+":";
            date/=600;
            min=date%60+":";
            date/=60;
            h=date%24+":";
            holder.time.setText(h+min+s+ms);
            holder.user.setText(data.get(position).user);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}