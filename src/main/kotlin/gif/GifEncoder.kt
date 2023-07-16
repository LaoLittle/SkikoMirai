package org.laolittle.plugin.gif

/**
 * Gif编码是多线程的，请注意：只有在[collector]被Close后[writer]编码才会停止
 * [writer]未开始写入时，[collector]在发送四张图片后开始等待[writer]
 */
public class GifEncoder private constructor(ptr: RawPointerArray) {
    public val collector: Collector = Collector(ptr[0])
    public val writer: Writer = Writer(ptr[1])

    public operator fun component1(): Collector = collector
    public operator fun component2(): Writer = writer

    public companion object {
        public fun new(setting: GifSetting): GifEncoder =
            GifEncoder(nNewEncoder(setting.width, setting.height, setting.quality, setting.fast, setting.repeat.times))
    }
}

private external fun nNewEncoder(
    width: Int,
    height: Int,
    quality: Byte,
    fast: Boolean,
    repeat: Short
): RawPointerArray