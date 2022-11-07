package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.spell.Component
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ComponentSerializer : KSerializer<Component> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Component") {
            element<String>("name")
            element<Int>("id")
            element<Boolean>("available")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Component =
        decoder.decodeStructure(descriptor) {
            var name = ""
            var id = -1
            var available = false
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> id = decodeIntElement(descriptor, index)
                    2 -> available = decodeBooleanElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Component(name = name, id = id, available = available)
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeIntElement(descriptor, 1, value.id)
            encodeBooleanElement(descriptor, 2, value.available)
        }
    }
}