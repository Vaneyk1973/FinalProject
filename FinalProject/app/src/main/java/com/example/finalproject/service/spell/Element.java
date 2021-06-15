package com.example.finalproject.service.spell;

public class Element extends Component{
    private int element, baseDamage;

    public Element(String name, int element, int baseDamage, boolean available) {
        super(name , available);
        this.element = element;
        this.baseDamage = baseDamage;
    }

    public Element(Element element){
        super(element.getName(), element.isAvailable());
        this.element=element.element;
        baseDamage =element.baseDamage;
    }

    public int getElement() {
        return element;
    }

    public int getBaseDamage() {
        return baseDamage;
    }
}
