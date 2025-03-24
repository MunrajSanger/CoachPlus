package uk.ac.aber.dcs.cs31620.coachplus.model

enum class EventType(val displayName: String) {
    TRAINING("Training"),
    MATCH("Match"),
    EVENT("Event"),
    TOURNAMENT("Tournament");

    companion object {
        fun fromString(value: String): EventType {
            return values().find { it.name.equals(value, ignoreCase = true) } ?: TRAINING
        }
    }
}
