package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.spell.Component
import com.example.finalproject.service.classes.spell.Type
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object TypeSerializer : KSerializer<Type> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Type") {
            element<Component>("component")
            element<Int>("type")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Type =
        decoder.decodeStructure(descriptor) {
            var component = Component()
            var type = -1
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> component =
                        decodeSerializableElement(descriptor, index, Component.serializer())
                    1 -> type = decodeIntElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Type(component = component, type = type)
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Type) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Component.serializer(),
                Component(value)
            )
            encodeIntElement(descriptor, 1, value.type)
        }
    }
}