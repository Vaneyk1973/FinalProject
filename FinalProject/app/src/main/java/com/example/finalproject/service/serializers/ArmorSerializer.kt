package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.items.Armor
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

object
ArmorSerializer : KSerializer<Armor> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Armor") {
            element<Item>("itemPart")
            element<Double>("armor")
            element<Int>("typeOfArmor")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Armor =
        decoder.decodeStructure(descriptor) {
            var item = Item()
            var armor = -1.0
            var typeOfArmor = -1
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> item = decodeSerializableElement(descriptor, index, Item.serializer())
                    1 -> armor = decodeDoubleElement(descriptor, index)
                    2 -> typeOfArmor = decodeIntElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Armor(
                item = item,
                armor = armor,
                typeOfArmor = typeOfArmor
            )
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Armor) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Item.serializer(),
                Item(value)
            )
            encodeDoubleElement(descriptor, 1, value.armor)
            encodeIntElement(descriptor, 2, value.typeOfArmor)
        }
    }
}