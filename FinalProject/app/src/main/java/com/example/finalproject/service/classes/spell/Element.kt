package com.example.finalproject.service.classes.spell

class Element(name:String, val element:Int, val baseDamage:Double, available:Boolean):Component(name, available) {
    constructor(element: Element) : this(element.name, element.element, element.baseDamage, element.available)
}