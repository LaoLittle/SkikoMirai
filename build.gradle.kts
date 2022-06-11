import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    val kotlinVersion = "1.7.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.1"
    `maven-publish`
}

group = "org.laolittle.plugin"
version = "1.1.0"

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
    val skikoVersion = "0.7.16"
    api("org.jetbrains.skiko:skiko-awt:$skikoVersion") {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    /*
    implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:$skikoVersion")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:$skikoVersion")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-linux-arm64:$skikoVersion")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-x64:$skikoVersion")
    implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:$skikoVersion")
     */
}

val generateJniHeaders: Task by tasks.creating {
    group = "build"
    dependsOn(tasks.getByName("compileKotlin"))

    // For caching
    val path = "build/generated/jni"
    inputs.dir("src/main/kotlin")
    outputs.dir(path)

    doLast {
        val javaHome = org.gradle.internal.jvm.Jvm.current().javaHome
        val javap = javaHome.resolve("bin").walk().firstOrNull { it.name.startsWith("javap") }?.absolutePath ?: error("javap not found")
        val javac = javaHome.resolve("bin").walk().firstOrNull { it.name.startsWith("javac") }?.absolutePath ?: error("javac not found")
        val buildDir = file("build/classes/kotlin/main")
        val tmpDir = file("build/tmp/jvmJni").apply { mkdirs() }

        val bodyExtractingRegex = """^.+\Rpublic \w* ?class ([^\s]+).*\{\R((?s:.+))\}\R$""".toRegex()
        val nativeMethodExtractingRegex = """.*\bnative\b.*""".toRegex()

        buildDir.walkTopDown()
            .filter { "META" !in it.absolutePath }
            .forEach { file ->
                if (!file.isFile) return@forEach

                val output = org.apache.commons.io.output.ByteArrayOutputStream().use {
                    project.exec {
                        commandLine(javap, "-private", "-cp", buildDir.absolutePath, file.absolutePath)
                        standardOutput = it
                    }.assertNormalExitValue()
                    it.toByteArray().decodeToString()
                }

                val (qualifiedName, methodInfo) = bodyExtractingRegex.find(output)?.destructured ?: return@forEach

                val lastDot = qualifiedName.lastIndexOf('.')
                val packageName = qualifiedName.substring(0, lastDot)
                val className = qualifiedName.substring(lastDot+1, qualifiedName.length)

                val nativeMethods =
                    nativeMethodExtractingRegex.findAll(methodInfo).map { it.groups }.flatMap { it.asSequence().mapNotNull { group -> group?.value } }.toList()
                if (nativeMethods.isEmpty()) return@forEach

                val source = buildString {
                    appendLine("package $packageName;")
                    appendLine("public class $className {")
                    for (method in nativeMethods) {
                        if ("()" in method) appendLine(method)
                        else {
                            val updatedMethod = StringBuilder(method).apply {
                                var count = 0
                                var i = 0
                                while (i < length) {
                                    if (this[i] == ',' || this[i] == ')') insert(i, " arg${count++}".also { i += it.length + 1 })
                                    else i++
                                }
                            }
                            appendLine(updatedMethod)
                        }
                    }
                    appendLine("}")
                }
                val outputFile = tmpDir.resolve(packageName.replace(".", "/")).apply { mkdirs() }.resolve("$className.java").apply { delete() }.apply { createNewFile() }
                outputFile.writeText(source)

                project.exec {
                    commandLine(javac, "-h", path, outputFile.absolutePath)
                }.assertNormalExitValue()
            }
    }
}