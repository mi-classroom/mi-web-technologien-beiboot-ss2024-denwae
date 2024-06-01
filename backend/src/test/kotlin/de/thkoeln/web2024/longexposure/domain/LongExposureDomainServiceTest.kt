package de.thkoeln.web2024.longexposure.domain

import ij.ImagePlus
import ij.ImageStack
import ij.plugin.ZProjector
import ij.process.ColorProcessor
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.time.measureTime

class LongExposureDomainServiceTest {

    @Test
    fun test() {
        val video = LongExposureDomainServiceTest::class.java.getResourceAsStream("/IMG_2942.MOV")
        val videoGrabber = FFmpegFrameGrabber(video)
        val imageConverter = Java2DFrameConverter()
        videoGrabber.start()
        Files.createDirectories(Paths.get("build/img"))
        val imageStack = ImageStack()
        var frame = 0
        println("Extracting frames")
        val durationExtract = measureTime {
            while(true){
                val image = imageConverter.convert(videoGrabber.grabImage()) ?: break
                imageStack.addSlice(ColorProcessor(image))
                frame++
            }
            videoGrabber.stop()
        }
        println("Finished extracting frames in $durationExtract")
        println("Merging images")
        val durationImages = measureTime {
            val projector = ZProjector.run(ImagePlus("longExpose", imageStack), "median")
            ImageIO.write(projector.bufferedImage, "png", File("build/img/merged.png"))
        }
        println("Finished merging images in $durationImages")
    }

    @Test
    fun testBlendImage() {
    }
}