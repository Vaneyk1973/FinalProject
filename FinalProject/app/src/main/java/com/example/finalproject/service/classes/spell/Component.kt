package com.example.finalproject.service.classes.spell

import com.example.finalproject.service.classes.Unit
import com.example.finalproject.service.serializers.ComponentSerializer
import kotlinx.serialization.Serializable

@Serializable(with= ComponentSerializer::class)
open class Component(name: String, id: Int, var available: Boolean) :
    Unit(name = name, id = id) {

    constructor():this("", -1, false)
    constructor(component: Component):this(component.name, component.id, component.available)

    fun avail() {
        available = true
    }
}