@file: Suppress("unused")

package org.laolittle.plugin

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skia.Surface
import org.jetbrains.skiko.toBufferedImage
import java.awt.image.BufferedImage
import net.mamoe.mirai.message.data.Image as MiraiImage

@Deprecated(
    """
    Use Image.getBytes
""", ReplaceWith(
        "Image.getBytes(format: EncodedImageFormat = EncodedImageFormat.PNG)"
    )
)
public fun Surface.getBytes(format: EncodedImageFormat = EncodedImageFormat.PNG): ByteArray {
    makeImageSnapshot().encodeToData(format).use {
        requireNotNull(it) { "Error: Draw Failed" }
        return it.bytes
    }
}

@Deprecated(
    """
    Use Image.toExternalResource
""", ReplaceWith(
        "Image.toExternalResource(format: EncodedImageFormat = EncodedImageFormat.PNG)",
        "net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource"
    )
)

public fun Surface.toExternalResource(format: EncodedImageFormat = EncodedImageFormat.PNG): ExternalResource =
    makeImageSnapshot().toExternalResource(format)

public fun Image.getBytes(format: EncodedImageFormat = EncodedImageFormat.PNG, quality: Int = 100): ByteArray {
    return encodeToData(format, quality)?.use {
        it.bytes
    } ?: throw IllegalStateException("Image encode failed")
}

public val Image.bytes: ByteArray get() = getBytes()

public fun Image.toExternalResource(format: EncodedImageFormat = EncodedImageFormat.PNG): ExternalResource =
    getBytes(format).toExternalResource(format.name.replace("JPEG", "JPG"))

public fun Image.toBufferedImage(): BufferedImage = Bitmap.makeFromImage(this).toBufferedImage()

public suspend inline fun Contact.uploadImage(image: Image): MiraiImage =
    image.toExternalResource().use { uploadImage(it) }

public suspend inline fun <C : Contact> C.sendImage(image: Image): MessageReceipt<C> =
    uploadImage(image).sendTo(this)