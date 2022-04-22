package org.laolittle.plugin.gif

public class GifSetting public constructor(
    public val width: Int,
    public val height: Int,
    public val quality: Byte,
    public val fast: Boolean,
    public val repeat: Repeat
) {
    public sealed class Repeat private constructor(public val times: Short) {
        public object Infinite : Repeat(-1)

        public class Finite(times: Short) : Repeat(times)
    }

    init {
        require(quality in 1..100) { "quality must be within 1 to 100" }
    }

    public companion object {
        public fun default(): GifSetting = GifSetting(0, 0, 100, false, Repeat.Infinite)
    }
}