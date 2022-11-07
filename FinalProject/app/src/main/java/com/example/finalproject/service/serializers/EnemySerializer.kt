package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.entities.Enemy
import com.example.finalproject.service.classes.entities.Entity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object EnemySerializer : KSerializer<Enemy> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Enemy") {
            element<Entity>("entity")
            element<Damage>("damage")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Enemy =
        decoder.decodeStructure(descriptor) {
            var entity = Entity()
            var damage = Damage()
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> entity = decodeSerializableElement(descriptor, index, Entity.serializer())
                    1 -> damage = decodeSerializableElement(descriptor, index, Damage.serializer())
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Enemy(entity = entity, damage = damage)
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Enemy) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, Entity.serializer(), Entity(value))
            encodeSerializableElement(descriptor, 1, Damage.serializer(), value.damage)
        }
    }
}