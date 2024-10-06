package com.edugma.features.account.domain.model.menu

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@kotlinx.serialization.Serializable(with = CardTypeSerializer::class)
enum class CardType {
    Profile,
    Students,
    Teachers,
    Classmates,
    Payments,
    Marks,
    Web
}

internal object CardTypeSerializer : KSerializer<CardType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CardType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): CardType {
        return decoder.decodeString().let { CardType.valueOf(it) }
    }

    override fun serialize(encoder: Encoder, value: CardType) {
        encoder.encodeString(value.name)
    }
}
