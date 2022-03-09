package org.laolittle.plugin

import net.mamoe.mirai.console.command.SimpleCommand

object FontFlush : SimpleCommand(
    SkikoMirai, "skflush",
    description = "刷新字体"
){
    @Handler
    fun flush() {
        FontConfig.fonts.forEach { (name, file) ->
            if (file.isBlank()) return@forEach
            val fileName = Fonts.fontFolder.listFiles()?.find { name.uppercase() in it.name.uppercase() }?.name ?: ""
            FontConfig.fonts[name] = fileName
        }
    }
}