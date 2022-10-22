package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.items.Weapon
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*

object WeaponSerializer : KSerializer<Weapon> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Weapon") {

        }

    override fun deserialize(decoder: Decoder): Weapon =
        decoder.decodeStructure(descriptor) {
            var item = Item()
            var damage = Damage()
            var typeOfWeapon = -1
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> item = decodeSerializableElement(descriptor, index, Item.serializer())
                    1 -> damage = decodeSerializableElement(descriptor, index, Damage.serializer())
                    2 -> typeOfWeapon = decodeIntElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index $index")
                }
            }
            Weapon(item = item, damage = damage, typeOfWeapon = typeOfWeapon)
        }

    override fun serialize(encoder: Encoder, value: Weapon) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(
                descriptor,
                0,
                Item.serializer(),
                Item(value)
            )
            encodeSerializableElement(descriptor, 1, Damage.serializer(), value.damage)
            encodeIntElement(descriptor, 2, value.typeOfWeapon)
        }
    }
}