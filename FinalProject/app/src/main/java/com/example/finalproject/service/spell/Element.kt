package com.example.finalproject.service.spell

class Element(name:String, private val element:Int, val baseDamage:Double, available:Boolean):Component(name, available) {
    constructor(element: Element) : this(element.name, element.element, element.baseDamage, element.available)

    fun getElement()=element
}