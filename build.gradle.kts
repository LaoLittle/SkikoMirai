import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    val kotlinVersion = "1.6.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.0-M2"
    `maven-publish`
}

group = "org.laolittle.plugin"
version = "1.0.5"

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

dependencies {
    // 为防止mirai获取多余的依赖
    val skikoVersion = "0.7.17"
    api("org.jetbrains.skiko:skiko-awt:$skikoVersion")

    //implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:$skikoVersion")
    //implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:$skikoVersion")
    //implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-arm64:$skikoVersion")
}