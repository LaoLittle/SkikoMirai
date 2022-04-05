package org.laolittle.plugin

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

public object SkikoConfig : ReadOnlyPluginConfig("SkikoConfig") {
    @ValueDescription("使用的Skiko库版本")
    public val skikoVersion: String by value("latest")
    @ValueDescription("Skiko库所在路径")
    public val skikoLibPath: String by value(DefaultSkikoLibFolder.path)
    @ValueDescription("下载源: Github或Gitee")
    public val libSource: Source by value(Source.Github)
}

public enum class Source {
    Github,
    Gitee
}