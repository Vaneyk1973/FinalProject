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

import android.util.Log;
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
import com.example.finalproject.service.Triplex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopFragment extends Fragment {
    private static Item chosenItem;
    private static String user;
    private static int amount=1;
    private static boolean mode, auction;
    private static TextView amount_text;
    private static FragmentManager fm1;

    public ShopFragment() {
        auction=false;
    }

    public ShopFragment(boolean auction) {
        ShopFragment.auction = auction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fm1=getChildFragmentManager();
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwitchCompat sellBuy=getView().findViewById(R.id.sell_buy_switch);
        TextView modeText=getView().findViewById(R.id.shop_mode);
        amount_text=getView().findViewById(R.id.amount);
        Button sellBuyButton=getView().findViewById(R.id.sell_buy_button),
                back=getView().findViewById(R.id.shop_back_button);
        FloatingActionButton add=getView().findViewById(R.id.add_button),
                remove=getView().findViewById(R.id.remove_button);
        RecyclerView shopList=getView().findViewById(R.id.items_to_sell);
        shopList.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Pair<Item, Integer>> data=new ArrayList<>();
        for (int i=0;i<MainActivity.shopList.size();i++)
            data.add(new Pair<>(MainActivity.shopList.get(i), 999));
        shopList.setAdapter(new ShopAdapter(MainActivity.player.getInventory()));
        if (!auction){
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
                    amount=1;
                    amount_text.setText(amount+"");
                }
            });
            sellBuy.setChecked(true);
            sellBuy.setChecked(false);
            sellBuyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mode){
                        if(!MainActivity.player.sellItem(new Pair<>(chosenItem, amount)))
                            Toast.makeText(getContext(), "You don't have enough items", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getContext(), "Sold successfully", Toast.LENGTH_SHORT).show();
                            shopList.setAdapter(new ShopAdapter(MainActivity.player.getInventory()));
                            FragmentManager fm=getChildFragmentManager();
                            FragmentTransaction fr=fm.beginTransaction();
                            if (fm.findFragmentById(R.id.characteristics3)!=null)
                                fr.remove(fm.findFragmentById(R.id.characteristics3));
                            fr.commit();
                        }
                    } else
                        if (!MainActivity.player.buyItem(new Pair<>(chosenItem, amount)))
                            Toast.makeText(getContext(), "You don't have enough gold", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getContext(), "Bought successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            modeText.setText("Place");
            sellBuy.setText("Place/Buy");
            sellBuyButton.setText("Place");
            shopList.setAdapter(new ShopAdapter(MainActivity.player.getInventory()));
            sellBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mode=isChecked;
                    FragmentTransaction fr=fm1.beginTransaction();
                    if (fm1.findFragmentById(R.id.characteristics3) != null)
                        fr.remove(fm1.findFragmentById(R.id.characteristics3));
                    fr.commit();
                    if (isChecked){
                        modeText.setText("Buy");
                        sellBuyButton.setText("Buy");
                        shopList.setAdapter(new ShopAdapter(new ArrayList<>()));
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Auction");
                        GenericTypeIndicator<ArrayList<Triplex<Item, Integer, String>>> genericTypeIndicator
                                = new GenericTypeIndicator<ArrayList<Triplex<Item, Integer, String>>>() {};
                        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                if (task.isSuccessful())
                                    if (task.getResult().getValue(genericTypeIndicator)!=null)
                                        shopList.setAdapter(new ShopAdapter(task.getResult().getValue(genericTypeIndicator), true));
                            }
                        });
                    }
                    else {
                        modeText.setText("Place");
                        sellBuyButton.setText("Place");
                        shopList.setAdapter(new ShopAdapter(MainActivity.player.getInventory()));
                    }
                    amount_text.setText(amount+"");
                }
            });
            sellBuyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chosenItem!=null){
                        if (!mode)
                            if (amount>MainActivity.player.getAmountOfItemsInInventory(chosenItem))
                                Toast.makeText(getContext(), "You don't have enough items", Toast.LENGTH_SHORT).show();
                            else {
                                MainActivity.player.placeItemOnAuction(
                                        new Triplex<>(chosenItem, amount, MainActivity.player.getUser().getLogin()), shopList);
                                FragmentTransaction fr=fm1.beginTransaction();
                                if (fm1.findFragmentById(R.id.characteristics3) != null)
                                    fr.remove(fm1.findFragmentById(R.id.characteristics3));
                                fr.commit();
                            }
                            else{
                                MainActivity.player.buyItemOnAuction(amount,
                                        chosenItem.getName(), user, shopList, getContext());
                            amount_text.setText(amount+"");
                            FragmentTransaction fr=fm1.beginTransaction();
                            if (fm1.findFragmentById(R.id.characteristics3) != null)
                                fr.remove(fm1.findFragmentById(R.id.characteristics3));
                            fr.commit();
                            }
                    }
                    else Toast.makeText(getContext(), "Please, choose item first", Toast.LENGTH_SHORT).show();
                }
            });
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount++;
                amount_text.setText(amount+"");
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount>1){
                    amount--;
                    amount_text.setText(amount+"");
                }
            }
        });
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
    }

    public static class ShopAdapter extends RecyclerView.Adapter<ShopFragment.ShopAdapter.ViewHolder> {
        private ArrayList<Pair<Item, Integer>> data = new ArrayList<>();
        private ArrayList<Triplex<Item, Integer, String>> dataA=new ArrayList<>();

        public ShopAdapter(ArrayList<Pair<Item, Integer>> data) {
            this.data.addAll(data);
        }

        public ShopAdapter(ArrayList<Triplex<Item, Integer, String>> dataA, boolean auction){
            this.dataA.addAll(dataA);
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
            if (!auction){
                if (data.size()==0){
                    FragmentTransaction fr=fm1.beginTransaction();
                    if (fm1.findFragmentById(R.id.characteristics3) != null)
                        fr.remove(fm1.findFragmentById(R.id.characteristics3));
                    fr.commit();
                }
                else {
                    if (!mode)
                        holder.name.setText(">"+data.get(position).first.getName()+":"+data.get(position).second+"\n");
                    else holder.name.setText(">"+data.get(position).first.getName()+"\n");
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chosenItem=data.get(position).first;
                            amount=1;
                            amount_text.setText(amount+"");
                            FragmentTransaction fr=fm1.beginTransaction();
                            if (fm1.findFragmentById(R.id.characteristics3) != null)
                                fr.remove(fm1.findFragmentById(R.id.characteristics3));
                            fr.add(R.id.characteristics3, new ItemCharacteristicsFragment(data.get(position).first, !mode));
                            fr.commit();
                        }
                    });
                }
            } else {
                if (dataA.size()==0&&mode||data.size()==0&&!mode){
                    FragmentTransaction fr=fm1.beginTransaction();
                    if (fm1.findFragmentById(R.id.characteristics3) != null)
                        fr.remove(fm1.findFragmentById(R.id.characteristics3));
                    fr.commit();
                } else if (mode){
                    holder.name.setText(">"+dataA.get(position).first.getName()+":"+dataA.get(position).second+"\n"
                            +dataA.get(position).third+"\n");
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chosenItem=dataA.get(position).first;
                            user=dataA.get(position).third;
                            amount=1;
                            amount_text.setText(amount+"");
                            FragmentTransaction fr=fm1.beginTransaction();
                            if (fm1.findFragmentById(R.id.characteristics3) != null)
                                fr.remove(fm1.findFragmentById(R.id.characteristics3));
                            fr.add(R.id.characteristics3, new ItemCharacteristicsFragment(dataA.get(position).first, !mode));
                            fr.commit();
                        }
                    });
                }
                else {
                    holder.name.setText(">"+data.get(position).first.getName()+":"+data.get(position).second+"\n");
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chosenItem=data.get(position).first;
                            amount=1;
                            amount_text.setText(amount + "");
                            FragmentTransaction fr=fm1.beginTransaction();
                            if (fm1.findFragmentById(R.id.characteristics3) != null)
                                fr.remove(fm1.findFragmentById(R.id.characteristics3));
                            fr.add(R.id.characteristics3, new ItemCharacteristicsFragment(data.get(position).first, mode));
                            fr.commit();
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            ArrayList a= new ArrayList();
            if (auction)
                if (mode){
                 for (int i=0;i<dataA.size();i++){
                     if (dataA.get(i).second!=0)
                         a.add(dataA.get(i));
                 }
                 dataA.clear();
                 dataA.addAll(a);
                 return dataA.size();
                }
            return data.size();
        }
    }
}