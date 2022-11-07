package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.finalproject.MainActivity
import com.example.finalproject.MainActivity.Companion.player
import com.example.finalproject.R
import com.example.finalproject.service.classes.Inventory
import com.example.finalproject.service.classes.items.Item
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShopFragment(private val auction: Boolean = false) : Fragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener, ValueEventListener {

    private val inf: Int = 1e9.toInt()

    private var chosenItem: Item? = null
    private var chosenItemUser: String = ""
    private var amount = 1
    private var buyMode = false
    private val shopListData = ArrayList<Pair<Int, Item>>()
    private lateinit var amountText: TextView
    private lateinit var modeText: TextView
    private lateinit var sellBuyButton: Button
    private lateinit var back: Button
    private lateinit var sellBuy: SwitchCompat
    private lateinit var add: FloatingActionButton
    private lateinit var remove: FloatingActionButton
    private lateinit var shopList: RecyclerView
    private val auctionReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Auction")

    /**
     * inflates fragment's layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    /**
     * initializes graphic components
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellBuy = requireView().findViewById(R.id.sell_buy_switch)
        modeText = requireView().findViewById(R.id.shop_mode)
        amountText = requireView().findViewById(R.id.amount)
        sellBuyButton = requireView().findViewById(R.id.sell_buy_button)
        back = requireView().findViewById(R.id.shop_back_button)
        add = requireView().findViewById(R.id.add_button)
        remove = requireView().findViewById(R.id.remove_button)
        shopList = requireView().findViewById(R.id.items_to_sell)
        shopList.layoutManager = LinearLayoutManager(context)
        for (i in MainActivity.assets.shopList)
            shopListData.add(Pair(inf, i))
        sellBuy.setOnCheckedChangeListener(this)
        if (!auction) {
            sellBuy.isChecked = true
            sellBuy.isChecked = false
            shopList.adapter = ShopAdapter(getInventoryAsArrayList())
        } else {
            var text = "Place"
            modeText.text = text
            sellBuyButton.text = text
            text = "Place/Buy"
            sellBuy.text = text
            shopList.adapter = AuctionAdapter(getInventoryAsArrayList(), player.user.uID)
        }
        sellBuyButton.setOnClickListener(this)
        back.setOnClickListener(this)
        add.setOnClickListener(this)
        remove.setOnClickListener(this)
    }

    /**
     * sets the click listener for needed views
     */
    override fun onClick(p0: View?) {
        if (p0 == add)
            amountText.text = (++amount).toString()
        else if (p0 == remove && amount > 1)
            amountText.text = (--amount).toString()
        else if (p0 == back) {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentManager.findFragmentById(R.id.shop)?.let { fragmentTransaction.remove(it) }
            fragmentTransaction.add(R.id.map, MapFragment(player.mapNumber))
            fragmentTransaction.add(R.id.status, StatusBarFragment())
            fragmentTransaction.add(R.id.menu, MenuFragment())
            fragmentTransaction.commit()
        } else {
            if (auction) {
                if (p0 == sellBuyButton) {
                    if (chosenItem == null)
                        Toast.makeText(context, "Please, choose item first", Toast.LENGTH_SHORT)
                            .show()
                    else {
                        if (buyMode) {
                            val ref = auctionReference.child(chosenItemUser)
                            val item = Pair(amount, chosenItem!!)
                            ref.get().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    for (k in (it.result.value as ArrayList<*>).indices) {
                                        val i = (it.result.value as ArrayList<*>)[k]
                                        if (((i as HashMap<*, *>)["second"] as HashMap<*, *>)["id"].toString()
                                                .toInt() == item.second.id
                                        ) {
                                            if (i["first"].toString().toInt() == item.first) {
                                                if (i["bought"] == false) {
                                                    if (player.buyItem(item)) {
                                                        ref.child(k.toString()).child("bought")
                                                            .setValue(true)
                                                        updateAuction()
                                                        break
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong, please try again later",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            amount = 1
                            amountText.text = amount.toString()
                            val fragmentTransaction = childFragmentManager.beginTransaction()
                            childFragmentManager.findFragmentById(R.id.characteristics3)
                                ?.let { fragmentTransaction.remove(it) }
                            fragmentTransaction.commit()
                        } else {
                            if (amount > player.inventory.quantity(chosenItem!!.id)) {
                                Toast.makeText(
                                    context,
                                    "You don't have enough items",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val item = Pair(amount, chosenItem!!)
                                auctionReference.child(player.user.uID).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            if (player.removeItemsFromInventory(item)) {
                                                val ref = auctionReference.child(player.user.uID)
                                                val num = it.result.childrenCount
                                                ref.child(num.toString()).setValue(item)
                                                ref.child(num.toString()).child("bought")
                                                    .setValue(false)
                                                ref.child(num.toString())
                                                    .addValueEventListener(this)
                                                shopList.adapter =
                                                    AuctionAdapter(
                                                        getInventoryAsArrayList(),
                                                        player.user.uID
                                                    )
                                            }
                                        }
                                    }
                                val fragmentTransaction = childFragmentManager.beginTransaction()
                                childFragmentManager.findFragmentById(R.id.characteristics3)
                                    ?.let { fragmentTransaction.remove(it) }
                                fragmentTransaction.commit()
                            }
                        }
                    }
                }
            } else {
                if (p0 == sellBuyButton) {
                    if (chosenItem == null)
                        Toast.makeText(context, "Choose item first", Toast.LENGTH_SHORT).show()
                    else if (!buyMode) {
                        if (player.sellItem(Pair(amount, chosenItem!!))) {
                            amount = 1
                            amountText.text = amount.toString()
                            Toast.makeText(context, "Sold successfully", Toast.LENGTH_SHORT).show()
                            shopList.adapter = ShopAdapter(getInventoryAsArrayList())
                            val fr = childFragmentManager.beginTransaction()
                            childFragmentManager.findFragmentById(R.id.characteristics3)
                                ?.let { it1 -> fr.remove(it1) }
                            fr.commit()
                        } else {
                            Toast.makeText(
                                context,
                                "You don't have enough items", Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (player.buyItem(Pair(amount, chosenItem!!))) {
                        amount = 1
                        amountText.text = amount.toString()
                        Toast.makeText(context, "Bought successfully", Toast.LENGTH_SHORT).show()
                    } else
                        Toast.makeText(context, "You don't have enough gold", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    /**
     * @param p0 the switch that has changed
     * @param p1 its value
     * modifies the buy/sell mode and changes the layout
     */
    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        if (auction) {
            if (p0 == sellBuy) {
                val fr = childFragmentManager.beginTransaction()
                childFragmentManager.findFragmentById(R.id.characteristics3)?.let { fr.remove(it) }
                fr.commit()
                buyMode = p1
                if (buyMode) {
                    add.visibility = View.GONE
                    amountText.visibility = View.GONE
                    remove.visibility = View.GONE
                    val text = "Buy"
                    modeText.text = text
                    sellBuyButton.text = text
                    updateAuction()
                } else {
                    add.visibility = View.VISIBLE
                    amountText.visibility = View.VISIBLE
                    remove.visibility = View.VISIBLE
                    val text = "Place"
                    modeText.text = text
                    sellBuyButton.text = text
                    shopList.adapter = ShopAdapter(getInventoryAsArrayList())
                }
                amount = 1
                amountText.text = amount.toString()
            }
        } else {
            if (p0 == sellBuy) {
                val fr = childFragmentManager.beginTransaction()
                childFragmentManager.findFragmentById(R.id.characteristics3)?.let { fr.remove(it) }
                fr.commit()
                buyMode = p1
                if (buyMode) {
                    val text = "Buy"
                    modeText.text = text
                    sellBuyButton.text = text
                    shopList.adapter = ShopAdapter(shopListData)
                } else {
                    val text = "Sell"
                    modeText.text = text
                    sellBuyButton.text = text
                    shopList.adapter = ShopAdapter(getInventoryAsArrayList())
                }
                amount = 1
                amountText.text = amount.toString()
            }
        }
    }

    /**
     * updates the list of items presented at the auction
     */
    private fun updateAuction() {
        FirebaseDatabase.getInstance().getReference("Auction").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.value != null) {
                        val sellList = ArrayList<Triple<Int, Item, String>>()
                        for (i in (it.result.value as HashMap<*, *>)) {
                            for (j in (i.value as ArrayList<HashMap<*, *>>)) {
                                if (j["bought"] == false)
                                    sellList.add(
                                        Triple(
                                            j["first"].toString().toInt(),
                                            (j["second"] as HashMap<*, *>).run {
                                                Item(
                                                    this["name"].toString(),
                                                    this["id"].toString().toInt(),
                                                    this["costSell"].toString().toInt(),
                                                    this["costBuy"].toString().toInt(),
                                                    this["rarity"].toString().toInt(),
                                                    this["category"].toString().toInt()
                                                )
                                            },
                                            i.key.toString()
                                        )
                                    )
                            }
                        }
                        shopList.adapter = AuctionAdapter(sellList)
                    } else {
                        shopList.adapter = AuctionAdapter(ArrayList())
                    }
                } else {
                    sellBuy.isChecked = false
                    Toast.makeText(
                        context,
                        "Something went wrong, please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /**
     * @param inventory the inventory of a player
     * @return the inventory of a player in the form need for the view
     */
    private fun getInventoryAsArrayList(inventory: Inventory = player.inventory) =
        inventory.inventory.run {
            val items: ArrayList<Pair<Int, Item>> = ArrayList()
            for (i in keys)
                items.add(Pair(this[i]!!, MainActivity.assets.items[i]!!))
            return@run items
        }

    private inner class ShopAdapter(private val data: ArrayList<Pair<Int, Item>>) :
        RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.textView)
        }

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
            return ViewHolder(view)
        }

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (buyMode)
                holder.name.text = ">${data[position].second.name}"
            else
                holder.name.text = ">${data[position].second.name}: ${data[position].first}"
            holder.name.setOnClickListener {
                chosenItem = data[position].second
                amount = 1
                amountText.text = amount.toString()
                val fr = childFragmentManager.beginTransaction()
                childFragmentManager.findFragmentById(R.id.characteristics3)
                    ?.let { it1 -> fr.remove(it1) }
                fr.add(
                    R.id.characteristics3,
                    ItemCharacteristicsFragment(data[position].second, buyMode)
                )
                fr.commit()
            }
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size
    }

    private inner class AuctionAdapter(val data: ArrayList<Triple<Int, Item, String>>) :
        RecyclerView.Adapter<AuctionAdapter.ViewHolder>() {

        constructor(data: ArrayList<Pair<Int, Item>>, user: String) : this(data.run {
            val newData = ArrayList<Triple<Int, Item, String>>()
            for (i in data)
                newData.add(Triple(i.first, i.second, user))
            newData
        })

        private inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.textView)
        }

        /**
         * inflates the list item layout
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
            return ViewHolder(view)
        }

        /**
         * @return amount of items in the list
         */
        override fun getItemCount(): Int = data.size

        /**
         * @param holder a holder for a list item view
         * sets the text displayed in the list
         */
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: AuctionAdapter.ViewHolder, position: Int) {
            holder.name.text = ">${data[position].second.name}: ${data[position].first}"
            holder.name.setOnClickListener {
                chosenItem = data[position].second
                chosenItemUser = data[position].third
                if (buyMode)
                    amount = data[position].first
                val fragmentTransaction = childFragmentManager.beginTransaction()
                childFragmentManager.findFragmentById(R.id.characteristics3)
                    ?.let { it1 -> fragmentTransaction.remove(it1) }
                fragmentTransaction.add(
                    R.id.characteristics3,
                    ItemCharacteristicsFragment(chosenItem!!, buyMode)
                )
                fragmentTransaction.commit()
            }
        }

    }

    /**
     * @param snapshot the snapshot of a database with changes
     * sets the action on the event if a database value has changed
     */
    override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.child("bought").value == true) {
            player.gold += snapshot.child("first").value.toString()
                .toInt() * snapshot.child("second")
                .child("costBuy").value.toString().toInt()
        }
    }

    /**
     * logs the event of an error
     */
    override fun onCancelled(error: DatabaseError) {
        Log.e("DataBaseError", error.message)
    }
}