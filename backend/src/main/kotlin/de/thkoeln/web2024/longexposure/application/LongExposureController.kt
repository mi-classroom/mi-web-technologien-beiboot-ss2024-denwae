package de.thkoeln.web2024.longexposure.application

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class LongExposureController(
    private val applicationService: LongExposureApplicationService
) {

    @PostMapping("/split-video", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun splitVideo(@RequestParam("file") file: MultipartFile): SseEmitter {
        return SseEmitter().apply {
            val emitter = this
            GlobalScope.async { applicationService.splitVideo(file, emitter) }
        }
    }

    @PostMapping("/blend-images", produces = [MediaType.IMAGE_PNG_VALUE])
    fun blendImages(@RequestParam("files") file: List<MultipartFile>): ByteArray {
        return applicationService.blendImages(file)
    }

}