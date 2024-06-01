package de.thkoeln.web2024.longexposure.application

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

class SplitVideoEventEmitter(private val emitter: SseEmitter) {

    fun emit(event: SplitVideoEvent) {
        emitter.send(SseEmitter.event()
            .name(event.type.name)
            .data(event)
            .build()
        )
    }
}
