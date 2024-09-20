package de.thkoeln.web2024.longexposure.application

import jakarta.servlet.http.HttpServletResponse
import jakarta.websocket.server.PathParam
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
import java.util.logging.Logger

@RestController
class LongExposureController(
    private val applicationService: LongExposureApplicationService
) {
    companion object {
        val LOGGER: Logger = Logger.getLogger(LongExposureController::class.java.name)
    }

    private val emitters = mutableMapOf<UUID, SseEmitter>()

    @PostMapping("/split-video/events/{emitterId}/ping", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun ssePing(
        @PathVariable emitterId: UUID,
        response: HttpServletResponse
    ) {
        response.setHeader("X-Accel-Buffering", "no")
        LOGGER.info("Received ping for emitter with id $emitterId")
        emitters[emitterId]?.send(
            SseEmitter.event()
                .name("ping")
                .id("ping")
                .data("Received")
                .build()
        ) ?: throw RuntimeException("No Emitter with this id")
    }

    @PostMapping("/split-video/events", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun registerSplitVideoEventEmitter(
        @RequestParam(required = false) id: UUID?,
        response: HttpServletResponse
    ): SseEmitter {
        response.setHeader("X-Accel-Buffering", "no")
        return SseEmitter(60000L).apply {
            val emitterId = UUID.randomUUID()
            LOGGER.info("Registered SSE Emitter with id: $emitterId")
            emitters[emitterId] = this
            this.onCompletion{
                emitters.remove(emitterId)
                LOGGER.info("Removing SSE Emitter with id $emitterId")
            }
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
    fun splitVideo(@RequestParam("file") file: MultipartFile, @RequestParam emitterId: UUID, @RequestParam downSample: Boolean): ResponseEntity<UUID> {
        val projectId = UUID.randomUUID()
        GlobalScope.async {
            applicationService.splitVideo(
                file,
                projectId,
                downSample,
                emitters[emitterId] ?: throw RuntimeException("Emitter not found")
            )
        }
        return ok(projectId)
    }

    @PostMapping("/blend-images")
    fun blendImages(@RequestBody images: BlendImageDTO): String {
        return applicationService.blendImages(images.project, images.images)
    }

    @GetMapping("/images/{project}/{img}", produces = ["image/png"])
    fun getImage(@PathVariable project: String, @PathVariable img: String): ResponseEntity<ByteArray> {
        return ok(applicationService.getImage(project, img))
    }
}