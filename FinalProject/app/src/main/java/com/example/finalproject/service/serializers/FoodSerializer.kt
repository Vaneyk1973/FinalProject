package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.items.Food
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

object FoodSerializer : KSerializer<Food> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Food") {
            element<Item>("item")
            element<Double>("manaRecovery")
            element<Double>("healthRecovery")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Food =
        decoder.decodeStructure(descriptor) {
            var item = Item()
            var manaRecovery = 0.0
            var healthRecovery = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> item = decodeSerializableElement(descriptor, index, Item.serializer())
                    1 -> manaRecovery = decodeDoubleElement(descriptor, index)
                    2 -> healthRecovery = decodeDoubleElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Food(item = item, manaRecovery = manaRecovery, healthRecovery = healthRecovery)
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Food) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Item.serializer(),
                Item(value)
            )
            encodeDoubleElement(descriptor, 1, value.manaRecovery)
            encodeDoubleElement(descriptor, 2, value.healthRecovery)
        }
    }
}