package de.thkoeln.web2024.longexposure.application

import de.thkoeln.web2024.longexposure.domain.LongExposureDomainService
import net.coobird.thumbnailator.Thumbnails
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO

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

    fun getThumb(project: String, img: String): ByteArray {
        return Thumbnails.of("./images/$project/$img").size(300, 300).asBufferedImage().toByteArray()
    }
}

fun BufferedImage.toByteArray(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(this , "png", byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}