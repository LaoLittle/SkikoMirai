package org.laolittle.plugin

import org.jetbrains.skiko.hostId
import java.io.File
import java.security.MessageDigest

internal const val SKIKO_LIBRARY_PATH_PROPERTY = "skiko.library.path"

internal val SkikoLibFile = DefaultNativeLibFolder.resolve(System.mapLibraryName("skiko-$hostId"))

private fun ByteArray.toHexString() = buildString {
    this@toHexString.forEach {
        val hex = it.toInt() and 0xFF
        val hexStr = Integer.toHexString(hex)

        if (hexStr.length == 1) {
            append("0$hexStr")
        } else {
            append(hexStr)
        }
    }
}

internal val File.sha256: String
    get() {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(readBytes()).toHexString()
    }