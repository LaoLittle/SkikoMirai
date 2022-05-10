package org.laolittle.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

public object FontConfig : AutoSavePluginConfig("FontConfig") {
    public val globalFont: String by value("default")

    public val funts: MutableMap<String, String> by value(mutableMapOf())
}