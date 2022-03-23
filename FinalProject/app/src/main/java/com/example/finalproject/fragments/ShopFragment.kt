package com.example.finalproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.items.Item
import com.example.finalproject.service.Triplex
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShopFragment(private val auction: Boolean = false) : Fragment() {
    private var chosenItem: Item? = null
    private var user: String? = null
    private var amount = 1
    private var buyMode = false
    private lateinit var amountText: TextView
    private lateinit var fm1: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fm1 = childFragmentManager
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sellBuy: SwitchCompat = requireView().findViewById(R.id.sell_buy_switch)
        val modeText: TextView = requireView().findViewById(R.id.shop_mode)
        amountText = requireView().findViewById(R.id.amount)
        val sellBuyButton: Button = requireView().findViewById(R.id.sell_buy_button)
        val back: Button = requireView().findViewById(R.id.shop_back_button)
        val add: FloatingActionButton = requireView().findViewById(R.id.add_button)
        val remove: FloatingActionButton = requireView().findViewById(R.id.remove_button)
        val shopList: RecyclerView = requireView().findViewById(R.id.items_to_sell)
        shopList.layoutManager = LinearLayoutManager(context)
        val data = ArrayList<Pair<Item, Int>>()
        for (i in MainActivity.shopList.indices)
            data.add(Pair(MainActivity.shopList[i], 999))
        shopList.adapter = ShopAdapter(MainActivity.player.inventory)
        if (!auction) {
            sellBuy.setOnCheckedChangeListener { _, isChecked ->
                buyMode = isChecked
                if (buyMode) {
                    modeText.text = "Buy"
                    sellBuyButton.text = "Buy"
                    shopList.adapter = ShopAdapter(data)
                } else {
                    modeText.text = "Sell"
                    sellBuyButton.text = "Sell"
                    shopList.adapter = ShopAdapter(MainActivity.player.inventory)
                }
                amount = 1
                amountText.text = "$amount"
            }
            sellBuy.isChecked = true
            sellBuy.isChecked = false
            sellBuyButton.setOnClickListener {
                if (chosenItem == null)
                    Toast.makeText(context, "Choose item first", Toast.LENGTH_SHORT).show()
                else if (!buyMode) {
                    if (!MainActivity.player.sellItem(Pair(chosenItem!!, amount)))
                        Toast.makeText(
                            context,
                            "You don't have enough items", Toast.LENGTH_SHORT
                        ).show()
                    else {
                        Toast.makeText(context, "Sold successfully", Toast.LENGTH_SHORT).show()
                        shopList.adapter = ShopAdapter(MainActivity.player.inventory)
                        val fm = childFragmentManager
                        val fr = fm.beginTransaction()
                        fm.findFragmentById(R.id.characteristics3)?.let { it1 -> fr.remove(it1) }
                        fr.commit()
                    }
                } else if (!MainActivity.player.buyItem(Pair(chosenItem!!, amount)))
                    Toast.makeText(context, "You don't have enough gold", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(context, "Bought successfully", Toast.LENGTH_SHORT).show()
            }
        } else {
            //TODO
            /*
                modeText.text = "Place"
                sellBuy.text = "Place/Buy"
                sellBuyButton.text = "Place"
                shopList.adapter = ShopAdapter(MainActivity.player.inventory)
                sellBuy.setOnCheckedChangeListener { buttonView, isChecked ->
                    mode = isChecked
                    val fr =
                        fm1!!.beginTransaction()
                    if (fm1!!.findFragmentById(
                            R.id.characteristics3
                        ) != null
                    ) fr.remove(
                        fm1!!.findFragmentById(
                            R.id.characteristics3
                        )!!
                    )
                    fr.commit()
                    if (isChecked) {
                        modeText.text = "Buy"
                        sellBuyButton.text = "Buy"
                        shopList.adapter =
                            ShopAdapter(ArrayList())
                        val ref = FirebaseDatabase.getInstance().getReference("Auction")
                        val genericTypeIndicator: GenericTypeIndicator<ArrayList<Triplex<Item, Int, String>>?> =
                            object :
                                GenericTypeIndicator<ArrayList<Triplex<Item?, Int?, String?>?>?>() {}
                        ref.get().addOnCompleteListener { task ->
                            if (task.isSuccessful) if (task.result.getValue(
                                    genericTypeIndicator
                                ) != null
                            ) shopList.adapter =
                                ShopAdapter(
                                    task.result.getValue(
                                        genericTypeIndicator
                                    ), true
                                )
                        }
                    } else {
                        modeText.text = "Place"
                        sellBuyButton.text = "Place"
                        shopList.adapter = ShopAdapter(MainActivity.player.inventory)
                    }
                    amountText.setText(amount.toString() + "")
                }
                sellBuyButton.setOnClickListener {
                    if (chosenItem != null) {
                        if (!mode) if (amount > MainActivity.player.getAmountOfItemsInInventory(
                                chosenItem
                            )
                        ) Toast.makeText(
                            context, "You don't have enough items", Toast.LENGTH_SHORT
                        ).show() else {
                            MainActivity.player.placeItemOnAuction(
                                Triplex(
                                    chosenItem,
                                    amount,
                                    MainActivity.player.user.login
                                ), shopList
                            )
                            val fr =
                                fm1!!.beginTransaction()
                            if (fm1!!.findFragmentById(
                                    R.id.characteristics3
                                ) != null
                            ) fr.remove(
                                fm1!!.findFragmentById(
                                    R.id.characteristics3
                                )!!
                            )
                            fr.commit()
                        } else {
                            MainActivity.player.buyItemOnAuction(
                                amount,
                                chosenItem!!.name,
                                user,
                                shopList,
                                context
                            )
                            amountText.setText(
                                amount.toString() + ""
                            )
                            val fr =
                                fm1!!.beginTransaction()
                            if (fm1!!.findFragmentById(
                                    R.id.characteristics3
                                ) != null
                            ) fr.remove(
                                fm1!!.findFragmentById(
                                    R.id.characteristics3
                                )!!
                            )
                            fr.commit()
                        }
                    } else Toast.makeText(context, "Please, choose item first", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            add.setOnClickListener {
                amount++
                amountText.setText(amount.toString() + "")
            }
            remove.setOnClickListener {
                if (amount > 1) {
                    amount--
                    amountText.setText(amount.toString() + "")
                }
            }
            back.setOnClickListener {
                val fm = parentFragmentManager
                val fr = fm.beginTransaction()
                fr.remove(fm.findFragmentById(R.id.shop)!!)
                if (fm.findFragmentById(R.id.map) != null) fr.remove(fm.findFragmentById(R.id.map)!!)
                fr.add(
                    R.id.map,
                    MapFragment(MainActivity.player.mapNum)
                )
                fr.add(R.id.status, StatusBarFragment())
                fr.add(R.id.menu, MenuFragment())
                fr.commit()*/
        }
    }

    private inner class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder> {
        private val data = ArrayList<Pair<Item, Int>>()
        private val dataA = ArrayList<Triplex<Item, Int, String>>()
        private var auction: Boolean

        constructor(data: ArrayList<Pair<Item, Int>>) {
            auction = false
            this.data.addAll(data)
        }

        constructor(dataA: ArrayList<Triplex<Item, Int, String>>, auction: Boolean) {
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
                    val fr = fm1.beginTransaction()
                    fm1.findFragmentById(R.id.characteristics3)?.let { fr.remove(it) }
                    fr.commit()
                } else {
                    if (!buyMode)
                        holder.name.text = ">${data[position].first.name}:${data[position].second}"
                    else holder.name.text = ">${data[position].first.name}"
                    holder.name.setOnClickListener {
                        chosenItem = data[position].first
                        amount = 1
                        amountText.text = amount.toString()
                        val fr = fm1.beginTransaction()
                        fm1.findFragmentById(R.id.characteristics3)?.let { it1 -> fr.remove(it1) }
                        fr.add(
                            R.id.characteristics3,
                            ItemCharacteristicsFragment(data[position].first, !buyMode)
                        )
                        fr.commit()
                    }
                }
            } else {
                //TODO
                /*
                    if (dataA.size == 0 && mode || data.size == 0 && !mode) {
                        val fr = fm1!!.beginTransaction()
                        if (fm1!!.findFragmentById(R.id.characteristics3) != null) fr.remove(
                            fm1!!.findFragmentById(R.id.characteristics3)!!
                        )
                        fr.commit()
                    } else if (mode) {
                        holder.name.text = """
                            >${dataA[position].first.name}:${dataA[position].second}
                            ${dataA[position].third}

                            """.trimIndent()
                        holder.name.setOnClickListener {
                            chosenItem =
                                dataA[position].first
                            user =
                                dataA[position].third
                            amount = 1
                            amount_text!!.text =
                                amount.toString() + ""
                            val fr =
                                fm1!!.beginTransaction()
                            if (fm1!!.findFragmentById(
                                    R.id.characteristics3
                                ) != null
                            ) fr.remove(
                                fm1!!.findFragmentById(
                                    R.id.characteristics3
                                )!!
                            )
                            fr.add(
                                R.id.characteristics3,
                                ItemCharacteristicsFragment(
                                    dataA[position].first,
                                    !mode
                                )
                            )
                            fr.commit()
                        }
                    } else {
                        holder.name.text = """
                            >${data[position].first.name}:${data[position].second}

                            """.trimIndent()
                        holder.name.setOnClickListener {
                            chosenItem =
                                data[position].first
                            amount = 1
                            amount_text!!.text =
                                amount.toString() + ""
                            val fr =
                                fm1!!.beginTransaction()
                            if (fm1!!.findFragmentById(
                                    R.id.characteristics3
                                ) != null
                            ) fr.remove(
                                fm1!!.findFragmentById(
                                    R.id.characteristics3
                                )!!
                            )
                            fr.add(
                                R.id.characteristics3,
                                ItemCharacteristicsFragment(
                                    data[position].first,
                                    mode
                                )
                            )
                            fr.commit()
                        }
                    }
                }*/
            }
        }

        override fun getItemCount(): Int {
            val a: ArrayList<Triplex<Item, Int, String>> = ArrayList()
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
}