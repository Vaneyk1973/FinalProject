package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.spell.Component
import com.example.finalproject.service.classes.spell.ManaChannel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object ManaChannelSerializer : KSerializer<ManaChannel> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ManaChannel") {
            element<Component>("component")
            element<Double>("bandwidth")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): ManaChannel =
        decoder.decodeStructure(descriptor) {
            var component = Component()
            var bandwidth = 0.0
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> component =
                        decodeSerializableElement(descriptor, index, Component.serializer())
                    1 -> bandwidth = decodeDoubleElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            ManaChannel(component = component, bandwidth = bandwidth)
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: ManaChannel) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Component.serializer(),
                Component(value)
            )
            encodeDoubleElement(descriptor, 1, value.bandwidth)
        }
    }
}