package com.example.finalproject.service.classes.spell

import com.example.finalproject.service.classes.Unit
import com.example.finalproject.service.serializers.ComponentSerializer
import kotlinx.serialization.Serializable

@Serializable(with= ComponentSerializer::class)
open class Component(name: String, id: Int, var available: Boolean) :
    Unit(name = name, id = id) {

    constructor():this("", -1, false)
    constructor(component: Component):this(component.name, component.id, component.available)

    /**
     * makes the components available
     */
    fun avail() {
        available = true
    }

    /**
     * @return the string value of an object
     */
    override fun toString(): String ="name:$name, id:$id, available:$available"
}