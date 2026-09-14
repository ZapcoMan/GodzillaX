# UI 现代化改造 - 完整实施方案

## 📅 更新日期
2026-09-14

## 📋 概述
对 GodzillaX 的 Swing UI 进行全面现代化改造，引入 FlatLaf 3.7.2 最新稳定版依赖，统一配置圆角、阴影、表格、菜单、滚动条等 UI 属性，显著提升视觉体验和操作舒适度。

---

## 🎯 改造目标

| 维度 | 改造前 | 改造后 |
|------|--------|--------|
| **FlatLaf 版本** | 3.2.5 | 3.7.2（最新稳定版） |
| **圆角** | 仅 4 个组件配置 arc=8 | 13 个组件统一 arc=10 |
| **表格** | 网格线 + 行高 28 | 关闭网格线 + 行高 32 + 交替行色 |
| **字体** | 仅部分组件 | 全局 Microsoft YaHei UI 13px |
| **滚动条** | 默认样式 | 10px 宽度 + 圆角 thumb + 隐藏箭头 |
| **菜单栏** | 与面板割裂 | 与面板同色，视觉连贯 |
| **编码** | GBK 默认（编译报错） | UTF-8 统一 |
| **配置方式** | 散落各处 | 集中在 ModernUITheme |

---

## 🔧 依赖升级

### pom.xml 修改

#### 1. FlatLaf 版本升级
```xml
<!-- 改造前 -->
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.2.5</version>
</dependency>

<!-- 改造后 -->
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.7.2</version>
</dependency>
```

同步升级的三个 FlatLaf 模块：
- `flatlaf` 3.2.5 → 3.7.2
- `flatlaf-intellij-themes` 3.2.5 → 3.7.2
- `flatlaf-extras` 3.2.5 → 3.7.2

#### 2. 编码统一（关键修复）
```xml
<properties>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <!-- 新增：统一 UTF-8 编码 -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.5.1</version>
    <configuration>
        <source>8</source>
        <target>8</target>
        <encoding>UTF-8</encoding>  <!-- 新增 -->
        <fork>true</fork>
    </configuration>
</plugin>
```

**修复效果**: 彻底解决项目长期存在的 GBK 不可映射字符编译错误（影响 `ShellFileManager`、`C2Profile`、`SocksProxyManagePanel` 等多个含 UTF-8 字符的源文件）。

---

## 📁 新增文件

### `src/main/java/core/ui/ModernUITheme.java`

集中式 UI 主题管理器，统一配置 FlatLaf 全局 UI 属性。所有 UI 配置从 `ApplicationContext.initUi()` 中迁移出来，集中管理，便于后续维护和主题切换。

#### 核心方法

```java
public final class ModernUITheme {
    // 全局 UI 初始化入口
    public static void apply() {
        configureGlobalDefaults();  // 统一圆角 + 阴影 + 边距 + 字体
        configureMenuBar();         // 菜单栏与面板同色
        configureTables();          // 表格现代化
        configureTrees();           // 树现代化
        configureLists();           // 列表现代化
        configureTextComponents();  // 文本组件
        configureButtons();         // 按钮强调色
        configurePopupMenu();       // 弹出菜单圆角边框
        configureSplitPane();       // 分割面板
        configureFileChooser();     // 文件选择器
        configureScrollPane();      // 滚动条现代化
        configureTabbedPane();      // Tab 紧凑化
        applySavedTheme();          // 应用持久化主题
    }

    // 工具方法
    public static Color hex(String hex);
    public static Color darken(Color src, int amount);
    public static Color overlay(Color src, Color overlay, int alpha);
    public static Border cardBorder();
}
```

#### 配置详情

##### 1️⃣ 统一圆角（arc=10）
```java
int arc = 10;
UIManager.put("Component.arc", arc);
UIManager.put("Button.arc", arc);
UIManager.put("CheckBox.arc", arc);
UIManager.put("ComboBox.arc", arc);
UIManager.put("ProgressBar.arc", arc);
UIManager.put("RadioButton.arc", arc);
UIManager.put("ScrollPane.arc", arc);
UIManager.put("Spinner.arc", arc);
UIManager.put("TextField.arc", arc);
UIManager.put("FormattedTextField.arc", arc);
UIManager.put("PasswordField.arc", arc);
UIManager.put("TextArea.arc", arc - 4);  // 文本区减弱圆角
UIManager.put("TextPane.arc", arc - 4);
UIManager.put("ToggleButton.arc", arc);
```

##### 2️⃣ 表格现代化
| 属性 | 改造前 | 改造后 |
|------|--------|--------|
| `Table.rowHeight` | 28 | 30（DataView 中 32） |
| `Table.showHorizontalLines` | true | false |
| `Table.showVerticalLines` | true | false |
| `Table.intercellSpacing` | 1x1 | 0x0 |
| `Table.alternateRowColor` | 未启用 | true |
| `TableHeader.height` | 35 | 36 |

**交替行色生成算法**:
```java
Color base = UIManager.getColor("Table.background");
Color alt = new Color(
    Math.max(0, base.getRed() - 8),
    Math.max(0, base.getGreen() - 8),
    Math.max(0, base.getBlue() - 8)
);
```

##### 3️⃣ 滚动条现代化
```java
UIManager.put("ScrollBar.width", 10);              // 宽度 10px
UIManager.put("ScrollBar.showButtons", false);     // 隐藏上下箭头
UIManager.put("ScrollBar.thumbArc", 999);          // 完全圆角
UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
UIManager.put("ScrollBar.allowsAbsolutePositioning", true);
```

##### 4️⃣ 按钮强调色
```java
Color accent = UIManager.getColor("Component.accentColor");
if (accent == null) accent = new Color(60, 130, 246);
UIManager.put("Button.default.background", accent);
UIManager.put("Button.default.foreground", Color.WHITE);
UIManager.put("Button.default.boldText", true);

// 悬停色加深
Color hover = new Color(
    Math.max(0, accent.getRed() - 20),
    Math.max(0, accent.getGreen() - 20),
    Math.max(0, accent.getBlue() - 20)
);
UIManager.put("Button.default.hoverBackground", hover);
```

##### 5️⃣ 菜单栏与面板同色
```java
Color accent = UIManager.getColor("Panel.background");
UIManager.put("MenuBar.background", accent);
UIManager.put("Menu.background", accent);
```

##### 6️⃣ 弹出菜单圆角边框
```java
UIManager.put("PopupMenu.dropShadowPainted", true);
UIManager.put("PopupMenu.border", BorderFactory.createCompoundBorder(
    new LineBorder(new Color(128, 128, 128, 80), 1),
    BorderFactory.createEmptyBorder(4, 4, 4, 4)
));
UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder(6, 12, 6, 12));
```

##### 7️⃣ 字体配置
```java
String fontName = SystemInfo.isWindows ? "Microsoft YaHei UI" : "Microsoft YaHei";
Font base = new Font(fontName, Font.PLAIN, 13);
UIManager.put("defaultFont", base);
// 同步设置 16 个组件字体...
```

##### 8️⃣ TabbedPane 紧凑化
```java
UIManager.put("TabbedPane.tabInsets", new Insets(6, 12, 6, 12));
UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
UIManager.put("TabbedPane.showContentSeparator", true);
UIManager.put("TabbedPane.tabSeparatorsFullHeight", false);
```

##### 9️⃣ SplitPane
```java
UIManager.put("SplitPane.dividerSize", 6);
UIManager.put("SplitPane.continuousLayout", true);
UIManager.put("SplitPaneDivider.gripColor", new Color(180, 180, 180, 0));  // 隐藏 grip dot
```

---

## 📝 修改文件清单

### 1. `pom.xml`
- FlatLaf 3.2.5 → 3.7.2（3 个模块同步升级）
- 新增 `project.build.sourceEncoding=UTF-8`
- 新增 `project.reporting.outputEncoding=UTF-8`
- maven-compiler-plugin 配置新增 `<encoding>UTF-8</encoding>`

### 2. `src/main/java/core/ApplicationContext.java`
- 新增 `import core.ui.ModernUITheme;`
- 重写 `initUi()` 方法：
  ```java
  public static void initUi() {
      try {
          ModernUITheme.apply();
      } catch (Throwable t) {
          Log.error(t);
      }
      JFrame.setDefaultLookAndFeelDecorated(true);
      JDialog.setDefaultLookAndFeelDecorated(true);
      // 兼容旧逻辑：如果数据库中保存了主题，覆盖默认主题
      String resourceNameString = Db.getSetingValue("ui-resourceName");
      String lafClassNameString = Db.getSetingValue("ui-lafClassName");
      if (resourceNameString == null && lafClassNameString == null) {
          Db.updateSetingKV("ui-lafClassName", "com.formdev.flatlaf.FlatIntelliJLaf");
      }
      lafClassNameString = Db.getSetingValue("ui-lafClassName");
      IJThemesPanel.setTheme(new IJThemeInfo(resourceNameString, lafClassNameString));
  }
  ```

### 3. `src/main/java/core/ui/MainActivity.java`
**主窗口现代化改造**：
- 状态栏：与菜单栏同色背景 + 顶部 1px 边框分隔
- SplitPane：`dividerSize=6`、`resizeWeight=0`、无 `oneTouchExpand`
- 主内容容器：6px 外边距，卡片式布局
- 表格：`AUTO_RESIZE_OFF` + 4px viewport 边距
- 菜单栏：4x6 边距
- 右键菜单：1px 灰边框 + 4px 内边距

```java
// 主内容容器添加少量外边距（现代化卡片式布局）
JPanel mainPanel = new JPanel(new BorderLayout());
mainPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
mainPanel.add(this.splitPane, "Center");
this.add(mainPanel, "Center");
```

### 4. `src/main/java/core/ui/ShellManage.java`
- 加载提示使用 14px Microsoft YaHei 字体
- Tab 使用 `SCROLL_TAB_LAYOUT`（避免 Tab 多时挤压）

### 5. `src/main/java/core/ui/component/DataView.java`
**表格核心改造**：
- 行高 28 → 32
- 关闭网格线（`showGrid=false`）
- 启用 FlatLaf 交替行色：`putClientProperty("JTable.alternateRowColor", Boolean.TRUE)`
- 选中样式 `filled`（无边框高亮）
- 表头 `plain` style、禁止列重排
- 单元格内边距 4x8

```java
this.setRowHeight(32);
this.setShowGrid(false);
this.setIntercellSpacing(new java.awt.Dimension(0, 0));
this.putClientProperty("JTable.alternateRowColor", Boolean.TRUE);
this.putClientProperty("JTable.selectionStyle", "filled");
this.putClientProperty("Table.cellPadding", new java.awt.Insets(2, 8, 2, 8));
```

---

## 🎨 视觉效果对比

### 表格改造

#### 改造前
```
┌────────────┬────────────┬────────────┐
│ URL        │ Payload    │ Cryption   │  ← 行高 28
├────────────┼────────────┼────────────┤  ← 网格线明显
│ shell1.jsp │ JavaDyn    │ AES_RAW    │
├────────────┼────────────┼────────────┤
│ shell2.jsp │ JavaDyn    │ STEALTH    │
└────────────┴────────────┴────────────┘
```

#### 改造后
```
─────────────────────────────────────────
 URL          Payload      Cryption       ← 行高 32，无网格线
─────────────────────────────────────────
 shell1.jsp   JavaDyn      AES_RAW        ← 交替行色（深 8）
 shell2.jsp   JavaDyn      STEALTH        ← 选中行填充高亮
─────────────────────────────────────────
```

### 滚动条改造

#### 改造前
```
▲▲▲▲   ← 上下箭头按钮
████   ← 默认宽度 15px
████   ← 方角 thumb
████
▲▲▲▲
```

#### 改造后
```
 ●●    ← 无箭头按钮
 ●●    ← 宽度 10px
 ●●    ← 完全圆角 thumb
 ●●
```

### 按钮改造

#### 改造前
```
┌──────────────┐
│   确定        │  ← 灰色背景
└──────────────┘
```

#### 改造后
```
╭──────────────╮
│   确定        │  ← 强调色背景 + 白字粗体
╰──────────────╯   ← 圆角 10
```

---

## 🔧 工具方法说明

`ModernUITheme` 提供了 4 个公开工具方法，方便后续 UI 开发使用：

| 方法 | 用途 | 示例 |
|------|------|------|
| `hex(String)` | 十六进制字符串转 Color | `Color c = ModernUITheme.hex("#3C82F6");` |
| `darken(Color, int)` | 颜色变暗 | `Color dark = ModernUITheme.darken(c, 20);` |
| `overlay(Color, Color, int)` | 半透明叠加色 | `Color sel = ModernUITheme.overlay(bg, accent, 60);` |
| `cardBorder()` | 卡片式边框 | `panel.setBorder(ModernUITheme.cardBorder());` |

---

## ✅ 验证测试

### 编译验证
```bash
mvn clean compile -DskipTests
# 结果：BUILD SUCCESS
# 无任何编译错误
```

### Lint 检查
- ✅ `ModernUITheme.java` 无错误
- ✅ `MainActivity.java` 无错误
- ✅ `ApplicationContext.java` 无错误
- ✅ `DataView.java` 无错误
- ✅ `ShellManage.java` 无错误
- ⚠️ `pom.xml` 仅有预存在的 CVE 安全警告（与本次改造无关）

### 编码修复验证
- ✅ `ShellFileManager.java`（含 UTF-8 中文）编译通过
- ✅ `C2Profile.java`（含 UTF-8 中文）编译通过
- ✅ `SocksProxyManagePanel.java`（含 UTF-8 中文）编译通过

---

## 🔄 向后兼容性

### 主题配置兼容
- ✅ 保留数据库 `seting` 表中的 `ui-resourceName` / `ui-lafClassName` 配置
- ✅ 保留 `IJThemesPanel.setTheme()` 调用，用户保存的主题仍生效
- ✅ 默认主题仍为 `FlatIntelliJLaf`

### 代码兼容
- ✅ `ModernUITheme.apply()` 包裹 try/catch，失败时降级到默认主题
- ✅ 所有 UI 配置使用 `UIManager.put()`，与 FlatLaf 原生配置完全兼容
- ✅ 修改的 UI 文件保持原有 API，无破坏性变更

---

## 🚀 后续扩展方向

### 短期
- [ ] 添加更多 IntelliJ Theme 选项（如 `Material Theme`、`Dracula`）
- [ ] 主题切换实时预览
- [ ] 自定义强调色配置（用户可选 UI 主色调）

### 中期
- [ ] 引入 SVG 图标库（FlatLaf Extras 已支持 `FlatSVGIcon`）
- [ ] 添加动画过渡效果（FlatLaf 3.7+ 支持）
- [ ] 暗色/亮色主题自动切换（跟随系统）

### 长期
- [ ] 支持用户自定义 CSS-like 样式表
- [ ] UI 主题市场（社区共享主题）
- [ ] 高对比度模式（无障碍）

---

## 📦 文件清单

```
本次改造涉及的文件：
├── pom.xml                                            # 依赖升级 + 编码配置
├── src/main/java/core/ui/ModernUITheme.java          # ⭐ 新增 - UI 主题管理器
├── src/main/java/core/ApplicationContext.java        # initUi() 重写
├── src/main/java/core/ui/MainActivity.java            # 主窗口现代化
├── src/main/java/core/ui/ShellManage.java             # Tab 现代化
└── src/main/java/core/ui/component/DataView.java      # 表格现代化
```

---

## 🔗 相关资源

- **FlatLaf 官方**: https://www.formdev.com/flatlaf/
- **FlatLaf GitHub**: https://github.com/JFormDesigner/FlatLaf
- **FlatLaf 3.7.2 Release Notes**: https://github.com/JFormDesigner/FlatLaf/releases/tag/3.7.2
- **Maven 编码配置**: https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html#encoding

---

## ✅ 完成状态

- [x] FlatLaf 3.2.5 → 3.7.2 升级
- [x] 修复 UTF-8 编码问题
- [x] 创建 `ModernUITheme.java` 集中配置类
- [x] 改造 `ApplicationContext.initUi()`
- [x] 改造 `MainActivity` 主窗口
- [x] 改造 `ShellManage` Tab 界面
- [x] 改造 `DataView` 表格样式
- [x] 编译测试通过
- [x] Lint 检查通过
- [x] 文档编写完成

---

**UI 现代化改造完成!** 🎉

GodzillaX 现在拥有更现代、更舒适、更一致的视觉体验，同时保留了原有的所有功能和主题配置兼容性。
