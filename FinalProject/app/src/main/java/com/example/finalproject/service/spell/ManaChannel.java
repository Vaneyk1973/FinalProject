package com.example.finalproject.service.spell;

public class ManaChannel extends Component {
    private final int mps;

    public ManaChannel(String name, int mps, boolean available) {
        super(name, available);
        this.mps = mps;
    }

    public ManaChannel(ManaChannel manaChannel){
        super(manaChannel.getName(), manaChannel.isAvailable());
        mps= manaChannel.getMps();
    }

    public int getMps() {
        return mps;
    }
}

