package com.example.finalproject;

public class Element extends Component{
    private int element;

    public Element(String name, int element) {
        super(name);
        this.element = element;
    }

    public int getElement() {
        return element;
    }
}
