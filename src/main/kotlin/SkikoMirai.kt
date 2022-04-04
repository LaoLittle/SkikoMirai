package org.laolittle.plugin

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.jetbrains.skiko.Library
import org.jetbrains.skiko.hostId
import kotlin.coroutines.ContinuationInterceptor

public object SkikoMirai : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.SkikoMirai",
        name = "SkikoMirai",
        version = "1.0.4",
    ) {
        author("LaoLittle")
    }
) {
    override fun onEnable() {
        fontFolder.mkdirs()
        FontConfig.reload()
        logger.info { "Plugin loaded" }
    }

    init {
        System.setProperty(SKIKO_LIBRARY_PATH_PROPERTY, SkikoLibFolder.path)
        CoroutineScope(SupervisorJob(coroutineContext[Job])).launch {
            val downloadLib = suspend {
                HttpClient(OkHttp).use { client ->

                }
            }
            if (!SkikoLibFile.isFile) {
                downloadLib()
            } else {
                if (SkikoLibFile.sha256 == "") {

                } else downloadLib()
            }
            Library.load()
        }
    }
}