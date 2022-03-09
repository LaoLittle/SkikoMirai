@file: Suppress("unused")

package org.laolittle.plugin

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skia.Surface
import org.jetbrains.skiko.toBufferedImage

@Deprecated(
    """
    Use Image.getBytes
""", ReplaceWith(
        "Image.getBytes(format: EncodedImageFormat = EncodedImageFormat.PNG)"
    )
)
fun Surface.getBytes(format: EncodedImageFormat = EncodedImageFormat.PNG): ByteArray {
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

fun Surface.toExternalResource(format: EncodedImageFormat = EncodedImageFormat.PNG) =
    makeImageSnapshot().toExternalResource(format)

fun Image.getBytes(format: EncodedImageFormat = EncodedImageFormat.PNG, quality: Int = 100): ByteArray {
    return encodeToData(format, quality)?.use {
        it.bytes
    } ?: throw IllegalStateException("Image encode failed")
}

val Image.bytes get() = getBytes()

fun Image.toExternalResource(format: EncodedImageFormat = EncodedImageFormat.PNG) =
    getBytes(format).toExternalResource(format.name.replace("JPEG", "JPG"))

fun Image.toBufferedImage() = Bitmap.makeFromImage(this).toBufferedImage()

suspend inline fun Contact.uploadImage(image: Image) =
    image.toExternalResource().use { uploadImage(it) }

suspend inline fun <C : Contact> C.sendImage(image: Image): MessageReceipt<C> =
    uploadImage(image).sendTo(this)