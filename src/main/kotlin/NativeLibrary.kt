package org.laolittle.plugin

import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.id

internal val DefaultNativeLibFolder =
    PluginManager.pluginsDataFolder.resolve(SkikoMirai.id).resolve("lib").apply { mkdirs() }