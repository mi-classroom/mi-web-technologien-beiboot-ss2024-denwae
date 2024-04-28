package de.thkoeln.web2024.longexposure.domain

import ij.ImagePlus
import ij.ImageStack
import ij.plugin.ZProjector
import ij.process.ColorProcessor
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.Java2DFrameConverter
import org.springframework.stereotype.Service
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.logging.Logger
import javax.imageio.ImageIO
import kotlin.time.measureTime

@Service
class LongExposureDomainService {

    private val logger: Logger = Logger.getLogger(LongExposureDomainService::class.java.name)

    fun splitVideo(video: ByteArrayInputStream): List<ByteArray> {
        val frameGrabber = FFmpegFrameGrabber(video)
        val images = mutableListOf<ByteArray>()
        logger.info("Extracting frames")
        val durationExtract = measureTime {
            frameGrabber.start()
            for(i in 1..frameGrabber.lengthInVideoFrames) {
                images.add(frameGrabber.grabImage().toByteArray())
            }
            frameGrabber.stop()
        }
        logger.info("Finished extracting frames in $durationExtract")
        return images
    }

    fun blendImages(images: List<ByteArrayInputStream>): ByteArray {
        val image: ByteArray
        logger.info("Merging ${images.size} images")
        val duration = measureTime {
            val imageStack = ImageStack()
            images.forEach {
                imageStack.addSlice(ColorProcessor(ImageIO.read(it)))
            }
            image = ZProjector.run(ImagePlus("longExpose", imageStack), "avg").bufferedImage.toByteArray()
        }
        logger.info("Merged images in $duration")
        return image
    }
}

fun Frame.toByteArray(): ByteArray {
    return Java2DFrameConverter().convert(this).toByteArray()
}

fun BufferedImage.toByteArray(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(this , "png", byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}