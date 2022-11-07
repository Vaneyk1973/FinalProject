package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.Loot
import com.example.finalproject.service.classes.Resistances
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

object EntitySerializer : KSerializer<Entity> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Entity") {
            element<String>("name")
            element<Int>("id")
            element<Double>("health")
            element<Double>("maxHealth")
            element<Double>("healthRegen")
            element<Double>("mana")
            element<Double>("maxMana")
            element<Double>("manaRegen")
            element<Resistances>("resistances")
            element<Loot>("loot")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Entity =
        decoder.decodeStructure(descriptor) {
            var name = ""
            var id = -1
            var health = -1.0
            var maxHealth = -1.0
            var healthRegen = -1.0
            var mana = -1.0
            var maxMana = -1.0
            var manaRegen = -1.0
            var resistances = Resistances()
            var loot = Loot()
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> id = decodeIntElement(descriptor, index)
                    2 -> health = decodeDoubleElement(descriptor, index)
                    3 -> maxHealth = decodeDoubleElement(descriptor, index)
                    4 -> healthRegen = decodeDoubleElement(descriptor, index)
                    5 -> mana = decodeDoubleElement(descriptor, index)
                    6 -> maxMana = decodeDoubleElement(descriptor, index)
                    7 -> manaRegen = decodeDoubleElement(descriptor, index)
                    8 -> resistances =
                        decodeSerializableElement(descriptor, index, Resistances.serializer())

                    9 -> loot = decodeSerializableElement(descriptor, index, Loot.serializer())
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            Entity(
                name = name,
                id = id,
                health = health,
                maxHealth = maxHealth,
                healthRegen = healthRegen,
                mana = mana,
                maxMana = maxMana,
                manaRegen = manaRegen,
                resistances = resistances,
                loot = loot
            )
        }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Entity) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeIntElement(descriptor, 1, value.id)
            encodeDoubleElement(descriptor, 2, value.health)
            encodeDoubleElement(descriptor, 3, value.maxHealth)
            encodeDoubleElement(descriptor, 4, value.healthRegen)
            encodeDoubleElement(descriptor, 5, value.mana)
            encodeDoubleElement(descriptor, 6, value.maxMana)
            encodeDoubleElement(descriptor, 7, value.manaRegen)
            encodeSerializableElement(
                descriptor,
                8,
                Resistances.serializer(),
                value.resistances
            )
            encodeSerializableElement(descriptor, 9, Loot.serializer(), value.loot)
        }
    }
}