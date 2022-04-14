package org.laolittle.plugin.gif

public class Collector(_ptr: RawPointer) : GifNative(_ptr) {
    override fun close() {
        if (dropped) error("Already closed")
        nCloseCollector(ptr)
        dropped = true
    }

    public fun addFrame(bytes: ByteArray, frameIndex: Int, presentation: Double) {
        if (dropped) error("Already closed")
        ptr = nCollectorAddFrameBytes(bytes, frameIndex, presentation, ptr)
    }

    public fun addFrame(path: String, frameIndex: Int, presentation: Double) {
        if (dropped) error("Already closed")
        ptr = nCollectorAddPngFile(path, frameIndex, presentation, ptr)
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