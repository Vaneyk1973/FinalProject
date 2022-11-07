package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.spell.Component
import com.example.finalproject.service.classes.spell.ManaReservoir
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ManaReservoirSerializer : KSerializer<ManaReservoir> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ManaReservoir") {
            element<Component>("component")
            element<Int>("volume")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): ManaReservoir =
        decoder.decodeStructure(descriptor) {
            var component = Component()
            var volume = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> component =
                        decodeSerializableElement(descriptor, index, Component.serializer())
                    1 -> volume = decodeDoubleElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected error: $index")
                }
            }
            ManaReservoir(component = component, volume = volume)
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: ManaReservoir) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Component.serializer(),
                Component(value)
            )
            encodeDoubleElement(descriptor, 1, value.volume)
        }
    }
}