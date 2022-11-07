package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.items.Item
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ItemSerializer : KSerializer<Item> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Color") {
            element<String>("name")
            element<Int>("id")
            element<Int>("costSell")
            element<Int>("costBuy")
            element<Int>("rarity")
            element<Int>("category")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Item =
        decoder.decodeStructure(descriptor) {
            var name = ""
            var id = -1
            var costSell = -1
            var costBuy = -1
            var rarity = -1
            var category = -1
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> id = decodeIntElement(descriptor, index)
                    2 -> costSell = decodeIntElement(descriptor, index)
                    3 -> costBuy = decodeIntElement(descriptor, index)
                    4 -> rarity = decodeIntElement(descriptor, index)
                    5 -> category = decodeIntElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Item(
                name = name,
                id = id,
                costSell = costSell,
                costBuy = costBuy,
                rarity = rarity,
                category = category
            )
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Item) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeIntElement(descriptor, 1, value.id)
            encodeIntElement(descriptor, 2, value.costSell)
            encodeIntElement(descriptor, 3, value.costBuy)
            encodeIntElement(descriptor, 4, value.rarity)
            encodeIntElement(descriptor, 5, value.category)
        }
    }

}