import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.0-M2"
    `maven-publish`
}

group = "org.laolittle.plugin"
version = "1.0.4"

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
    api("org.jetbrains.skiko:skiko-awt:0.7.16")
}