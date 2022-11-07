package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.items.Ingredient
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

object IngredientSerializer : KSerializer<Ingredient> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Ingredient") {
            element<Item>("item")
            element<Int>("effect")
            element<Double>("effectStrength")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Ingredient =
        decoder.decodeStructure(descriptor) {
            var item = Item()
            var effect = -1
            var effectStrength = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> item = decodeSerializableElement(descriptor, index, Item.serializer())
                    1 -> effect = decodeIntElement(descriptor, index)
                    2 -> effectStrength = decodeDoubleElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Ingredient(item = item, effect = effect, effectStrength = effectStrength)
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Ingredient) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Item.serializer(),
                Item(value)
            )
            encodeIntElement(descriptor, 1, value.effect)
            encodeDoubleElement(descriptor, 2, value.effectStrength)
        }
    }
}