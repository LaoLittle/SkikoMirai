package org.laolittle.plugin.gif

public class GifEncoder private constructor(ptr: LongArray) {
    public val collector: Collector = Collector(ptr[0])
    public val writer: Writer = Writer(ptr[1])

    public operator fun component1(): Collector = collector
    public operator fun component2(): Writer = writer

    public companion object {
        public fun new(setting: GifSetting): GifEncoder =
            GifEncoder(nNewEncoder(setting.width, setting.height, setting.quality, setting.fast, setting.repeat.times))

        init {
            if (!GifLibrary.loaded) GifLibrary.load()
        }
    }
}

private external fun nNewEncoder(
    width: Int,
    height: Int,
    quality: Byte,
    fast: Boolean,
    repeat: Short
): LongArray