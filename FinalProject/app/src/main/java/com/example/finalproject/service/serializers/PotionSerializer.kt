package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.items.Potion
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object PotionSerializer : KSerializer<Potion> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Potion") {
            element<Item>("item")
            element<Int>("lastingTime")
            element<ArrayList<Int>>("effects")
            element<ArrayList<Double>>("strengths")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Potion =
        decoder.decodeStructure(descriptor) {
            var item = Item()
            var lastingTime = -1
            var effects = ArrayList<Int>()
            var strengths = ArrayList<Double>()
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> item = decodeSerializableElement(descriptor, index, Item.serializer())
                    1 -> lastingTime = decodeIntElement(descriptor, index)
                    2 -> effects = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(Int.serializer())
                    ) as ArrayList<Int>
                    3 -> strengths = decodeSerializableElement(
                        descriptor,
                        index,
                        ListSerializer(Double.serializer())
                    ) as ArrayList<Double>
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Potion(
                item = item,
                lastingTime = lastingTime,
                effects = effects,
                strengths = strengths
            )
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Potion) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                ItemSerializer,
                Item(value)
            )
            encodeIntElement(descriptor, 1, value.lastingTime)
            encodeSerializableElement(
                descriptor,
                2,
                ListSerializer(Int.serializer()),
                value.effects
            )
            encodeSerializableElement(
                descriptor,
                3,
                ListSerializer(Double.serializer()),
                value.strengths
            )
        }
    }
}