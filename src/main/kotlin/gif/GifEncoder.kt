package org.laolittle.plugin.gif

public class GifEncoder(ptr: LongArray) {

    public val collector: Collector = Collector(ptr[0])
    public val writer: Writer = Writer(ptr[1])

    public operator fun component1(): Collector = collector
    public operator fun component2(): Writer = writer

    public suspend fun encodeToByteArray(): ByteArray = writer.writeToBytes()

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
): LongArray