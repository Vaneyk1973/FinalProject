package com.example.finalproject.service

import android.graphics.Bitmap
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class Map(mapXml:XmlPullParser) {

    val map=ArrayList<ArrayList<MapTile>>()

    init{
        try {
            while (mapXml.eventType != XmlPullParser.END_DOCUMENT) {
                if (mapXml.eventType == XmlPullParser.START_TAG && mapXml.name == "row")
                    map.add(ArrayList())
                if (mapXml.eventType == XmlPullParser.START_TAG && mapXml.name == "map_title") {
                    val type = mapXml.getAttributeValue(0).toInt()
                    val tile = MapTile(type)
                    map[map.size-1].add(tile)
                }
                mapXml.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    class MapTile(val id:Int){
        private lateinit var texture: Bitmap

        fun getTexture():Bitmap{
            return Bitmap.createBitmap(texture)
        }

        fun setTexture(texture:Bitmap){
            this.texture= Bitmap.createBitmap(texture)
        }
    }
}