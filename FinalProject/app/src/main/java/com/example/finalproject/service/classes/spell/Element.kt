package com.example.finalproject.service.classes.spell

import com.example.finalproject.service.serializers.ElementSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ElementSerializer::class)
class Element(name: String, id: Int, available: Boolean, val element: Int, val baseDamage: Double) :
    Component(name = name, id = id, available = available) {
    constructor(element: Element) : this(
        element.name,
        element.id,
        element.available,
        element.element,
        element.baseDamage,
    )

    constructor(component: Component, element: Int, baseDamage: Double) : this(
        component.name,
        component.id,
        component.available,
        element,
        baseDamage
    )
}