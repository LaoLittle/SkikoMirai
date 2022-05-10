package org.laolittle.plugin

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.jetbrains.skia.impl.Library
import java.io.File
import java.io.InputStream

public object SkikoMirai : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.SkikoMirai",
        name = "SkikoMirai",
        version = "1.0.8",
    ) {
        author("LaoLittle")
    }
) {
    override fun onEnable() {
        SkikoConfig.reload()

        val baseUrl = when (SkikoConfig.libSource) {
            Source.Github -> "https://github.com/LaoLittle/SkikoLibs/raw/master"
            Source.Gitee -> "https://gitee.com/laolittle/skiko-libs/raw/master"
        }

        if (SkikoConfig.check) {
            val client = HttpClient(OkHttp)
            try {
                runBlocking(coroutineContext) {
                    val skikoVer = async {
                        if (SkikoConfig.skikoVersion == "latest") client.get("$baseUrl/latest")
                        else SkikoConfig.skikoVersion
                    }

                    val shaFile = File("$SkikoLibFile.sha256")
                    if (!shaFile.isFile) {
                        val sha = async {
                            client.get<String>(
                                "$baseUrl/skiko/${skikoVer.await()}/${
                                    SkikoLibFile.name.replace(
                                        Regex(".*.(dll|dylib|so)"),
                                        ""
                                    )
                                }.sha256"
                            ) {
                                userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36 Edg/100.0.1185.29")
                            }
                        }

                        shaFile.writeText(sha.await())
                    }

                    if (!(SkikoLibFile.isFile && SkikoLibFile.sha256.also(::println) == shaFile.readText())) {
                        client.get<InputStream>("$baseUrl/skiko/$skikoVer/${SkikoLibFile.name}").use { input ->
                            SkikoLibFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                if (!SkikoLibFile.isFile) {
                    e.printStackTrace()
                    logger.error(
                        """
                无法自动获取Skiko运行所需库，请自行前往下载
                Github: https://github.com/LaoLittle/SkikoLibs/tree/master/skiko
                Gitee: https://gitee.com/laolittle/skiko-libs/tree/master/skiko
                
                遇到意外的错误，本插件将不会启用
            """.trimIndent()
                    )
                    return
                }
            } finally {
                client.close()
            }
        }

        loadSkikoLibrary()
        fontFolder.mkdirs()
        FontConfig.reload()
        logger.info { "Plugin loaded" }
    }

    public fun loadSkikoLibrary() {
        Library.staticLoad()
    }

    init {
        System.setProperty(SKIKO_LIBRARY_PATH_PROPERTY, SkikoConfig.skikoLibPath)
    }
}