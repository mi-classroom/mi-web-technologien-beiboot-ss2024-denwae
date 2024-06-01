package de.thkoeln.web2024.longexposure.domain

import de.thkoeln.web2024.longexposure.application.EventType
import de.thkoeln.web2024.longexposure.application.SplitVideoEvent
import de.thkoeln.web2024.longexposure.application.SplitVideoEventEmitter
import ij.ImagePlus
import ij.ImageStack
import ij.plugin.ZProjector
import ij.process.ColorProcessor
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.logging.Logger
import javax.imageio.ImageIO
import kotlin.time.measureTime


@Service
class LongExposureDomainService {

    private val logger: Logger = Logger.getLogger(LongExposureDomainService::class.java.name)

    fun splitVideo(video: ByteArrayInputStream, eventEmitter: SplitVideoEventEmitter, project: UUID) {
        val frameGrabber = FFmpegFrameGrabber(video)
        logger.info("Extracting frames")
        val durationExtract = measureTime {
            frameGrabber.start()
            val maxFrames = frameGrabber.lengthInVideoFrames
            eventEmitter.emit(SplitVideoEvent(EventType.STARTED, maxFrames, -1))

            for(i in 1..frameGrabber.lengthInVideoFrames) {
                logger.info("Extracting frame $i")
                Files.createDirectories(Paths.get("./images/$project"))
                val imagePath = "./images/$project/$i.png"

                ImageIO.write(
                    Java2DFrameConverter().convert(frameGrabber.grabImage()) ,
                    "png",
                    File(imagePath)
                )
                eventEmitter.emit(SplitVideoEvent(EventType.SPLIT, maxFrames, i, imagePath))
            }
            frameGrabber.stop()
            eventEmitter.emit(SplitVideoEvent(EventType.FINISHED, maxFrames, -1))
        }
        logger.info("Finished extracting frames in $durationExtract")
    }

    fun blendImages(project: UUID, images: List<Int>): String {
        val image: ByteArray
        logger.info("Merging ${images.size} images")
        val imagePath = "./images/$project/blended.png"
        val duration = measureTime {
            val imageStack = ImageStack()
            images.forEach {
                imageStack.addSlice(ColorProcessor(ImageIO.read(File("./images/$project/$it.png"))))
            }
            ImageIO.write(
                ZProjector.run(ImagePlus("longExpose", imageStack), "avg").bufferedImage,
                "png",
                File(imagePath)
            )
        }
        logger.info("Merged images in $duration")
        return imagePath
    }
}

fun BufferedImage.toByteArray(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(this , "png", byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}
