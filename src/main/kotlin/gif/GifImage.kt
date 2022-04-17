package org.laolittle.plugin.gif

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

public class GifImage internal constructor(private val images: Array<ByteArray>, public val bytes: ByteArray) {
    public val frameCount: Int get() = images.size

    public val frames: Array<ByteArray> get() = images
}

public class GifImageBuilder(private val setting: GifSetting = GifSetting.default()) {
    private var frameIndex = 0

    private val images: ArrayList<Pair<ByteArray, Double>> = arrayListOf()

    public fun addFrame(image: ByteArray, delay: Double = 1.0): Boolean = images.add(image to delay)

    public suspend fun build(): GifImage {
        return coroutineScope {
            val (collector, writer) = GifEncoder.new(setting)
            val result = async(Dispatchers.IO) {
                writer.writeToBytes()
            }

            collector.use {
                for (image in images) {
                    it.addFrame(image.first, frameIndex++, image.second * frameIndex)
                }
            }

            GifImage(images.map { it.first }.toTypedArray(), result.await())
        }
    }

    init {
        if (!GifLibrary.loaded) GifLibrary.load()
    }
}

public suspend fun buildGifImage(setting: GifSetting, block: GifImageBuilder.() -> Unit): GifImage =
    GifImageBuilder(setting).apply(block).build()