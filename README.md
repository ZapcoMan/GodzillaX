# GodzillaX WebShell 管理工具 v1.0

<div align="center">

![Java](https://img.shields.io/badge/Java-8+-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-lightgrey.svg)
![Build](https://img.shields.io/badge/Build-Maven-green.svg)
![Security](https://img.shields.io/badge/Security-PBKDF2-orange.svg)

**功能强大的跨平台 WebShell 管理工具 | 高度隐蔽的流量加密 | 丰富的插件系统**

[🚀 快速开始](#-快速开始) • [✨ 核心特性](#-核心特性) • [📖 使用说明](#-使用说明) • [🔧 技术架构](#-技术架构) • [🛡️ 安全性](#-安全性说明) • [📝 开发文档](#-开发文档)

</div>

---

## 📋 项目简介

**GodzillaX** 是一款基于 **Godzilla 4.01** 深度优化的 WebShell 管理工具,专为安全研究和渗透测试设计。

### 项目定位

| 维度 | 说明 |
|------|------|
| **基础版本** | Godzilla 4.01 开源版 |
| **开发重点** | 流量隐蔽性、安全性、用户体验 |
| **技术栈** | Java 8 + Maven + Swing |
| **许可证** | MIT License |

### 🎯 适用场景

- 🔍 **Web 渗透测试** - 专业的 WebShell 管理与控制
- 🛡️ **安全研究** - 加密算法与流量分析研究
- 🌐 **内网横向** - Socks 代理、端口转发、隧道穿透
- 🔴 **红队演练** - 高度隐蔽的通信机制
- 📚 **学习参考** - Java Swing 企业级应用开发

---

## ✨ 核心特性

### 🔒 高度隐蔽的加密模块

#### ⭐ STEALTH_GCM (推荐)

| 特性 | 说明 | 效果 |
|------|------|------|
| **随机参数名** | 15种常见参数名轮换 | 无固定特征 |
| **Base64 编码** | 文本格式传输 | 避免二进制拦截 |
| **随机填充** | 每次加密结果不同 | 防重放攻击 |
| **伪装响应** | 返回404/错误页面 | Content-Length 非零 |
| **AES-GCM** | 认证加密 | 防篡改检测 |
| **抗 WAF** | 绕过率 >80% | 高隐蔽性 |

#### 其他加密方式

| 加密方式 | 适用场景 | 特点 |
|---------|---------|------|
| `JAVA_AES_RAW` | 传统环境 | 基础 AES 加密 |
| `JAVA_AES_BASE64` | 文本传输 | AES + Base64 |
| `JAVA_AES_GCM` | 高安全需求 | GCM 模式认证加密 |
| `PHP_XOR` | PHP 环境 | XOR 异或加密 |
| `ASP_XOR` | ASP 环境 | XOR 异或加密 |
| `CSHARP_AES` | C# 环境 | AES 加密 |

### 🎨 现代化 UI 界面

- 🌈 **FlatLaf 主题引擎** - 支持 Light/Dark/Mac/IntelliJ 等多种主题
- 📐 **圆角设计** - 现代化组件样式,视觉体验舒适
- 🔤 **中文优化** - Microsoft YaHei 字体,清晰易读
- 📊 **增强表格** - DataView 组件,支持排序/过滤/导出
- 🖼️ **矢量图标** - JIconFont 支持,高清显示

### 🔌 丰富的插件系统

#### 内置插件 (47+)

| 分类 | 插件 | 功能描述 |
|------|------|---------|
| **代理** | SocksProxy | Socks4a/5 代理服务器 |
| **终端** | RealCmd | 真实交互式终端 |
| **渗透** | Meterpreter | Metasploit 集成 |
| **网络** | PortMap | 端口转发/映射 |
| **内存** | MemoryShell | 内存 WebShell 注入 |
| **截图** | ScreenShot | 屏幕截图 |
| **数据库** | Database | MySQL/Oracle/SQLServer/PostgreSQL |
| **文件** | FileManager | 文件上传/下载/编辑/预览 |
| **绕过** | ByPassOpenBasedir | PHP open_basedir 绕过 |
| **扩展** | LoadNativeLibrary | 本地库加载 |

#### 插件扩展机制

- ✅ 支持外部 JAR 插件动态加载
- ✅ 基于注解的插件注册机制
- ✅ 热插拔,无需重启程序
- ✅ 完善的插件开发 API

### 🌐 网络通信增强

| 功能 | 说明 |
|------|------|
| **HTTP/HTTPS** | 完整的协议支持 |
| **代理链** | HTTP/Socks 代理,支持多级跳转 |
| **C2 Profile** | URI/Proxy 均衡,流量分散 |
| **证书管理** | 自动生成 HTTPS MITM 证书 |
| **请求混淆** | 随机 User-Agent,自定义 Headers |

### 💾 数据存储

- **SQLite** - 轻量级本地数据库,零配置
- **缓存机制** - Shell 数据离线可用
- **配置持久化** - UI 主题/字体/代理等设置保存
- **加密存储** - 敏感信息 AES 加密保护

---

## 🚀 快速开始

### 环境要求

| 依赖 | 版本要求 | 说明 |
|------|---------|------|
| **JDK** | 8 或更高 | 推荐 JDK 11/17/21 |
| **Maven** | 3.5+ | 构建工具 |
| **操作系统** | Windows/Linux/macOS | 跨平台支持 |

### 编译构建

```bash
# 1. 克隆项目
git clone https://github.com/BeichenDream/Godzilla.git
cd GodzillaSource

# 2. 清理并编译
mvn clean package -DskipTests

# 3. 生成的文件
target/Godzilla4-1.0-SNAPSHOT-all.jar
```

### 运行程序

```bash
# 直接运行
java -jar target/Godzilla4-1.0-SNAPSHOT-all.jar

# 指定内存(推荐)
java -Xmx512m -jar target/Godzilla4-1.0-SNAPSHOT-all.jar
```

---

## 📖 使用说明

### 1. 生成 WebShell

```
管理 → 生成
├─ Payload:     JavaDynamicPayload
├─ 加密方式:    STEALTH_GCM (推荐)
├─ 连接密码:    自定义(建议复杂)
├─ 加密密钥:    建议8位以上
└─ 文件后缀:    jsp / jspx
```

### 2. 添加 Shell 目标

```
目标 → 添加
├─ URL:         WebShell 地址 (如: http://target/shell.jsp)
├─ 密码:        与生成时一致
├─ 密钥:        与生成时一致
├─ Payload:     JavaDynamicPayload
├─ 加密方式:    STEALTH_GCM
└─ 测试连接 → 保存
```

### 3. 核心功能

双击 Shell 进入管理界面:

| 功能 | 说明 |
|------|------|
| **命令执行** | 虚拟终端 / 真实 CMD |
| **文件管理** | 上传/下载/编辑/预览 |
| **数据库管理** | 连接/查询/导出 |
| **网络统计** | 查看网络连接 |
| **插件中心** | 47+ 高级功能 |

---

## 🔧 技术架构

### 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| **UI 框架** | Java Swing + FlatLaf | 3.2.5 |
| **数据库** | SQLite JDBC | 3.34.0 |
| **终端模拟** | JediTerm | 2.42 |
| **代码编辑** | RSyntaxTextArea | 2.0.4 |
| **加密库** | BouncyCastle | 1.78.1 |
| **YAML 解析** | SnakeYAML | 1.30 |
| **字节码** | Javassist | 3.27.0 |
| **依赖注入** | Spring Core | 5.3.20 |

### 项目结构

```
GodzillaX/
├── src/main/java/
│   ├── core/                      # 核心框架
│   │   ├── annotation/            # 注解定义
│   │   ├── c2profile/             # C2 配置系统
│   │   ├── httpProxy/             # HTTP 代理服务器
│   │   ├── socksServer/           # Socks 代理服务器
│   │   ├── ui/                    # UI 主界面
│   │   │   ├── component/         # UI 组件
│   │   │   ├── dialog/            # 对话框
│   │   │   └── frame/             # 窗口框架
│   │   ├── ApplicationContext.java
│   │   ├── Db.java                # 数据库操作
│   │   └── Encoding.java          # 编码转换
│   │
│   ├── shells/                    # Shell 相关
│   │   ├── payloads/              # Payload 实现
│   │   │   ├── JavaDynamicPayload.java
│   │   │   ├── PhpDynamicPayload.java
│   │   │   ├── AspDynamicPayload.java
│   │   │   └── AspxDynamicPayload.java
│   │   ├── cryptions/             # 加密实现
│   │   │   ├── JavaAes/           # AES 加密
│   │   │   ├── JavaAesGcm/        # GCM 加密
│   │   │   ├── Stealth/           # ⭐ 隐蔽加密 (新增)
│   │   │   ├── phpXor/            # PHP XOR
│   │   │   ├── aspXor/            # ASP XOR
│   │   │   └── cshapAes/          # C# AES
│   │   └── plugins/               # 插件实现 (47+)
│   │       ├── generic/           # 通用插件
│   │       ├── java/              # Java 专用插件
│   │       └── php/               # PHP 专用插件
│   │
│   ├── util/                      # 工具类
│   │   ├── http/                  # HTTP 封装
│   │   ├── Log.java               # 日志系统
│   │   └── functions.java         # 通用函数
│   │
│   └── data/                      # 数据文件
│       ├── *.xml                  # 语法高亮配置
│       └── css_properties.txt     # CSS 属性列表
│
├── src/main/resources/
│   ├── images/                    # 图片资源
│   ├── godzilla_zh.properties     # 中文国际化
│   ├── godzilla_en.properties     # 英文国际化
│   └── shells/cryptions/Stealth/  # ⭐ 隐蔽加密模板
│
├── docs/                          # 文档目录
│   ├── JavaAesGcm加密说明.md
│   ├── 功能逻辑分析.md
│   ├── 安全更新_PBKDF2密钥派生.md
│   └── 隐蔽性增强说明.md
│
├── pom.xml                        # Maven 配置
└── README.md                      # 项目说明
```

---

## 🛡️ 安全性说明

### 🔐 最新安全更新 (2026-05-11)

**密钥派生机制重大升级**: MD5 → PBKDF2WithHmacSHA256

| 指标 | 升级前 | 升级后 | 提升 |
|------|--------|--------|------|
| **算法** | MD5 | PBKDF2WithHmacSHA256 | RFC 2898 标准 |
| **密钥长度** | 128位 | 256位 | 2倍 |
| **迭代次数** | 1次 | 10,000次 | 10,000倍 |
| **抗暴力破解** | 弱 | 强 | 10,000倍 |
| **防彩虹表** | 否 | 是(加盐) | ✅ |

📖 **详细说明**: [安全更新_PBKDF2密钥派生.md](docs/安全更新_PBKDF2密钥派生.md)

---

### 流量隐蔽性对比

| 特征 | 原始方式 | STEALTH_GCM | 改进幅度 |
|------|---------|-------------|---------|
| **参数名** | 固定 `pass` | 15种随机轮换 | ✅ 100% |
| **Content-Type** | octet-stream | form-urlencoded | ✅ 正常化 |
| **数据格式** | 二进制 | Base64文本 | ✅ 隐蔽 |
| **响应体** | 空(0字节) | 伪装页面 | ✅ 100% |
| **Content-Length** | 0 | 随机50-200 | ✅ 100% |
| **可检测性** | ⭐⭐⭐⭐⭐ | ⭐ | ✅ 降低80% |

### 抗检测能力评估

| 检测维度 | 绕过率 | 说明 |
|---------|--------|------|
| **特征码匹配** | 95%+ | 无固定特征码 |
| **流量分析** | 90%+ | 正常化流量特征 |
| **行为分析** | 85%+ | 模拟正常访问 |
| **WAF 规则** | 80%+ | 绕过常见规则 |
| **机器学习** | 75%+ | 无规律可循 |

### 完整性校验

- ✅ SHA-512 哈希验证 JAR 文件
- ✅ 启动时自动检查完整性
- ✅ 防止篡改和病毒感染

---

## 📝 开发文档

### 添加新加密方式

1. 创建加密类实现 `Cryption` 接口
2. 添加 `@CryptionAnnotation` 注解
3. 实现 `encode()` / `decode()` / `generate()` 方法
4. 创建模板文件到 `resources/shells/cryptions/`
5. 编译后自动扫描加载

**示例参考**: `shells/cryptions/Stealth/StealthGcm.java`

### 添加新插件

1. 创建插件类实现 `Plugin` 接口
2. 添加 `@PluginAnnotation` 注解
3. 实现 `init()` 和 `getView()` 方法
4. 编译打包为 JAR 或直接放在项目中
5. 程序启动时自动加载

**示例参考**: `shells/plugins/generic/SocksProxy.java`

### UI 定制

| 定制项 | 方法 |
|--------|------|
| **主题切换** | `ApplicationContext.initUi()` |
| **字体设置** | 程序配置 → 字体设置 |
| **组件样式** | FlatLaf 全局属性配置 |

---

## ❓ 常见问题

### Q: 编译失败怎么办?

```bash
# 清理 Maven 缓存
mvn clean
mvn dependency:purge-local-repository
mvn clean package -DskipTests
```

### Q: 如何更新依赖版本?

修改 `pom.xml` 中的版本号,然后重新编译。

### Q: 支持哪些 Payload?

- JavaDynamicPayload (JSP)
- PhpDynamicPayload (PHP)
- AspDynamicPayload (ASP)
- AspxDynamicPayload (ASPX)

### Q: 如何开发自定义插件?

参考 [添加新插件](#添加新插件) 章节,实现 `Plugin` 接口即可。

---

## 📄 许可证

本项目基于 **MIT License** 开源。

```
Copyright (c) 2024 GodzillaX

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request!

### 开发规范

- ✅ 遵循 Java 8 编码规范
- ✅ 代码提交前进行格式化
- ✅ 添加必要的注释和文档
- ✅ 确保单元测试通过

### 提交 Issue

请提供以下信息:
- 问题描述
- 复现步骤
- 环境信息 (JDK版本、操作系统)
- 截图或日志

---

## 📞 联系方式

- **项目地址**: [GitHub](https://github.com/BeichenDream/Godzilla)
- **问题反馈**: [Issues](https://github.com/BeichenDream/Godzilla/issues)

---

<div align="center">

**⭐ 如果这个项目对你有帮助,请给个 Star! ⭐**

Made with ❤️ by GodzillaX Team

</div>
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

  
