import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.10.0"
    `maven-publish`
}

group = "org.laolittle.plugin"
val ver = "1.0.4"

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

repositories {
    mavenCentral()
    maven("https://maven.aliyun.com/repository/central")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.laolittle.plugin"
            artifactId = "SkikoMirai"

            from(components["java"])
        }
    }
}

val osName: String = System.getProperty("os.name")
val targetOs = when {
    osName == "Mac OS X" -> "macos"
    osName.startsWith("Win") -> "windows"
    osName.startsWith("Linux") -> "linux"
    else -> error("Unsupported OS: $osName")
}

var targetArch = when (val osArch: String = System.getProperty("os.arch")) {
    "x86_64", "amd64" -> "x64"
    "aarch64" -> "arm64"
    else -> error("Unsupported arch: $osArch")
}

val skikoVersion = "0.7.16"
val target = "${targetOs}-${targetArch}"
//version = "$ver-$target"
version = ver
dependencies {
    //implementation("org.jetbrains.skiko:skiko-awt-runtime-$target:$skikoVersion")
    compileOnly("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:$skikoVersion")
    api("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:$skikoVersion")
    compileOnly("org.jetbrains.skiko:skiko-awt-runtime-linux-arm64:$skikoVersion")
}