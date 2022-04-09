package org.laolittle.plugin.gif

import java.util.ArrayList

public class GifImage(private val images: Array<ByteArray>,public val bytes: ByteArray) {
    public val frameCount: Int get() = images.size

    public val frames: Array<ByteArray> get() = images
}

public class GifImageBuilder(setting: GifSetting = GifSetting.default()) {
    private var frameIndex = 0

    public val encoder: GifEncoder = GifEncoder.new(setting)

    private val images: ArrayList<Pair<ByteArray, Double>> = arrayListOf()

    public fun addFrame(image: ByteArray, delay: Double = 1.0): Boolean = images.add(image to delay)

    public suspend fun build(): GifImage {
        val (collector, writer) = encoder
        for (image in images) {
            collector.addFrame(image.first, frameIndex++, image.second)
        }
        collector.close()

        return GifImage(images.map { it.first }.toTypedArray(), writer.writeToBytes())
    }
}

public suspend fun buildGifImage(setting: GifSetting, block: GifImageBuilder.() -> Unit): GifImage =
    GifImageBuilder(setting).apply(block).build()