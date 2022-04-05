package org.laolittle.plugin

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.jetbrains.skiko.Library
import java.io.InputStream

public object SkikoMirai : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.SkikoMirai",
        name = "SkikoMirai",
        version = "1.0.5",
    ) {
        author("LaoLittle")
    }
) {
    override fun onEnable() {
        System.setProperty(SKIKO_LIBRARY_PATH_PROPERTY, SkikoLibFolder.path)
        runBlocking {
            val downloadLib = suspend {
                HttpClient(OkHttp).use { client ->
                    client.get<InputStream>("https://github.com/LaoLittle/SkikoMirai/raw/master/skiko/0.7.17/libskiko-linux-arm64.so").use { input ->
                        SkikoLibFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }

            val sha = HttpClient(OkHttp).use { client ->
                client.get<String>("https://raw.githubusercontent.com/LaoLittle/SkikoMirai/master/skiko/0.7.17/libskiko-linux-arm64.so.sha256")
            }

            if (SkikoLibFile.isFile) println(SkikoLibFile.sha256)

            if (!SkikoLibFile.isFile || SkikoLibFile.sha256 != sha)
                downloadLib()
        }

        Library.load()

        fontFolder.mkdirs()
        FontConfig.reload()
        logger.info { "Plugin loaded" }
    }
}