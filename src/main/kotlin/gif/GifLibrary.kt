package org.laolittle.plugin.gif

import org.laolittle.plugin.DefaultNativeLibFolder
import java.util.concurrent.atomic.AtomicBoolean

public object GifLibrary {
    internal val loaded = AtomicBoolean(false)

    private val defaultGifLibrary = DefaultNativeLibFolder.resolve(System.mapLibraryName("gifski"))

    @Synchronized
    public fun load() {
        if (loaded.compareAndSet(false, true))
            System.load(defaultGifLibrary.absolutePath)
    }
}