package com.example.finalproject;

public class Type extends Component {
    private final int type;

    public Type(String name, int type) {
        super(name);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
