package org.laolittle.plugin.gif

import org.laolittle.plugin.DefaultNativeLibFolder
import java.util.concurrent.atomic.AtomicBoolean

public object GifLibrary {
    private val loaded = AtomicBoolean(false)

    private val defaultGifLibrary = DefaultNativeLibFolder.resolve(System.mapLibraryName("gifski"))

    public var gifSupported: Boolean = true
        private set

    @Synchronized
    public fun load() {
        if (!gifSupported) return

        if (loaded.compareAndSet(false, true))
            runCatching {
                System.load(defaultGifLibrary.absolutePath)
            }.onFailure {
                gifSupported = false
            }.getOrThrow()
    }
}