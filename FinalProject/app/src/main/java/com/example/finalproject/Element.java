package com.example.finalproject;

public class Element extends Component{
    private int element, base_damage;

    public Element(String name, int element) {
        super(name);
        this.element = element;
    }

    public int getElement() {
        return element;
    }

    public int getBase_damage() {
        return base_damage;
    }
}
