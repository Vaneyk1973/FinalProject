package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.R
import com.example.finalproject.service.Statistics
import com.example.finalproject.service.classes.items.Item
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator

class ShopFragment(private val auction: Boolean = false) : Fragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private val inf: Int = 1e9.toInt()

    private var chosenItem: Item? = null
    private var user: String? = null
    private var amount = 1
    private var buyMode = false
    private val data = ArrayList<Pair<Int, Item>>()
    private lateinit var amountText: TextView
    private lateinit var modeText: TextView
    private lateinit var sellBuyButton: Button
    private lateinit var back: Button
    private lateinit var sellBuy: SwitchCompat
    private lateinit var add: FloatingActionButton
    private lateinit var remove: FloatingActionButton
    private lateinit var shopList: RecyclerView
    private lateinit var _childFragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _childFragmentManager = childFragmentManager
        sellBuy = requireView().findViewById(R.id.sell_buy_switch)
        modeText = requireView().findViewById(R.id.shop_mode)
        amountText = requireView().findViewById(R.id.amount)
        sellBuyButton = requireView().findViewById(R.id.sell_buy_button)
        back = requireView().findViewById(R.id.shop_back_button)
        add = requireView().findViewById(R.id.add_button)
        remove = requireView().findViewById(R.id.remove_button)
        shopList = requireView().findViewById(R.id.items_to_sell)
        shopList.layoutManager = LinearLayoutManager(context)
        for (i in Statistics.shopList)
            data.add(Pair(inf, i))
        shopList.adapter = ShopAdapter(player.inventory.inventory)
        if (!auction) {
            sellBuy.setOnCheckedChangeListener(this)
            sellBuy.isChecked = true
            sellBuy.isChecked = false
            sellBuyButton.setOnClickListener(this)
        } else {
            var text="Place"
            modeText.text = text
            sellBuyButton.text = text
            text="Place/Buy"
            sellBuy.text = text
            shopList.adapter = ShopAdapter(player.inventory.inventory)
            sellBuy.setOnCheckedChangeListener(this)
            sellBuyButton.setOnClickListener(this)
        }
        add.setOnClickListener(this)
        remove.setOnClickListener(this)
        back.setOnClickListener(this)
    }

    private inner class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder> {
        private val data = ArrayList<Pair<Int, Item>>()
        private val dataA = ArrayList<Triple<Item, Int, String>>()
        private var auction: Boolean

        constructor(data: ArrayList<Pair<Int, Item>>) {
            auction = false
            this.data.addAll(data)
        }

        constructor(dataA: ArrayList<Triple<Item, Int, String>>, auction: Boolean) {
            this.auction = true
            this.dataA.addAll(dataA)
        }

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var name: TextView = itemView.findViewById<View>(R.id.textView) as TextView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (!auction) {
                if (data.size == 0) {
                    val fr = _childFragmentManager.beginTransaction()
                    _childFragmentManager.findFragmentById(R.id.characteristics3)
                        ?.let { fr.remove(it) }
                    fr.commit()
                } else {
                    if (!buyMode)
                        holder.name.text = ">${data[position].second.name}:${data[position].first}"
                    else holder.name.text = ">${data[position].second.name}"
                    holder.name.setOnClickListener {
                        chosenItem = data[position].second
                        amount = 1
                        amountText.text = amount.toString()
                        val fr = _childFragmentManager.beginTransaction()
                        _childFragmentManager.findFragmentById(R.id.characteristics3)
                            ?.let { it1 -> fr.remove(it1) }
                        fr.add(
                            R.id.characteristics3,
                            ItemCharacteristicsFragment(data[position].second, !buyMode)
                        )
                        fr.commit()
                    }
                }
            } else {
                if (dataA.size == 0 && buyMode || data.size == 0 && !buyMode) {
                    val fragmentTransaction = _childFragmentManager.beginTransaction()
                    _childFragmentManager.findFragmentById(R.id.characteristics3)
                        ?.let { fragmentTransaction.remove(it) }
                    fragmentTransaction.commit()
                } else if (buyMode) {
                    holder.name.text =">${dataA[position].first.name}:${dataA[position].second} ${dataA[position].third}"
                    holder.name.setOnClickListener {
                        chosenItem =
                            dataA[position].first
                        user =
                            dataA[position].third
                        amount = 1
                        amountText.text = amount.toString()
                        val fr = _childFragmentManager.beginTransaction()
                        _childFragmentManager.findFragmentById(R.id.characteristics3)
                            ?.let { it1 -> fr.remove(it1) }
                        fr.add(R.id.characteristics3,
                            ItemCharacteristicsFragment(dataA[position].first, !buyMode))
                        fr.commit()
                    }
                } else {
                    holder.name.text =">${data[position].second.name}:${data[position].first}"
                    holder.name.setOnClickListener {
                        chosenItem =
                            data[position].second
                        amount = 1
                        amountText.text = amount.toString()
                        val fr = _childFragmentManager.beginTransaction()
                        _childFragmentManager.findFragmentById(R.id.characteristics3)
                            ?.let { it1 -> fr.remove(it1) }
                        fr.add(R.id.characteristics3,
                            ItemCharacteristicsFragment(data[position].second, buyMode))
                        fr.commit()
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            val a: ArrayList<Triple<Item, Int, String>> = ArrayList()
            if (auction)
                if (buyMode) {
                    for (i in dataA.indices) {
                        if (dataA[i].second != 0)
                            a.add(dataA[i])
                    }
                    dataA.clear()
                    dataA.addAll(a)
                    return dataA.size
                }
            return data.size
        }
    }

    override fun onClick(p0: View?) {
        if (p0==add)
            amountText.text = (++amount).toString()
        else if (p0==remove&&amount>1)
            amountText.text = (--amount).toString()
        else if (p0==back){
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentById(R.id.shop)?.let { fragmentTransaction.remove(it) }
            fragmentManager.findFragmentById(R.id.map)?.let { fragmentTransaction.remove(it) }
            fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        } else{
            if (auction) {
                if (p0==sellBuyButton){
                    if (chosenItem != null) {
                        if (!buyMode)
                            if (amount > player.inventory.quantity(chosenItem!!))
                                Toast.makeText(context, "You don't have enough items", Toast.LENGTH_SHORT).show()
                            else {
                                val fragmentTransaction = _childFragmentManager.beginTransaction()
                                _childFragmentManager.findFragmentById(R.id.characteristics3)
                                    ?.let { fragmentTransaction.remove(it) }
                                fragmentTransaction.commit()
                            }
                        else {
                            //player.buyItemOnAuction()
                            amountText.text = amount.toString()
                            val fragmentTransaction = _childFragmentManager.beginTransaction()
                            _childFragmentManager.findFragmentById(R.id.characteristics3)
                                ?.let { fragmentTransaction.remove(it) }
                            fragmentTransaction.commit()
                        }
                    } else Toast.makeText(context, "Please, choose item first", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (p0 == sellBuyButton) {
                    if (chosenItem == null)
                        Toast.makeText(context, "Choose item first", Toast.LENGTH_SHORT).show()
                    else if (!buyMode) {
                        if (!player.sellItem(Pair(amount, chosenItem!!)))
                            Toast.makeText(
                                context,
                                "You don't have enough items", Toast.LENGTH_SHORT
                            ).show()
                        else {
                            Toast.makeText(context, "Sold successfully", Toast.LENGTH_SHORT).show()
                            shopList.adapter = ShopAdapter(player.inventory.inventory)
                            val fr = _childFragmentManager.beginTransaction()
                            _childFragmentManager.findFragmentById(R.id.characteristics3)?.let { it1 -> fr.remove(it1) }
                            fr.commit()
                        }
                    } else if (player.buyItem(Pair(amount, chosenItem!!)))
                        Toast.makeText(context, "You don't have enough gold", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context, "Bought successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        if (auction) {
            if (p0==sellBuy){
                buyMode = p1
                val fr = _childFragmentManager.beginTransaction()
                _childFragmentManager.findFragmentById(R.id.characteristics3)?.let { fr.remove(it) }
                fr.commit()
                if (p1) {
                    val text="Buy"
                    modeText.text = text
                    sellBuyButton.text = text
                    shopList.adapter = ShopAdapter(ArrayList())
                    val ref = FirebaseDatabase.getInstance().getReference("Auction")
                    val genericTypeIndicator: GenericTypeIndicator<ArrayList<Triple<Item, Int, String>>> =
                        object : GenericTypeIndicator<ArrayList<Triple<Item, Int, String>>>() {}
                    ref.get().addOnCompleteListener { task ->
                        if (task.isSuccessful)
                            if (task.result.getValue(genericTypeIndicator) != null )
                                shopList.adapter = ShopAdapter(task.result.getValue(genericTypeIndicator)!!, true)
                    }
                } else {
                    val text="Place"
                    modeText.text = text
                    sellBuyButton.text = text
                    shopList.adapter = ShopAdapter(player.inventory.inventory)
                }
                amountText.text = amount.toString()
            }
        } else {
            if (p0 == sellBuy) {
                buyMode = p1
                if (buyMode) {
                    val text="Buy"
                    modeText.text = text
                    sellBuyButton.text = text
                    shopList.adapter = ShopAdapter(data)
                } else {
                    val text="Sell"
                    modeText.text = text
                    sellBuyButton.text = text
                    shopList.adapter = ShopAdapter(player.inventory.inventory)
                }
                amount=1
                amountText.text = amount.toString()
            }
        }
    }
}