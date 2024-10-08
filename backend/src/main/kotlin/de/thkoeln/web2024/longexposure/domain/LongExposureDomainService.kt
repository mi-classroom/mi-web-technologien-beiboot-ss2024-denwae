package de.thkoeln.web2024.longexposure.domain

import de.thkoeln.web2024.longexposure.application.EventType
import de.thkoeln.web2024.longexposure.application.SplitVideoEvent
import de.thkoeln.web2024.longexposure.application.SplitVideoEventEmitter
import ij.ImagePlus
import ij.ImageStack
import ij.plugin.ZProjector
import ij.process.ColorProcessor
import org.bytedeco.javacpp.Loader
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
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
    private val ffmpeg: String = Loader.load(org.bytedeco.ffmpeg.ffmpeg::class.java)


    fun splitVideo(video: ByteArrayInputStream, downSample: Boolean, eventEmitter: SplitVideoEventEmitter, project: UUID) {
        val projectPath = "./images/$project"
        Files.createDirectories(Paths.get(projectPath))
        val convertedVideoPath = "$projectPath/convertedVideo.mp4"
        convertVideo(video, "$projectPath/originalVideo", convertedVideoPath, downSample)
        val convertedVideo = ByteArrayInputStream(File(convertedVideoPath).readBytes())
        val frameGrabber = FFmpegFrameGrabber(convertedVideo)
        logger.info("Extracting frames")
        val converter = Java2DFrameConverter()
        val durationExtract = measureTime {
            frameGrabber.start()
            val maxFrames = frameGrabber.lengthInVideoFrames
            eventEmitter.emit(SplitVideoEvent(EventType.STARTED, maxFrames, -1))
            for(i in 1..<maxFrames) {
                logger.info("Extracting frame $i")
                Files.createDirectories(Paths.get(projectPath))
                val imagePath = "$projectPath/$i.png"

                ImageIO.write(
                    converter.convert(frameGrabber.grabImage()) ,
                    "png",
                    File(imagePath)
                )
                eventEmitter.emit(SplitVideoEvent(EventType.SPLIT, maxFrames, i, "./thumb/$project/$i.png"))
            }
            frameGrabber.stop()
            eventEmitter.emit(SplitVideoEvent(EventType.FINISHED, maxFrames, -1))
        }
        logger.info("Finished extracting frames in $durationExtract")
    }

    private fun convertVideo(video: ByteArrayInputStream, originalVideoPath: String, convertedVideoPath: String, downSample: Boolean) {
        val videoFile = File(originalVideoPath)
        videoFile.writeBytes(video.readBytes())
        val pb = if (downSample) {
            logger.info("Converting video with reduced resolution")
            ProcessBuilder(ffmpeg, "-i", originalVideoPath, "-filter:v", "scale=1280:-1", "-vcodec", "h264", convertedVideoPath)
        }else {
            logger.info("Converting video with full resolution")
            ProcessBuilder(ffmpeg, "-i", originalVideoPath, "-vcodec", "h264", convertedVideoPath)
        }
        pb.inheritIO().start().waitFor()
    }

    fun blendImages(project: UUID, images: List<Int>): String {
        logger.info("Merging ${images.size} images")
        val projectPath = "./images/$project"
        val imagePath = "$projectPath/blended-${UUID.randomUUID()}.png"
        val duration = measureTime {
            val batchedPath = "$projectPath/batched"
            deleteFolderContent(batchedPath)
            Files.createDirectories(Paths.get(batchedPath))
            var batch = 1
            images.chunked(longExposureSettings.blendBatchSize) {
                logger.info("Blending batch $batch")
                blend("$batchedPath/blended-$batch.png", projectPath, it.map { image -> "$image.png" })
                batch++
            }
            logger.info("Blending final image")
            blend(imagePath, batchedPath, getBlendedImagesPaths(batchedPath))
        }
        logger.info("Merged images in $duration")
        return imagePath
    }

    private fun getBlendedImagesPaths(imagePath: String): List<String> {
        return File(imagePath).listFiles()?.map { it.name } ?: throw RuntimeException("No directory with this path")
    }

    private fun deleteFolderContent(path: String) {
        val file = File(path)
            file.listFiles()?.forEach {
            if (it.isDirectory) {
                deleteFolderContent(it.path)
            } else {
                it.delete()
            }
        }
        file.delete()
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
