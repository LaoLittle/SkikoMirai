package org.laolittle.plugin

import org.jetbrains.skia.Font
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.makeFromFile
import java.io.File

internal val typeFaces = mutableMapOf<String, Typeface>()

public object Fonts {
    public operator fun get(fontName: String, size: Float = 100F): Font {
        val fileName = fontFolder.listFiles()?.find { fontName.uppercase() in it.name.uppercase() }?.name ?: ""

        return try {
            if (fileName == "") {
                val style = when (fontName.split("-").last().uppercase()) {
                    "BOLD" -> FontStyle.BOLD
                    "ITALIC" -> FontStyle.ITALIC
                    "BOLD_ITALIC" -> FontStyle.BOLD_ITALIC
                    else -> FontStyle.NORMAL
                }
                Font(
                    typeFaces.getOrPut(fontName) {
                        Typeface.makeFromName(
                            fontName.replace(Regex("""-(?i)(?:BOLD|ITALIC|BOLD_ITALIC|NORMAL)$"""), ""),
                            style
                        )
                    }, size
                )
            } else {
                val file = fontFolder.resolve(fileName)
                require(file.isFile) { "无法找到字体: $fontName" }
                Font(typeFaces.getOrPut(fontName) { Typeface.makeFromFile(file.path) }, size)
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            SkikoMirai.logger.error("无法获取字体: $fontName 请检查是否下载对应字体")
            SkikoMirai.logger.error("字体目录位于 ${fontFolder.absolutePath}")
            Font(Typeface.makeDefault(), size)
        }
    }
}

public val fontFolder: File = SkikoMirai.dataFolder.resolve("Fonts")