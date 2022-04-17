package org.laolittle.plugin

import org.laolittle.plugin.gif.GifEncoder
import org.laolittle.plugin.gif.GifSetting
import java.io.File
import kotlin.system.measureTimeMillis

suspend fun main() {
    val libFile = File(File("."), System.mapLibraryName("gifski"))

    System.load(libFile.absolutePath)

    val encoder = GifEncoder.new(GifSetting(
        112, 112, 100, true, GifSetting.Repeat.Infinite
    ))

    encoder.writer
    encoder.collector

    measureTimeMillis {
        val (collector, writer) = encoder

        collector.addFrame(File("img0.png").readBytes(), 0, 0.0)
        collector.addFrame(File("img1.png").readBytes(), 1, 1.0)
        collector.close()
        if (writer.canWrite) writer.writeToBytes().also { File("f.gif").writeBytes(it) }
    }.also(::println)
}