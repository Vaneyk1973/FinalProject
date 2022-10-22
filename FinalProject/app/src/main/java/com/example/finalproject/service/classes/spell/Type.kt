package com.example.finalproject.service.classes.spell

import com.example.finalproject.service.serializers.TypeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = TypeSerializer::class)
class Type(name: String, id: Int, available: Boolean, val type: Int) :
    Component(name = name, id = id, available = available) {
    constructor(type: Type) : this(type.name, type.id, type.available, type.type)
    constructor(component: Component, type: Int) : this(
        component.name,
        component.id,
        component.available,
        type
    )
}
