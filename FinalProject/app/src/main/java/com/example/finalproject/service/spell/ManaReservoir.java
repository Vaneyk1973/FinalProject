package com.example.finalproject.service.spell;

public class ManaReservoir extends Component {
    private final int volume;

    public ManaReservoir(String name, int volume, boolean available) {
        super(name, available);
        this.volume = volume;
    }

    public ManaReservoir(ManaReservoir manaReservoir){
        super(manaReservoir.getName(), manaReservoir.isAvailable());
        volume=manaReservoir.volume;
    }

    public int getVolume() {
        return volume;
    }
}
