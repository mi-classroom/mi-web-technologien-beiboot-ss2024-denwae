package de.thkoeln.web2024.longexposure.application

import de.thkoeln.web2024.longexposure.domain.LongExposureDomainService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class LongExposureApplicationService(
    private val longExposureDomainService: LongExposureDomainService
) {

    fun splitVideo(file: MultipartFile, project: UUID, downSample: Boolean, emitter: SseEmitter) {
        longExposureDomainService.splitVideo(ByteArrayInputStream(file.bytes), downSample, SplitVideoEventEmitter(emitter), project)
    }

    fun blendImages(project: UUID, images: List<Int>): String {
        return longExposureDomainService.blendImages(project, images)
    }

    fun getImage(project: String, img: String): ByteArray {
        return Files.readAllBytes(Paths.get("./images/$project/$img"))
    }

}