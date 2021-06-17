package com.example.finalproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.items.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShopFragment extends Fragment {
    private Item chosenItem;
    private int amount=1;
    private boolean mode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwitchCompat sellBuy=getView().findViewById(R.id.sell_buy_switch);
        TextView modeText=getView().findViewById(R.id.shop_mode);
        Button sellBuyButton=getView().findViewById(R.id.sell_buy_button),
                back=getView().findViewById(R.id.shop_back_button);
        RecyclerView shopList=getView().findViewById(R.id.items_to_sell);
        ArrayList<Pair<Item, Integer>> data=new ArrayList<>();
        for (int i=0;i<MainActivity.shopList.size();i++)
            data.add(new Pair<>(MainActivity.shopList.get(i), 999));
        shopList.setLayoutManager(new LinearLayoutManager(getContext()));
        shopList.setAdapter(new ShopAdapter(MainActivity.player.getInventory()));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getParentFragmentManager();
                FragmentTransaction fr=fm.beginTransaction();
                fr.remove(fm.findFragmentById(R.id.shop));
                if (fm.findFragmentById(R.id.map)!=null)
                    fr.remove(fm.findFragmentById(R.id.map));
                fr.add(R.id.map, new MapFragment(MainActivity.player.getMapNum()));
                fr.add(R.id.status, new StatusBarFragment());
                fr.add(R.id.menu, new MenuFragment());
                fr.commit();
            }
        });
        sellBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mode=isChecked;
                if (isChecked){
                    modeText.setText("Buy");
                    sellBuyButton.setText("Buy");
                    shopList.setAdapter(new ShopAdapter(data));
                }
                else {
                    modeText.setText("Sell");
                    sellBuyButton.setText("Sell");
                    shopList.setAdapter(new ShopAdapter(MainActivity.player.getInventory()));
                }
            }
        });
        sellBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sellBuy.isChecked()){
                    if(!MainActivity.player.sellItem(new Pair<>(chosenItem, amount)))
                        Toast.makeText(getContext(), "You don't have enough items", Toast.LENGTH_SHORT).show();
                    else {
                        shopList.setAdapter(new ShopAdapter(MainActivity.player.getInventory()));
                        FragmentManager fm=getChildFragmentManager();
                        FragmentTransaction fr=fm.beginTransaction();
                        if (fm.findFragmentById(R.id.characteristics3)!=null)
                            fr.remove(fm.findFragmentById(R.id.characteristics3));
                        fr.commit();
                    }
                } else {
                    if (!MainActivity.player.buyItem(new Pair<>(chosenItem, amount)))
                        Toast.makeText(getContext(), "You don't have enough gold", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ShopAdapter extends RecyclerView.Adapter<ShopFragment.ShopAdapter.ViewHolder> {
        private ArrayList<Pair<Item, Integer>> data = new ArrayList<>();

        public ShopAdapter(ArrayList<Pair<Item, Integer>> data) {
            this.data.addAll(data);
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.textView);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ShopFragment.ShopAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ShopFragment.ShopAdapter.ViewHolder holder, int position) {
            if (!mode)
                holder.name.setText(">"+data.get(position).first.getName()+":"+data.get(position).second+"\n");
            else holder.name.setText(">"+data.get(position).first.getName()+"\n");
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chosenItem=data.get(position).first;
                    FragmentManager fm=getChildFragmentManager();
                    FragmentTransaction fr=fm.beginTransaction();
                    if (fm.findFragmentById(R.id.characteristics3) != null)
                        fr.remove(fm.findFragmentById(R.id.characteristics3));
                    fr.add(R.id.characteristics3, new ItemCharacteristicsFragment(data.get(position).first, !mode));
                    fr.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}