package org.laolittle.plugin.gif

import java.io.Closeable

internal typealias RawPointer = Long
internal typealias RawPointerArray = LongArray

internal val nativeNullPtr: RawPointer get() = 0L

public open class GifNative internal constructor(_ptr: RawPointer) : Closeable {
    public var dropped: Boolean = false
        protected set

    public var ptr: RawPointer = _ptr
        protected set

    override fun close(): Unit = throw IllegalStateException("Managed by native")

    init {
        if (!GifLibrary.loaded) GifLibrary.load()
        require(_ptr != nativeNullPtr) { "Can't wrap nullptr, which is supposed to use by ${this::class.simpleName}" }
    }
}