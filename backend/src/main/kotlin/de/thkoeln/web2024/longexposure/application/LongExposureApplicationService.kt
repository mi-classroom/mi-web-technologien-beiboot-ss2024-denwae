package de.thkoeln.web2024.longexposure.application

import de.thkoeln.web2024.longexposure.domain.LongExposureDomainService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream

@Service
class LongExposureApplicationService(
    private val longExposureDomainService: LongExposureDomainService
) {

    fun splitVideo(file: MultipartFile): List<ByteArray> {
        return longExposureDomainService.splitVideo(ByteArrayInputStream(file.bytes))
    }

    fun blendImages(files: List<MultipartFile>): ByteArray {
        return longExposureDomainService.blendImages(files.map { ByteArrayInputStream(it.bytes) })
    }

    fun longExpose(file: MultipartFile): ByteArray {
        return longExposureDomainService.blendImages(
            longExposureDomainService.splitVideo(ByteArrayInputStream(file.bytes))
                .map { ByteArrayInputStream(it) }
        )
    }
}