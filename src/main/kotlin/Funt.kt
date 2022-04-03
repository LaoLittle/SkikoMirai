package org.laolittle.plugin

import org.jetbrains.skia.Font

public infix fun Font.usedBy(funName: String): Font {
    FontConfig.funts[funName]?.let {
        return Fonts[it, size]
    }

    typeFaces.forEach { (fontName, tf) ->
        if (tf == typeface) {
            FontConfig.funts[funName] = fontName
            return this
        }
    }
    SkikoMirai.logger.warning("未找到TypeFace对应字体名称")
    return this
}