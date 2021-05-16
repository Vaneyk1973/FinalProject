package com.example.finalproject.service.spell;

public class Element extends Component{
    private int element, base_damage;

    public Element(String name, int element, int base_damage, boolean available) {
        super(name , available);
        this.element = element;
        this.base_damage=base_damage;
    }

    public Element(Element element){
        super(element.getName(), element.isAvailable());
        this.element=element.element;
        base_damage=element.base_damage;
    }

    public int getElement() {
        return element;
    }

    public int getBase_damage() {
        return base_damage;
    }
}
