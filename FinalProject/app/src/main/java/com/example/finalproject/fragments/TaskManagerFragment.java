package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.service.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TaskManagerFragment extends Fragment {
    private boolean inVillage;
    private Task chosenTask;

    public TaskManagerFragment(){
        inVillage=false;
    }

    public TaskManagerFragment(boolean inVillage) {
        this.inVillage = inVillage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back=getView().findViewById(R.id.task_manager_back_button),
                takeTask=getView().findViewById(R.id.take_task_button);
        if (!inVillage)
            takeTask.setVisibility(View.GONE);
        takeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenTask==null)
                    Toast.makeText(getContext(), "Take task first", Toast.LENGTH_SHORT).show();
                else {

                }
            }
        });
        RecyclerView tasks=getView().findViewById(R.id.tasks_list);
        tasks.setLayoutManager(new LinearLayoutManager(getContext()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                FragmentTransaction fr=fm.beginTransaction();
                fr.remove(fm.findFragmentById(R.id.tasks));
                if (inVillage){
                    fr.add(R.id.menu, new MenuFragment());
                    fr.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                    fr.add(R.id.status, new StatusBarFragment());
                } else fr.add(R.id.settings_menu, new SettingsMenuFragment());
                fr.commit();
            }
        });
    }

    private class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>{
        private ArrayList<Task> data;

        public TasksAdapter(ArrayList<Task> data) {
            for (int i=0;i<data.size();i++){
                if (!data.get(i).isCompleted()||!data.get(i).isTaken())
                    this.data.add(data.get(i));
            }
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.task, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull TaskManagerFragment.TasksAdapter.ViewHolder holder, int position) {
            holder.name.setText(data.get(position).getName());
            TextView description=getView().findViewById(R.id.task_description);
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    description.setText(data.get(position).getDescription());
                    chosenTask=data.get(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder{
            TextView name;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                name=getView().findViewById(R.id.textView15);
            }
        }
    }
}