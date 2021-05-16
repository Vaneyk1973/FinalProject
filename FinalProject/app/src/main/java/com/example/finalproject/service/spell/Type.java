package com.example.finalproject.service.spell;

public class Type extends Component {
    private final int type;

    public Type(String name, int type, boolean available) {
        super(name, available);
        this.type = type;
    }

    public Type(Type type){
        super(type.getName(), type.isAvailable());
        this.type=type.type;
    }

    public int getType() {
        return type;
    }
}
