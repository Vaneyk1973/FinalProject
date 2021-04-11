package com.example.finalproject;

public class ManaReservoir extends Component {
    private final int volume;

    public ManaReservoir(String name, int volume) {
        super(name);
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }
}
