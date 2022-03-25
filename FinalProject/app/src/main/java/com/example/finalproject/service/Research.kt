package com.example.finalproject.service

import com.example.finalproject.fragments.MainActivity
import com.example.finalproject.fragments.MainActivity.Companion.elements

data class Research(
    val requiredResearches:ArrayList<Research>,
    val name:String,
    val cost:Int,
    val tier:Int,
    val effect:Int,
    var researched:Boolean,
    var available:Boolean) {

    private fun affect()= elements[effect].avail()

    private fun enable(){
        for (i in requiredResearches)
            if (!i.researched) return
        available=true
    }

    fun research(){
        for (i in MainActivity.researches)
            i.enable()
        this.affect()
    }
    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.example.finalproject.service.Research research = (com.example.finalproject.service.Research) o;
        return Objects.equals(name, research.name);
    }*/
}