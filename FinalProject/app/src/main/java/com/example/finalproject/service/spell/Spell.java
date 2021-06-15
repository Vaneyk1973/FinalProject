package com.example.finalproject.service.spell;

import com.example.finalproject.entities.Enemy;
import com.example.finalproject.fragments.MainActivity;

import java.io.Serializable;

public class Spell implements Serializable {
    private int manaConsumption, damage, lastingTime;
    private Element element;
    private Type type;
    private Form form;
    private ManaChannel manaChannel;
    private ManaReservoir manaReservoir;
    private String name;

    public Spell(Element element, Type type, Form form, ManaChannel manaChannel, ManaReservoir manaReservoir, String name) {
        this.element = element;
        this.type = type;
        this.form = form;
        this.manaChannel = manaChannel;
        this.manaReservoir = manaReservoir;
        this.name = name;
        damage=manaReservoir.getVolume()*element.getBaseDamage();
        lastingTime =manaReservoir.getVolume()/manaChannel.getMps();
        manaConsumption =manaReservoir.getVolume();
    }

    public Spell(Spell spell){
        element=new Element(spell.element);
        type=new Type(spell.type);
        form=new Form(spell.form);
        manaChannel=new ManaChannel(spell.manaChannel);
        manaReservoir=new ManaReservoir(spell.manaReservoir);
        name=spell.name;
        manaConsumption =spell.manaConsumption;
        damage=spell.damage;
        lastingTime =spell.lastingTime;
    }


    public void affect (Enemy enemy){
        if (MainActivity.player.getMana()>= manaConsumption){
            consume_mana();
            enemy.take_damage(damage);
        }
    }

    public void consume_mana(){
        MainActivity.player.setMana(MainActivity.player.getMana()- manaConsumption);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManaConsumption() {
        return manaConsumption;
    }

    public void setManaConsumption(int manaConsumption) {
        this.manaConsumption = manaConsumption;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getLastingTime() {
        return lastingTime;
    }

    public void setLastingTime(int lastingTime) {
        this.lastingTime = lastingTime;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public ManaChannel getManaChannel() {
        return manaChannel;
    }

    public void setManaChannel(ManaChannel manaChannel) {
        this.manaChannel = manaChannel;
    }

    public ManaReservoir getManaReservoir() {
        return manaReservoir;
    }

    public void setManaReservoir(ManaReservoir manaReservoir) {
        this.manaReservoir = manaReservoir;
    }
}

