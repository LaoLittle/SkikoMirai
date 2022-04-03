package org.laolittle.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

public object FontConfig : AutoSavePluginConfig("FontConfig") {
    public val fonts: MutableMap<String, String> by value(mutableMapOf())
}