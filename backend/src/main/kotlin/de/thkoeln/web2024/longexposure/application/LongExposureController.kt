package de.thkoeln.web2024.longexposure.application

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.UUID

@RestController
class LongExposureController(
    private val applicationService: LongExposureApplicationService
) {

    private val emitters = mutableMapOf<UUID, SseEmitter>()

    @PostMapping("/split-video/events", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun registerSplitVideoEventEmitter(@RequestParam(required = false) id: UUID?): SseEmitter {
        return SseEmitter(0L).apply {
            println("registerSplitVideoEventEmitter")
            val emitterId = UUID.randomUUID()
            emitters[emitterId] = this
            this.send(
                SseEmitter.event()
                    .name("registered")
                    .id("registered")
                    .data(emitterId.toString())
                    .build()
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @PostMapping("/split-video")
    fun splitVideo(@RequestParam("file") file: MultipartFile, @RequestParam emitterId: UUID): ResponseEntity<UUID> {
        val projectId = UUID.randomUUID()
        GlobalScope.async {
            applicationService.splitVideo(
                file,
                projectId,
                emitters[emitterId] ?: throw RuntimeException("Emitter not found")
            )
        }
        return ok(projectId)
    }

    @PostMapping("/blend-images")
    fun blendImages(@RequestBody images: BlendImageDTO): String {
        return applicationService.blendImages(images.project, images.images)
    }

    @GetMapping("/images/{project}/{img}", produces = ["image/webp"])
    fun getImage(@PathVariable project: String, @PathVariable img: String): ResponseEntity<ByteArray> {
        return ok(applicationService.getImage(project, img))
    }


}