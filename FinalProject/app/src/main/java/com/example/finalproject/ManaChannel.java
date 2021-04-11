package com.example.finalproject;

public class ManaChannel extends Component {
    private final int mps;

    public ManaChannel(String name, int mps) {
        super(name);
        this.mps = mps;
    }

    public int getMps() {
        return mps;
    }
}

