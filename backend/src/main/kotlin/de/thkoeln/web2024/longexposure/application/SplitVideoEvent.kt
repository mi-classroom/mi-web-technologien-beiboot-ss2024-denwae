package de.thkoeln.web2024.longexposure.application

data class SplitVideoEvent(
    val type: EventType,
    val maxFrame: Int,
    val currentFrame: Int,
    val frame: ByteArray? = null
)