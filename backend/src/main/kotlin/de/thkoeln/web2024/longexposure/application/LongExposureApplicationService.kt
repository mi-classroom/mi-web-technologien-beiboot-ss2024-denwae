package de.thkoeln.web2024.longexposure.application

import de.thkoeln.web2024.longexposure.domain.LongExposureDomainService
import de.thkoeln.web2024.longexposure.domain.toByteArray
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO
import kotlin.io.path.Path

@Service
class LongExposureApplicationService(
    private val longExposureDomainService: LongExposureDomainService
) {

    fun splitVideo(file: MultipartFile, project: UUID, emitter: SseEmitter) {
        longExposureDomainService.splitVideo(ByteArrayInputStream(file.bytes), SplitVideoEventEmitter(emitter), project)
    }

    fun blendImages(project: UUID, images: List<Int>): String {
        return longExposureDomainService.blendImages(project, images)
    }

    fun getImage(project: String, img: String): ByteArray {
        return Files.readAllBytes(Paths.get("./images/$project/$img"))
    }

}