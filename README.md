# SkikoMirai
> Skiko前置插件 for Mirai-Console

[![jitpack](https://jitpack.io/v/LaoLittle/SkikoMirai.svg)](https://jitpack.io/#LaoLittle/SkikoMirai)
[![version](https://img.shields.io/badge/dynamic/json.svg?color=cyan&label=latest%20version&query=$.tag_name&url=https://github.com/LaoLittle/SkikoMirai/releases/latest)](https://github.com/LaoLittle/SkikoMirai/releases/latest)

Based on [Jetbrains/skiko](https://github.com/JetBrains/skiko)

支持的平台:
- Windows-x86_64
- Linux-x86_64
- Linux-arm64

```kotlin
内置的工具
Surface.getBytes()
Surface.toExternalResource()
```

获取字体请使用`Fonts.get() //Fonts[]`

会读取插件数据目录里的字体文件

使用拓展中缀函数`Fonts.usedBy()`，
可以标记一个功能所使用的字体，方便用户修改

示例: 
```kotlin
val phFont: Font = Fonts["MiSans-Bold", 100f] usedBy "PornHub生成器"
```

配置文件示例:
```yaml
funts:
  PornHub生成器: 'MiSans-Bold'
```

## 字体命名规则: 
字体名-字体风格

例如:
- MiSans-Bold
- Noto Sans SC-BOLD

字体风格支持: 
- NORMAL
- BOLD
- ITALIC
- BOLD_ITALIC

当无法在字体文件夹下找到字体文件，会自动尝试获取系统字体

配置Gradle: 
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    // 无需编译 && 无需让mirai-console(>= 2.11.0)下载
    compileOnly("com.github.LaoLittle:SkikoMirai:1.0.4")
}
```

配置插件依赖示例: 
```kotlin
object DrawMeme : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.draw.DrawMeme",
        name = "DrawMeme",
        version = "1.0.4",
    ) {
        author("LaoLittle")

        dependsOn(
            id = "org.laolittle.plugin.SkikoMirai",
            versionRequirement = ">=1.0.3",
            // 前置插件是否可选
            isOptional = true
        )
    }
)
```