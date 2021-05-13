package com.example.finalproject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class Chat extends Fragment {

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
        RecyclerView chat=getView().findViewById(R.id.chat_list);
        ArrayList<Message> messages=new ArrayList<>();
        EditText enter_message=getView().findViewById(R.id.message);
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



    class Message{
        @Expose
        @SerializedName("message")
        public String message;
        @Expose
        @SerializedName("user")
        public String user;
        @Expose
        @SerializedName("date")
        public long date;

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", user='" + user + '\'' +
                    ", date=" + date +
                    '}';
        }
    }

    interface A{
        @GET("/get_messages")
        Call<ArrayList<Message>> get_messages();

        @POST("/put_message")
        Call<String> put_message(@Query("message") String message);
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
        public void onBindViewHolder(@NonNull @NotNull Chat.ChatAdapter.ViewHolder holder, int position) {
            holder.message.setText(data.get(position).message);
            holder.time.setText(String.valueOf(data.get(position).date));
            holder.user.setText(data.get(position).user);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}