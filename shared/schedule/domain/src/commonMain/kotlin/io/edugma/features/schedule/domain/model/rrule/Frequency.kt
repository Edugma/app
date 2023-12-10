package io.edugma.features.schedule.domain.model.rrule

enum class Frequency {
    Daily,
    Weekly,
    Monthly,
    Yearly,
    ;

    override fun toString(): String {
        return super.toString().uppercase()
    }
}
