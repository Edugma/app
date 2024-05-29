package com.edugma.features.schedule.domain.model.rrule

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object RRuleSerializer : KSerializer<RRule> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("RRule", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): RRule {
        return RRuleFormatter(decoder.decodeString()).toRRule()
    }

    override fun serialize(encoder: Encoder, value: RRule) {
        encoder.encodeString(RRuleFormatter(value).toRFC5545String())
    }
}

object RRuleListSerializer : KSerializer<List<RRule>> by ListSerializer(RRuleSerializer)
