package com.example.finalproject.fragments;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.items.Item;
import com.example.finalproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
        RecyclerView inventory = (RecyclerView) getView().findViewById(R.id.list);
        ArrayList<ImageView> categories = new ArrayList<>();
        Button back = (Button) getView().findViewById(R.id.inventory_back_button);
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventory.setAdapter(new InventoryAdapter(MainActivity.player.getInventory(), categories.indexOf(v)));
            }
        };
        Bitmap bm = Bitmap.createScaledBitmap
                (MainActivity.b[0][6], MainActivity.category_image_width, MainActivity.category_image_width, false);
        categories.add(getView().findViewById(R.id.armor_weapons));
        categories.add(getView().findViewById(R.id.potions_food));
        categories.add(getView().findViewById(R.id.recourses));
        categories.add(getView().findViewById(R.id.other));
        categories.get(0).setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[1][6], MainActivity.category_image_width, MainActivity.category_image_width, false);
        categories.get(1).setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[2][6], MainActivity.category_image_width, MainActivity.category_image_width, false);
        categories.get(2).setImageBitmap(Bitmap.createBitmap(bm));
        bm = Bitmap.createScaledBitmap
                (MainActivity.b[3][6], MainActivity.category_image_width, MainActivity.category_image_width, false);
        categories.get(3).setImageBitmap(Bitmap.createBitmap(bm));
        inventory.setAdapter(new InventoryAdapter(MainActivity.player.getInventory()));
        inventory.setLayoutManager(new LinearLayoutManager(getContext()));
        categories.get(0).setOnClickListener(clickListener);
        categories.get(1).setOnClickListener(clickListener);
        categories.get(2).setOnClickListener(clickListener);
        categories.get(3).setOnClickListener(clickListener);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.remove(fm.findFragmentById(R.id.inventory));
                fragmentTransaction.add(R.id.map, new MapFragment());
                fragmentTransaction.add(R.id.status, new StatusBarFragment());
                fragmentTransaction.add(R.id.menu, new MenuFragment());
                fragmentTransaction.commit();
            }
        });
    }

    class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InvenoryViewHolder> {
        private ArrayList<Pair<Item, Integer>> data = new ArrayList<>();
        private final int category;

        public InventoryAdapter(ArrayList<Pair<Item, Integer>> data) {
            this.data.addAll(data);
            category=0;
        }

        public InventoryAdapter(ArrayList<Pair<Item, Integer>> data, int category) {
            this.category=category;
            this.data.addAll(data);
        }

        public class InvenoryViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public InvenoryViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.textView);
            }
        }

        @NonNull
        @NotNull
        @Override
        public InventoryFragment.InventoryAdapter.InvenoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
            return new InvenoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull InventoryFragment.InventoryAdapter.InvenoryViewHolder holder, int position) {
            holder.name.setText(">"+data.get(position).first.getName()+":"+data.get(position).second+"\n");
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.get(position).first.getCategory()==0){
                        MainActivity.player.equip(data.get(position).first);
                    }
                    FragmentManager fm=getChildFragmentManager();
                    FragmentTransaction fr=fm.beginTransaction();
                    if (fm.findFragmentById(R.id.characteristics) != null)
                        fr.remove(fm.findFragmentById(R.id.characteristics));
                    fr.add(R.id.characteristics, new ItemCharacteristicsFragment(data.get(position).first));
                    fr.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (category==-1)
                return data.size();
            else {
                ArrayList<Pair<Item, Integer>> data1=new ArrayList<>();
                for (int i=0;i<data.size();i++){
                    if (data.get(i).first.getCategory()==category)
                        data1.add(data.get(i));
                }
                data=new ArrayList<>(data1);
                return data1.size();
            }
        }
    }
}


