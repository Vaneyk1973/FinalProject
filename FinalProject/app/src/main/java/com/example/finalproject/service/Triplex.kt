package com.example.finalproject.service

data class Triplex<T1, T2, T3>(var first:T1, var second:T2, var third:T3) {
    override fun toString(): String {
        return "Triplex{ frist=$first, second=$second, third=$third}"
    }

    fun getPair(): android.util.Pair<T1, T2> {
        return android.util.Pair<T1, T2>(first, second)
    }
}