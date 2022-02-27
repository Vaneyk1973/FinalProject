package com.example.finalproject.service.spell

class Type(var type:Int, name: String, available: Boolean): Component(name, available){
    constructor(type:Type):this(type.type, type.name, type.available)
}
