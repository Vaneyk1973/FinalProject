package com.example.finalproject.service.spell

class Form(val form:Int, name:String, available:Boolean):Component(name, available) {
    constructor(form:Form):this(form.form, form.name, form.available)
}