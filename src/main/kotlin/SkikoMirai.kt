package org.laolittle.plugin

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.runInterruptible
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.jetbrains.skiko.Library
import org.jetbrains.skiko.OS.*
import org.jetbrains.skiko.hostId
import org.jetbrains.skiko.hostOs
import java.nio.file.Path
import java.nio.file.StandardOpenOption.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.io.path.*

public object SkikoMirai : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.SkikoMirai",
        name = "SkikoMirai",
        version = "1.1.0",
    ) {
        author("LaoLittle")
    }
) {
    override fun onEnable() {
        SkikoConfig.reload()

        if (SkikoConfig.check) {
            logger.info { "开始下载skiko运行所需库" }
            val cacheFile = SkikoLibPath.toPath().resolve("cache").also(Path::createDirectories).resolve("$hostId.jar")

            val ver = Version.fromString(org.jetbrains.skiko.Version.skiko)

            val verFile = SkikoLibPath.resolve(".$ver").toPath()
            if (!verFile.isRegularFile()) {
                SkikoLibPath.toPath().firstOrNull { it.name.first() == '.' }?.deleteExisting()
                runBlocking(coroutineContext + CoroutineExceptionHandler { _, e -> e.printStackTrace() }) {
                    getSkiko(ver).use { input ->
                        cacheFile.outputStream(CREATE, TRUNCATE_EXISTING, WRITE).use { out ->
                            input.copyTo(out)
                        }
                    }

                    val zip = runInterruptible(Dispatchers.IO) {
                        ZipFile(cacheFile.toFile())
                    }

                    suspend fun copyZipTo(entry: ZipEntry, output: Path) {
                        runInterruptible(Dispatchers.IO) {
                            zip.getInputStream(entry).use { input ->
                                output.outputStream().use(input::copyTo)
                            }
                        }
                    }

                    copyZipTo(zip.getEntry(SkikoLibFile.name) ?: throw IllegalStateException(), SkikoLibFile.toPath())

                    when (hostOs) {
                        Android, Ios, JS -> {
                            logger.error("暂不支持$hostId")
                        }

                        Linux, MacOS -> {

                        }

                        Windows -> {
                            copyZipTo(zip.getEntry("icudtl.dat") ?: run {
                                throw IllegalStateException()
                            }, Path(SkikoConfig.skikoLibPath).resolve("icudtl.dat"))
                        }
                    }

                    cacheFile.deleteIfExists()
                    verFile.createFile()
                }
            }
        }

        loadSkikoLibrary()
        fontFolder.mkdirs()
        FontConfig.reload()
        logger.info { "Plugin loaded" }
    }

    public fun loadSkikoLibrary() {
        Library.load()
    }

    init {
        System.setProperty(SKIKO_LIBRARY_PATH_PROPERTY, SkikoConfig.skikoLibPath)
    }
}