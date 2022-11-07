package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.spell.Component
import com.example.finalproject.service.classes.spell.Element
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ElementSerializer : KSerializer<Element> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Element") {
            element<Component>("componentPart")
            element<Int>("element")
            element<Double>("baseDamage")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Element =
        decoder.decodeStructure(descriptor) {
            var component = Component()
            var element = -1
            var baseDamage = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> component =
                        decodeSerializableElement(descriptor, index, Component.serializer())
                    1 -> element = decodeIntElement(descriptor, index)
                    2 -> baseDamage = decodeDoubleElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Element(
                component = component,
                element = element,
                baseDamage = baseDamage
            )
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Element) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Component.serializer(),
                Component(value)
            )
            encodeIntElement(descriptor, 1, value.element)
            encodeDoubleElement(descriptor, 2, value.baseDamage)
        }
    }
}