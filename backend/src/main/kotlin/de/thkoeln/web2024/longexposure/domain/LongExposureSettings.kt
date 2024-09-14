package de.thkoeln.web2024.longexposure.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "long-expose")
data class LongExposureSettings @ConstructorBinding constructor(
    val blendBatchSize: Int
)
