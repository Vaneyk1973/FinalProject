package com.example.finalproject.service.classes.spell

import com.example.finalproject.service.serializers.FormSerializer
import kotlinx.serialization.Serializable

@Serializable(with = FormSerializer::class)
class Form(name: String, id: Int, available: Boolean, val form: Int) :
    Component(name = name, id = id, available = available) {
    constructor(form: Form) : this(form.name, form.id, form.available, form.form)
    constructor(component: Component, form: Int) : this(
        component.name,
        component.id,
        component.available,
        form
    )
}