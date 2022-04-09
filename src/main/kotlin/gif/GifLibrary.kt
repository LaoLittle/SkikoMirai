package org.laolittle.plugin.gif

import org.laolittle.plugin.DefaultNativeLibFolder

public object GifLibrary {
    internal var loaded = false
    private set
    private val defaultGifLibrary = DefaultNativeLibFolder.resolve(System.mapLibraryName("gifski"))

    public fun load() {
        loaded = true

        System.load(defaultGifLibrary.absolutePath)
    }
}