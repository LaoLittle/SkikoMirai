package org.laolittle.plugin

import org.jetbrains.skia.Font
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.makeFromFile

object Fonts {
    val fontFolder by lazy {
        SkikoMirai.dataFolder.resolve("Fonts").also { it.mkdirs() }
    }

    operator fun get(fileName: String, size: Float = 100F): Font {
        val file = fontFolder.resolve(fileName)
        require(file.isFile) { "无法找到字体文件: $fileName" }
        return Font(Typeface.makeFromFile(file.path), size)
    }

    operator fun get(fontName: String, size: Float = 100F, fontStyle: FontStyle) =
        Font(Typeface.makeFromName(fontName, fontStyle), size)
}