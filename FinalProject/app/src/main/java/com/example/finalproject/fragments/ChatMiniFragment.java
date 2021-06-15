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
import com.example.finalproject.service.Message;
import com.example.finalproject.service.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChatMiniFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_mini, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Message> messages = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Message");
        RecyclerView chat = getView().findViewById(R.id.chat_list);
        EditText enterMessage = getView().findViewById(R.id.message);
        Button back = getView().findViewById(R.id.chat_mini_back_button), register = getView().findViewById(R.id.log_out);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messages.clear();
                snapshot.getChildren().forEach(e -> messages.add(e.getValue(Message.class)));
                chat.setAdapter(new ChatMiniFragment.ChatAdapter(messages));
                chat.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction fr=fm.beginTransaction();
                fr.remove(fm.findFragmentById(R.id.status));
                fr.commit();
                fr=fm.beginTransaction();
                fr.add(R.id.status, new StatusBarFragment());
                fr.commit();
            }
        });
        chat.setLayoutManager(new LinearLayoutManager(getContext()));
        enterMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Message m = new Message();
                m.message = v.getText().toString();
                m.user = new Gson().toJson(MainActivity.player.getUser());
                m.date=new Date().getTime()-Calendar.getInstance().getTimeZone().getOffset(new Date().getTime())*60*1000;
                ref.child(messages.size() + "").setValue(m);
                v.setText("");
                return false;
            }
        });
        chat.scrollToPosition(messages.size() - 1);
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatMiniFragment.ChatAdapter.ChatViewHolder> {

        ArrayList<Message> data;

        public ChatAdapter(ArrayList<Message> messages) {
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
            holder.message.setText(data.get(position).message);
            long time = data.get(position).date / 1000 / 60 +new Date().getTimezoneOffset()*60;
            String mins, hrs;
            mins = time % 60 >= 10 ? time % 60 + "" : "0" + time % 60;
            time /= 60;
            hrs = (time % 24 + 3) % 24 + "";
            String date = hrs + ":" + mins;
            holder.time.setText(date);
            holder.user.setText(new Gson().fromJson(data.get(position).user, User.class).getLogin());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}