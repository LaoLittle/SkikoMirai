# SkikoMirai
> Skiko前置插件 for Mirai-Console

[![](https://jitpack.io/v/LaoLittle/SkikoMirai.svg)](https://jitpack.io/#LaoLittle/SkikoMirai)

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

配置文件示例 (使用DrawMeme):
```yaml
funts: 
  黑白图片: 'MiSans-Bold'
  5k兆顶部文字: 'Noto Sans SC-BOLD'
  5k兆底部文字: 'Noto Serif SC-BOLD'
  0%生成器: 'MiSans-Regular'
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
    val skikoVer = "0.7.16"
    compileOnly(skikoAwt("windows-x64:$skikoVer"))
    compileOnly(skikoAwt("linux-x64:$skikoVer"))
    compileOnly(skikoAwt("linux-arm64:$skikoVer"))
    implementation("com.github.LaoLittle:SkikoMirai:1.0.4")
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
            // 前置插件可选
            isOptional = true
        )
    }
)
```