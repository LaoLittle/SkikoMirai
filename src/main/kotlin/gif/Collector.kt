package org.laolittle.plugin.gif

/**
 * 图片收集器
 */
public class Collector internal constructor(ptr: RawPointer) : GifNative(ptr) {
    public fun addFrame(bytes: ByteArray, frameIndex: Int, presentation: Double) {
        if (dropped) error("Already closed")
        ptr = nCollectorAddFrameBytes(bytes, frameIndex, presentation, ptr)
    }

    public fun addFrame(path: String, frameIndex: Int, presentation: Double) {
        if (dropped) error("Already closed")
        ptr = nCollectorAddPngFile(path, frameIndex, presentation, ptr)
    }

    /**
     * 释放本[Collector]
     */
    override fun close() {
        if (dropped) error("Already closed")
        nCloseCollector(ptr)
        dropped = true
    }
}

private external fun nCollectorAddFrameBytes(
    bytes: ByteArray,
    frameIndex: Int,
    presentation: Double,
    collector: RawPointer
): RawPointer

private external fun nCollectorAddPngFile(
    path: String,
    frameIndex: Int,
    presentation: Double,
    collector: RawPointer
): RawPointer

private external fun nCloseCollector(collector: RawPointer)