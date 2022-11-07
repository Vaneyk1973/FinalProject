package com.example.finalproject.service.serializers

import com.example.finalproject.service.classes.Damage
import com.example.finalproject.service.classes.Inventory
import com.example.finalproject.service.classes.User
import com.example.finalproject.service.classes.entities.Entity
import com.example.finalproject.service.classes.entities.Player
import com.example.finalproject.service.classes.items.Item
import com.example.finalproject.service.classes.spell.Spell
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure


object PlayerSerializer : KSerializer<Player> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Player") {
            element<Entity>("entity")
            element<Inventory>("inventory")
            element<Damage>("damage")
            element<ArrayList<Item>>("equipment")
            element<Int>("level")
            element<Int>("experience")
            element<Int>("experienceToTheNextLevelRequired")
            element<User>("user")
            element<Int>("mapNumber")
            element<ArrayList<Pair<Int, Int>>>("coordinates")
            element<ArrayList<Spell>>("spells")
            element<Int>("researchPoints")
            element<Boolean>("chatMode")
            element<Int>("gold")
        }

    /**
     * @return the value of a needed class according to a descriptor
     * deserializes the value
     */
    override fun deserialize(decoder: Decoder): Player = decoder.decodeStructure(descriptor) {
        var entity = Entity()
        var inventory = Inventory()
        var damage = Damage()
        var equipment = ArrayList<Item>()
        var level = 0
        var experience = 0
        var experienceToTheNextLevelRequired = 0
        var user = User()
        var mapNumber = 0
        var coordinates = ArrayList<Pair<Int, Int>>()
        var spells = ArrayList<Spell>()
        var researchPoints = 0
        var chatMode = false
        var gold = 0
        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> entity = decodeSerializableElement(descriptor, index, Entity.serializer())
                1 -> inventory =
                    decodeSerializableElement(descriptor, index, Inventory.serializer())

                2 -> damage = decodeSerializableElement(descriptor, index, Damage.serializer())
                3 -> equipment = decodeSerializableElement(
                    descriptor,
                    index,
                    ListSerializer(Item.serializer())
                ) as ArrayList<Item>

                4 -> level = decodeIntElement(descriptor, index)
                5 -> experience = decodeIntElement(descriptor, index)
                6 -> experienceToTheNextLevelRequired = decodeIntElement(descriptor, index)
                7 -> user = decodeSerializableElement(descriptor, index, User.serializer())
                8 -> mapNumber = decodeIntElement(descriptor, index)
                9 -> coordinates = decodeSerializableElement(
                    descriptor, index, ListSerializer(
                        PairSerializer(Int.serializer(), Int.serializer())
                    )
                ) as ArrayList<Pair<Int, Int>>

                10 -> spells = decodeSerializableElement(
                    descriptor,
                    index,
                    ListSerializer(Spell.serializer())
                ) as ArrayList<Spell>

                11 -> researchPoints = decodeIntElement(descriptor, index)
                12 -> chatMode = decodeBooleanElement(descriptor, index)
                13 -> gold = decodeIntElement(descriptor, index)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }
        Player(
            entity = entity,
            inventory = inventory,
            damage = damage,
            equipment = equipment,
            level = level,
            experience = experience,
            experienceToTheNextLevelRequired = experienceToTheNextLevelRequired,
            user = user,
            mapNumber = mapNumber,
            coordinates = coordinates,
            spells = spells,
            researchPoints = researchPoints,
            chatMode = chatMode,
            gold = gold
        )
    }

    /**
     * serializes the value
     */
    override fun serialize(encoder: Encoder, value: Player) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, Entity.serializer(), Entity(value))
            encodeSerializableElement(descriptor, 1, Inventory.serializer(), value.inventory)
            encodeSerializableElement(descriptor, 2, Damage.serializer(), value.damage)
            encodeSerializableElement(
                descriptor,
                3,
                ListSerializer(Item.serializer()),
                value.equipment
            )
            encodeIntElement(descriptor, 4, value.level)
            encodeIntElement(descriptor, 5, value.experience)
            encodeIntElement(descriptor, 6, value.experienceToTheNextLevelRequired)
            encodeSerializableElement(descriptor, 7, User.serializer(), value.user)
            encodeIntElement(descriptor, 8, value.mapNumber)
            encodeSerializableElement(
                descriptor,
                9,
                ListSerializer(PairSerializer(Int.serializer(), Int.serializer())),
                value.coordinates
            )
            encodeSerializableElement(
                descriptor, 10, ListSerializer(Spell.serializer()), value.spells
            )
            encodeIntElement(descriptor, 11, value.researchPoints)
            encodeBooleanElement(descriptor, 12, value.chatMode)
            encodeIntElement(descriptor, 13, value.gold)
        }
    }
}