package org.laolittle.plugin

import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Surface

fun Surface.getBytes(format: EncodedImageFormat = EncodedImageFormat.PNG): ByteArray {
    val resource = makeImageSnapshot().encodeToData(format)
    requireNotNull(resource) { "Error: Draw Failed" }
    return resource.bytes
}

fun Surface.toExternalResource(format: EncodedImageFormat = EncodedImageFormat.PNG) =
    getBytes(format).toExternalResource(format.name.lowercase())