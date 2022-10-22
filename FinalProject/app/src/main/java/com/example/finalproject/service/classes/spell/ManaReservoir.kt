package com.example.finalproject.service.classes.spell

import com.example.finalproject.service.serializers.ManaReservoirSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ManaReservoirSerializer::class)
class ManaReservoir(name: String, id: Int, available: Boolean, val volume: Double) :
    Component(name = name, id = id, available = available) {
    constructor(manaReservoir: ManaReservoir) : this(
        manaReservoir.name,
        manaReservoir.id,
        manaReservoir.available,
        manaReservoir.volume
    )

    constructor(component: Component, volume: Double) : this(
        component.name,
        component.id,
        component.available,
        volume
    )
}