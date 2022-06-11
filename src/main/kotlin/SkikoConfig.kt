package org.laolittle.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

public object SkikoConfig : AutoSavePluginConfig("SkikoConfig") {
    @ValueDescription("检查Skiko版本")
    public val check: Boolean by value(true)

    @ValueDescription("使用的Skiko库版本")
    public val skikoVersion: String by value("latest")

    @ValueDescription("Skiko库所在路径")
    public val skikoLibPath: String by value(DefaultNativeLibFolder.path)

    @ValueDescription("下载源: JetBrains")
    public val libSource: Source by value(Source.JetBrains)
}

public enum class Source {
    JetBrains,
}