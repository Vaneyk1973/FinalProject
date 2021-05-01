package com.example.finalproject;

import android.util.Log;

import java.io.Serializable;

public class Spell implements Serializable {
    private int mana_consumption, damage, lasting_time;
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
        damage=manaReservoir.getVolume()*element.getBase_damage();
        lasting_time=manaReservoir.getVolume()/manaChannel.getMps();
        mana_consumption=manaReservoir.getVolume();
    }

    public Spell(Spell spell){
        element=new Element(spell.element);
        type=new Type(spell.type);
        form=new Form(spell.form);
        manaChannel=new ManaChannel(spell.manaChannel);
        manaReservoir=new ManaReservoir(spell.manaReservoir);
        name=spell.name;
        mana_consumption=spell.mana_consumption;
        damage=spell.damage;
        lasting_time=spell.lasting_time;
    }


    public void affect (Enemy enemy){
        if (MainActivity.player.getMana()>=mana_consumption){
            consume_mana();
            enemy.take_damage(damage);
        }
        else {
            Log.d("TTTTT", "Not enough mana");
        }
    }

    public void consume_mana(){
        MainActivity.player.setMana(MainActivity.player.getMana()-mana_consumption);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMana_consumption() {
        return mana_consumption;
    }

    public void setMana_consumption(int mana_consumption) {
        this.mana_consumption = mana_consumption;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getLasting_time() {
        return lasting_time;
    }

    public void setLasting_time(int lasting_time) {
        this.lasting_time = lasting_time;
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

