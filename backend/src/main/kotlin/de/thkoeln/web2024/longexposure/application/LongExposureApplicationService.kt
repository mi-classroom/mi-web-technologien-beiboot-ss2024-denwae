package de.thkoeln.web2024.longexposure.application

import de.thkoeln.web2024.longexposure.domain.LongExposureDomainService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.ByteArrayInputStream

@Service
class LongExposureApplicationService(
    private val longExposureDomainService: LongExposureDomainService
) {

    fun splitVideo(file: MultipartFile, emitter: SseEmitter) {
        longExposureDomainService.splitVideo(ByteArrayInputStream(file.bytes), SplitVideoEventEmitter(emitter))
    }

    fun blendImages(files: List<MultipartFile>): ByteArray {
        return longExposureDomainService.blendImages(files.map { ByteArrayInputStream(it.bytes) })
    }

}