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
class LongExposureDomainService(
    val longExposureSettings: LongExposureSettings
) {

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
                Files.createDirectories(Paths.get("./images/$project/batched"))
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
        logger.info("Merging ${images.size} images")
        val projectPath = "./images/$project"
        val imagePath = "$projectPath/blended.png"
        val duration = measureTime {
            val batchedPath = "$projectPath/batched"
            var batch = 1
            images.chunked(longExposureSettings.blendBatchSize) {
                logger.info("Blending batch $batch")
                blend("$batchedPath/blended$batch.png", projectPath, it.map { image -> "$image.png" })
                batch++
            }
            logger.info("Blending final image")
            blend("$projectPath/blended.png", batchedPath, getBlendedImagesPaths(batchedPath))
        }
        logger.info("Merged images in $duration")
        return imagePath
    }

    private fun getBlendedImagesPaths(imagePath: String): List<String> {
        return File(imagePath).listFiles()?.map { it.name } ?: throw RuntimeException("No directory with this path")
    }

    private fun blend(imagePath: String, projectPath: String, images: List<String>){
        val imageStack = ImageStack()
        images.forEach {
            imageStack.addSlice(ColorProcessor(ImageIO.read(File("$projectPath/$it"))))
        }
        ImageIO.write(
            ZProjector.run(ImagePlus("longExpose", imageStack), "avg").bufferedImage,
            "png",
            File(imagePath)
        )
    }
}

fun BufferedImage.toByteArray(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(this , "png", byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}
