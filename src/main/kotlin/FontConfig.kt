package org.laolittle.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object FontConfig : AutoSavePluginConfig ("FontConfig") {
    val fonts by value(mutableMapOf<String, String>())
}