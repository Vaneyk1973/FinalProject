package com.example.finalproject.service;

import android.util.Pair;

public class Triplex<T, T1, T2> {
    public T first;
    public T1 second;
    public T2 third;

    public Triplex() {
    }

    public Triplex(T first, T1 second, T2 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Pair<T, T1> getPair(){
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return "Triplex{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
