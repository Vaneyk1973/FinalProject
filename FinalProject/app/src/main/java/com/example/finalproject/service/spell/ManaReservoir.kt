package com.example.finalproject.service.spell

class ManaReservoir(val volume:Double, name: String, available: Boolean): Component(name, available) {
    constructor(manaReservoir: ManaReservoir) : this(manaReservoir.volume, manaReservoir.name, manaReservoir.available)
}