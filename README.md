# SkikoMirai
> Skiko前置插件 for Mirai-Console

[![jitpack](https://jitpack.io/v/LaoLittle/SkikoMirai.svg)](https://jitpack.io/#LaoLittle/SkikoMirai)
[![version](https://img.shields.io/badge/dynamic/json.svg?color=cyan&label=latest%20version&query=$.tag_name&url=https://github.com/LaoLittle/SkikoMirai/releases/latest)](https://github.com/LaoLittle/SkikoMirai/releases/latest)

Based on [Jetbrains/skiko](https://github.com/JetBrains/skiko)

### 使用说明

-----
- 1.启动一次插件，生成插件的数据目录
   - 位于`data/org.laolittle.plugin.SkikoMirai` *
- 2.从 https://github.com/LaoLittle/SkikoLibs 获取你所需要的库文件
  - 请根据操作系统自行选择，若不会操作则全部下载。
- 3.将下载得到的库放入插件数据目录*下的`lib`文件夹内
- 4.启动插件

-----
### Gif编码器

-----
本插件利用`jni`调用Rust编写的[Gif编 / 解码器](https://github.com/ImageOptim/gifski)，相关源码位于

`src/main/kotlin/gif`及`src/main/rust`内

自行编译：Clone本项目，切换到`./gifski`目录下

然后执行`cargo build --release`

若要使用Gif编解码器，请将`gifski`库文件放置于`lib`文件夹内

-----

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
    compileOnly("com.github.LaoLittle:SkikoMirai:1.0.5")
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