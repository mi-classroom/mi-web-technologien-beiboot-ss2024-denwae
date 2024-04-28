package de.thkoeln.web2024.longexposure.application

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class LongExposureController(
    private val applicationService: LongExposureApplicationService
) {

    @PostMapping("/split-video")
    fun splitVideo(@RequestParam("file") file: MultipartFile): List<ByteArray> {
        return applicationService.splitVideo(file)
    }

    @PostMapping("/blend-images", produces = [MediaType.IMAGE_PNG_VALUE])
    fun blendImages(@RequestParam("files") file: List<MultipartFile>): ByteArray {
        return applicationService.blendImages(file)
    }

    @PostMapping("/long-expose", produces = [MediaType.IMAGE_PNG_VALUE])
    fun longExpose(@RequestParam("file") file: MultipartFile): ByteArray {
        return applicationService.longExpose(file)
    }
}