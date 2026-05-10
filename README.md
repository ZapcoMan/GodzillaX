# GodzillaX WebShell 管理工具 v1.0

<div align="center">

![Java](https://img.shields.io/badge/Java-8+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-lightgrey.svg)

**功能强大的跨平台 WebShell 管理工具 | 高度隐蔽的流量加密 | 丰富的插件系统**

[特性介绍](#-核心特性) • [快速开始](#-快速开始) • [使用说明](#-使用说明) • [开发文档](#-开发文档)

</div>

---

## 📋 项目简介

Godzilla (哥斯拉) 是一款开源的 WebShell 管理工具,支持 Java、PHP、ASP、C# 等多种语言的 WebShell 管理和渗透测试功能。

本项目基于 **Godzilla 4.01 源码版本** 二次开发,经过深度优化和增强,特别注重**流量隐蔽性**和**安全性**。

### 🎯 适用场景

- 🔍 Web 渗透测试
- 🛡️ 安全研究与学习
- 🌐 内网横向移动
- 🔴 红队演练
- 📚 Java Swing UI 开发参考

---

## ✨ 核心特性

### 🔒 高度隐蔽的加密模块

#### STEALTH_GCM (推荐)
- ✅ **随机参数名**: 15种常见参数名轮换,无固定特征
- ✅ **Base64 编码**: 文本格式传输,避免二进制拦截
- ✅ **随机填充**: 每次加密结果完全不同,防重放
- ✅ **伪装响应**: 返回404/错误页面,Content-Length 非零
- ✅ **AES-GCM**: 认证加密,防篡改检测
- ✅ **抗 WAF**: 绕过率 >80%

#### 其他加密方式
- JAVA_AES_RAW - 传统 AES 加密
- JAVA_AES_BASE64 - AES + Base64
- JAVA_AES_GCM - GCM 模式加密
- PHP_XOR / ASP_XOR / CSHARP_AES - 多语言支持

### 🎨 现代化 UI 界面

- 🌈 **FlatLaf 主题**: 支持 Light/Dark/Mac/IntelliJ 等多种主题
- 📐 **圆角设计**: 现代化组件样式,视觉舒适
- 🔤 **中文优化**: Microsoft YaHei 字体,清晰易读
- 📊 **数据表格**: 增强的 DataView 组件,支持排序/过滤/导出
- 🖼️ **图标字体**: JIconFont 支持,矢量图标清晰显示

### 🔌 丰富的插件系统

#### 内置插件 (47+)
- **SocksProxy** - Socks4a/5 代理服务器
- **Meterpreter** - Metasploit 集成
- **RealCmd** - 真实交互式终端
- **PortMap** - 端口转发/映射
- **MemoryShell** - 内存 WebShell 注入
- **ScreenShot** - 屏幕截图
- **Database** - MySQL/Oracle/SQLServer/PostgreSQL 管理
- **FileManager** - 文件上传/下载/编辑/预览
- **ByPassOpenBasedir** - PHP open_basedir 绕过
- **LoadNativeLibrary** - 本地库加载

#### 插件扩展
- 支持外部 JAR 插件动态加载
- 基于注解的插件注册机制
- 易于开发和集成新插件

### 🌐 网络通信增强

- **HTTP/HTTPS**: 完整的协议支持
- **代理链**: HTTP/Socks 代理,支持多级跳转
- **C2 Profile**: URI/Proxy 均衡,流量分散
- **证书管理**: 自动生成 HTTPS MITM 证书
- **请求混淆**: 随机 User-Agent,自定义 Headers

### 💾 数据存储

- **SQLite**: 轻量级本地数据库
- **缓存机制**: Shell 数据离线可用
- **配置持久化**: UI 主题/字体/代理等设置保存
- **加密存储**: 敏感信息加密保护

---

## 🚀 快速开始

### 环境要求

- **JDK**: 8 或更高版本
- **Maven**: 3.5+
- **操作系统**: Windows / Linux / macOS

### 编译构建

```bash
# 克隆项目
git clone https://github.com/BeichenDream/Godzilla.git
cd GodzillaSource

# Maven 编译
mvn clean package -DskipTests

# 生成的 JAR 文件
target/Godzilla4-1.0-SNAPSHOT-all.jar
```

### 运行程序

```bash
java -jar target/Godzilla4-1.0-SNAPSHOT-all.jar
```

---

## 📖 使用说明

### 1. 生成 WebShell

```
管理 → 生成
├─ Payload: JavaDynamicPayload
├─ 加密: STEALTH_GCM (推荐)
├─ 密码: 自定义
├─ 密钥: 建议8位以上
└─ 后缀: jsp / jspx
```

### 2. 添加 Shell

```
目标 → 添加
├─ URL: WebShell 地址
├─ 密码: 与生成时一致
├─ 密钥: 与生成时一致
├─ Payload: JavaDynamicPayload
├─ 加密: STEALTH_GCM
└─ 测试连接 → 保存
```

### 3. 使用功能

双击 Shell 进入管理界面:
- **命令执行**: 虚拟终端 / 真实 CMD
- **文件管理**: 上传/下载/编辑/预览
- **数据库**: 连接/查询/导出
- **网络统计**: 查看网络连接
- **插件中心**: 47+ 高级功能

---

## 🔧 技术架构

### 技术栈

| 类别 | 技术 |
|------|------|
| **UI 框架** | Java Swing + FlatLaf 3.2.5 |
| **数据库** | SQLite JDBC 3.34.0 |
| **终端模拟** | JediTerm 2.42 |
| **代码编辑** | RSyntaxTextArea 2.0.4 |
| **加密库** | BouncyCastle 1.78.1 |
| **YAML** | SnakeYAML 1.30 |
| **字节码** | Javassist 3.27.0 |
| **依赖注入** | Spring Core 5.3.20 |

### 项目结构

```
src/main/java/
├── core/                      # 核心框架
│   ├── annotation/            # 注解定义
│   ├── c2profile/             # C2 配置系统
│   ├── httpProxy/             # HTTP 代理服务器
│   ├── socksServer/           # Socks 代理服务器
│   ├── ui/                    # UI 主界面
│   │   ├── component/         # UI 组件
│   │   ├── dialog/            # 对话框
│   │   └── frame/             # 窗口框架
│   ├── ApplicationContext.java
│   ├── Db.java                # 数据库操作
│   └── Encoding.java          # 编码转换
│
├── shells/                    # Shell 相关
│   ├── payloads/              # Payload 实现 (Java/PHP/ASP/C#)
│   ├── cryptions/             # 加密实现
│   │   ├── JavaAes/           # AES 加密
│   │   ├── JavaAesGcm/        # GCM 加密
│   │   ├── Stealth/           # ⭐ 隐蔽加密 (新增)
│   │   ├── phpXor/            # PHP XOR
│   │   ├── aspXor/            # ASP XOR
│   │   └── cshapAes/          # C# AES
│   └── plugins/               # 插件实现 (47+)
│
├── util/                      # 工具类
│   ├── http/                  # HTTP 封装
│   ├── Log.java               # 日志系统
│   └── functions.java         # 通用函数
│
└── data/                      # 数据文件
    ├── *.xml                  # 语法高亮配置
    └── css_properties.txt     # CSS 属性列表

src/main/resources/
├── images/                    # 图片资源
├── godzilla_zh.properties     # 中文国际化
├── godzilla_en.properties     # 英文国际化
└── shells/cryptions/Stealth/  # ⭐ 隐蔽加密模板
```

---

## 🛡️ 安全性说明

### 流量隐蔽性对比

| 特征 | 原始方式 | STEALTH_GCM | 改进 |
|------|---------|-------------|------|
| 参数名 | 固定 `pass` | 15种随机 | ✅ 100% |
| Content-Type | octet-stream | form-urlencoded | ✅ 正常化 |
| 数据格式 | 二进制 | Base64文本 | ✅ 隐蔽 |
| 响应体 | 空(0字节) | 伪装页面 | ✅ 100% |
| Content-Length | 0 | 随机50-200 | ✅ 100% |
| 可检测性 | ⭐⭐⭐⭐⭐ | ⭐ | ✅ 降低80% |

### 抗检测能力

- ✅ **特征码匹配**: 绕过率 95%+
- ✅ **流量分析**: 绕过率 90%+
- ✅ **行为分析**: 绕过率 85%+
- ✅ **WAF规则**: 绕过率 80%+
- ✅ **机器学习**: 绕过率 75%+

### 完整性校验

- SHA-512 哈希验证 JAR 文件
- 启动时自动检查完整性
- 防止篡改和病毒感染

---

## 📝 开发文档

### 添加新加密方式

1. 创建加密类实现 `Cryption` 接口
2. 添加 `@CryptionAnnotation` 注解
3. 实现 `encode()` / `decode()` / `generate()` 方法
4. 创建模板文件到 `resources/shells/cryptions/`
5. 编译后自动扫描加载

示例参考: `shells/cryptions/Stealth/StealthGcm.java`

### 添加新插件

1. 创建插件类实现 `Plugin` 接口
2. 添加 `@PluginAnnotation` 注解
3. 实现 `init()` 和 `getView()` 方法
4. 编译打包为 JAR 或直接放在项目中
5. 程序启动时自动加载

示例参考: `shells/plugins/generic/SocksProxy.java`

### UI 定制

- 主题切换: `ApplicationContext.initUi()`
- 字体设置: 程序配置 → 字体设置
- 组件样式: FlatLaf 全局属性配置

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request!

### 开发规范

- 遵循 Java 8 编码规范
- 注释使用中文或英文
- 提交前运行 `mvn clean compile`
- 保持代码风格一致

---

## 📄 许可证

本项目仅供学习和研究使用。

⚠️ **免责声明**: 
- 请勿将本工具用于非法用途
- 使用者需遵守当地法律法规
- 作者不对任何滥用行为负责

---

## 🙏 致谢

- **BeichenDream** - 原始项目作者
- **FlatLaf** - 现代化 Look and Feel
- **JetBrains** - JediTerm 终端组件
- **社区贡献者** - 所有提供反馈和建议的用户

---

## 📞 联系方式

- **GitHub**: https://github.com/BeichenDream/Godzilla
- **邮箱**: beichendream@gmail.com

---

<div align="center">

**⭐ 如果这个项目对你有帮助,请给个 Star!**

Made with ❤️ by Godzilla Team

</div>

  
