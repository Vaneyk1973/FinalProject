package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

public class InventoryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        RecyclerView inventory=(RecyclerView)getView().findViewById(R.id.list);
        ArrayList<Item> items=new ArrayList<>();
        ArrayList<ImageView> categories=new ArrayList<>();
        Button back=(Button)getView().findViewById(R.id.back_button);
        FragmentManager fm=getParentFragmentManager();
        FragmentTransaction fr=fm.beginTransaction();
        Bitmap bm = Bitmap.createBitmap(width/4, width/4, Bitmap.Config.ARGB_8888);
        categories.add(getView().findViewById(R.id.armor_weapons));
        categories.add(getView().findViewById(R.id.potions_food));
        categories.add(getView().findViewById(R.id.recourses));
        categories.add(getView().findViewById(R.id.other));
        bm.eraseColor(Color.RED);
        categories.get(0).setImageBitmap(bm);
        categories.get(1).setImageBitmap(bm);
        categories.get(2).setImageBitmap(bm);
        categories.get(3).setImageBitmap(bm);
        items.add(new Weapon(0, 0, 0, "Weapon"));
        items.add(new Potion(0, "Potion"));
        inventory.setAdapter(new MyAdapter(items));
        inventory.setLayoutManager(new LinearLayoutManager(getContext()));
        fr.add(R.id.characteristics, new ItemCharacteristics());
        fr.commit();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction= fm.beginTransaction();
                fragmentTransaction.remove(fm.findFragmentById(R.id.inventory));
                fragmentTransaction.add(R.id.map, new Map());
                fragmentTransaction.add(R.id.status, new StatusBar());
                fragmentTransaction.add(R.id.menu, new Menu());
                fragmentTransaction.commit();
            }
        });
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private final ArrayList<Item> data=new ArrayList<>();

        public MyAdapter(ArrayList<Item> data) {
            this.data.addAll(data);
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                name=(TextView) itemView.findViewById(R.id.textView);

            }
        }

        @NonNull
        @NotNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MyAdapter.ViewHolder holder, int position) {
            holder.name.setText(data.get(position).getName());
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), holder.name.getText(), Toast.LENGTH_SHORT).show();
                    /*FragmentContainerView characteristics=getView().findViewById(R.id.characteristics);
                    TextView name_view=(TextView)characteristics.findViewById(R.id.name_field);
                    name_view.setText(data.get(position).getName());*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}


