package com.example.finalproject.service.classes.spell

class ManaChannel(val mps:Double, name: String, available: Boolean):Component(name, available) {
    constructor(manaChannel: ManaChannel) : this(manaChannel.mps, manaChannel.name, manaChannel.available)
}