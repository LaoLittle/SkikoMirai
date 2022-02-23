# SkikoMirai
> Skiko前置插件 for Mirai-Console

Based on [Jetbrains/skiko](https://github.com/JetBrains/skiko)

支持的平台:
- Windows-x86_64
- Linux-x86_64
- Linux-arm64

```kotlin
内置的工具
Surface.getBytes()
Surface.toExternalResource()

获取字体请使用Fonts.get() //Fonts[]
会读取插件数据目录里的字体文件

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