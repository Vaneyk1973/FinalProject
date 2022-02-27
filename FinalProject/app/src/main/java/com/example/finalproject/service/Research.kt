package com.example.finalproject.service

import com.example.finalproject.fragments.MainActivity.elements

data class Research(
    val requiredResearches:ArrayList<Research>,
    val name:String,
    val cost:Int,
    val tier:Int,
    val effect:Int,
    var researched:Boolean,
    var available:Boolean) {

    fun affect()= elements[effect].avail()

    fun enable(){
        for (i in requiredResearches)
            if (!i.researched) return
        available=true
    }
    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.example.finalproject.service.Research research = (com.example.finalproject.service.Research) o;
        return Objects.equals(name, research.name);
    }*/

}