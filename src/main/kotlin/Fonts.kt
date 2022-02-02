package org.laolittle.plugin

import org.jetbrains.skia.Font
import org.jetbrains.skia.Typeface
import org.jetbrains.skia.makeFromFile

object Fonts {
    val fontFolder = SkikoMirai.dataFolder.resolve("Fonts")

    operator fun get(fileName: String, size: Float = 100F): Font {
        val file = fontFolder.resolve(fileName)
        require(file.isFile) { "无法找到字体: $fileName" }

        file.readBytes()
        return Font(Typeface.makeFromFile(file.path), size)
    }
}