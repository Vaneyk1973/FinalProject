package com.example.finalproject.service.classes.spell

import com.example.finalproject.service.serializers.ManaChannelSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ManaChannelSerializer::class)
class ManaChannel(name: String, id: Int, available: Boolean, val bandwidth: Double) :
    Component(name = name, id = id, available = available) {
    constructor(manaChannel: ManaChannel) : this(
        manaChannel.name,
        manaChannel.id,
        manaChannel.available,
        manaChannel.bandwidth
    )

    constructor(component: Component, bandwidth: Double) : this(
        component.name,
        component.id,
        component.available,
        bandwidth
    )
}