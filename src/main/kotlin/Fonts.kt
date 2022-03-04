package org.laolittle.plugin

import org.jetbrains.skia.Font
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.makeFromFile

object Fonts {
    private val fonts by FontConfig::fonts

    val fontFolder = SkikoMirai.dataFolder.resolve("Fonts").also { it.mkdirs() }

    operator fun get(fontName: String, size: Float = 100F): Font {
        val fileName = fonts.getOrPut(fontName) {
            fontFolder.listFiles()?.find { fontName.uppercase() in it.name.uppercase() }?.name ?: ""
        }
        return if (fileName == "") {
            val style = when (fontName.split("-").last().uppercase()) {
                "BOLD" -> FontStyle.BOLD
                "ITALIC" -> FontStyle.ITALIC
                "BOLD_ITALIC" -> FontStyle.BOLD_ITALIC
                else -> FontStyle.NORMAL
            }
            Font(
                Typeface.makeFromName(
                    fontName.replace(Regex("""-(?i)(?:BOLD|ITALIC|BOLD_ITALIC|NORMAL)$"""), ""),
                    style
                ), size
            )
        } else {
            val file = fontFolder.resolve(fileName)
            require(file.isFile) { "无法找到字体: $fontName" }
            Font(Typeface.makeFromFile(file.path), size)
        }
    }
}