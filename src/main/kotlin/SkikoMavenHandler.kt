package org.laolittle.plugin

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jetbrains.skiko.hostId
import java.io.InputStream

private const val JetBrainsSkikoMaven = "https://maven.pkg.jetbrains.space/public/p/compose/dev/org/jetbrains/skiko"
public suspend fun latestVersion(): Version {
    var latest = Version(0, 0, 0)

    val client = HttpClient(OkHttp)
    val regex = Regex("""<a(.*?)>(.*?)/(</a>)""")
    val html = client.get("$JetBrainsSkikoMaven/$repository") {
        userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.33")
        headers {
            set("HttpHeaders.Authorization", "maven.pkg.jetbrains.space")
        }
    }.bodyAsText()

    val matches = regex.findAll(html)
    matches.forEach {
        val str = it.groupValues[2]

        val ver = Version.fromStringOrNull(str) ?: return@forEach
        if (latest < ver) latest = ver
    }

    return latest
}

public suspend fun getSkiko(version: Version): InputStream {
    val client = HttpClient(OkHttp)

    return client
        .get("https://maven.pkg.jetbrains.space/public/p/compose/dev/org/jetbrains/skiko/$repository/$version/$repository-$version.jar")
        .body()
}

public val repository: String by lazy { "skiko-awt-runtime-$hostId" }

public class Version(public vararg val sub: Byte) : Comparable<Version> {
    override operator fun compareTo(other: Version): Int {
        repeat(maxOf(sub.size, other.sub.size)) { i ->
            val tsub = sub.getOrNull(i) ?: return -1
            val osub = other.sub.getOrNull(i) ?: return 1

            if (tsub < osub) return -1
        }

        return 0
    }

    public operator fun compareTo(other: Int): Int {
        return sub.first().compareTo(other)
    }

    public operator fun compareTo(other: String): Int {
        return compareTo(fromString(other))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Version) return false
        return sub.contentEquals(other.sub)
    }

    override fun toString(): String {
        return sub.joinToString(".")
    }

    override fun hashCode(): Int {
        return sub.hashCode()
    }

    public companion object {
        public fun fromString(string: String): Version {
            val sp = string.split('.')
            val arr = ByteArray(sp.size)
            sp.forEachIndexed { i, sub ->
                arr[i] = sub.toByte()
            }

            return Version(*arr)
        }

        public fun fromStringOrNull(string: String): Version? {
            val sp = string.split('.')
            val arr = ByteArray(sp.size)
            sp.forEachIndexed { i, sub ->
                arr[i] = sub.toByteOrNull() ?: return null
            }

            return Version(*arr)
        }
    }
}