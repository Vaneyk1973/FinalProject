package com.example.finalproject.service.classes

import android.graphics.Bitmap
import kotlinx.serialization.Contextual
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class Map(mapXml: XmlPullParser) {
    val map: ArrayList<ArrayList<MapTile>> = ArrayList<ArrayList<MapTile>>()
    var length: Int = 0
    var width: Int = 0

    init {
        try {
            while (mapXml.eventType != XmlPullParser.END_DOCUMENT) {
                if (mapXml.eventType == XmlPullParser.START_TAG && mapXml.name == "row")
                    map.add(ArrayList())
                if (mapXml.eventType == XmlPullParser.START_TAG && mapXml.name == "map_title") {
                    val type = mapXml.getAttributeValue(0).toInt()
                    val tile = MapTile(type)
                    map[map.size - 1].add(tile)
                }
                mapXml.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        length = map.size
        width = map[0].size
    }

    class MapTile(val id: Int) {
        @Contextual
        private lateinit var texture: Bitmap

        /**
         * @return the texture of a map tile
         */
        fun getTexture(): Bitmap = Bitmap.createBitmap(texture)

        /**
         * @param texture the texture to be set
         * sets the texture to the map tile
         */
        fun setTexture(texture: Bitmap) {
            this.texture = Bitmap.createBitmap(texture)
        }
    }
}