package core.ui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.SystemInfo;
import core.Db;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Font;

/**
 * 现代化 UI 主题管理器
 * <p>
 * 统一配置 FlatLaf 全局 UI 属性，提供：
 * 1. 圆角统一调整 (arc = 10)
 * 2. 阴影/边距/行高优化
 * 3. 表格/树/列表/菜单 现代化样式
 * 4. 默认深色配色 (FlatIntelliJLaf dark / 自定义)
 * 5. 字体配置 (Microsoft YaHei / 思源/默认)
 */
public final class ModernUITheme {

    private ModernUITheme() {
    }

    /**
     * 全局 UI 初始化入口
     * 由 {@link core.ApplicationContext#initUi()} 调用
     */
    public static void apply() {
        // 1. 先切换 LAF（基于数据库保存的主题）
        applySavedTheme();
        // 2. LAF 安装后再配置颜色/样式 —— 此时 UIManager 读取的是新主题颜色，
        //    避免切换主题后硬编码颜色停留在旧主题导致"按钮/菜单字看不清"
        configureAll();
        // 3. 关键 padding 兜底，避免 FlatLaf 内部字段为 null 抛 NPE
        ensureRequiredDefaults();
    }

    /**
     * 基于「当前已安装的 LAF」重新配置所有颜色/样式。
     * <p>
     * 主题切换后（如 ThemesPanel 实时预览、保存主题重启）必须重新调用本方法，
     * 才能让选择高亮、边框、按钮强调色等跟随新主题深浅。
     */
    public static void configureAll() {
        configureGlobalDefaults();
        configureMenuBar();
        configureTables();
        configureTrees();
        configureLists();
        configureTextComponents();
        configureButtons();
        configurePopupMenu();
        configureSplitPane();
        configureFileChooser();
        configureScrollPane();
        configureTabbedPane();
    }

    /**
     * LAF 安装完成后的兜底：确保 FlatLaf 依赖的关键 padding/Insets 非 null。
     * <p>
     * 这样即使后续存在与 flatlaf 核心库版本不匹配的旧代码触发 LAF 半初始化，
     * UIManager.getInsets(...) 仍能返回非 null 值，避免 FlatComboBoxUI.applyStyle
     * 因 this.padding 为 null 而抛 NullPointerException。
     */
    private static void ensureRequiredDefaults() {
        if (UIManager.getInsets("ComboBox.padding") == null) {
            UIManager.put("ComboBox.padding", new java.awt.Insets(2, 6, 2, 6));
        }
        String[] paddingKeys = {
                "TextField.padding", "FormattedTextField.padding", "PasswordField.padding",
                "TextArea.padding", "TextPane.padding", "Spinner.padding"
        };
        for (String key : paddingKeys) {
            if (UIManager.getInsets(key) == null) {
                UIManager.put(key, new java.awt.Insets(4, 8, 4, 8));
            }
        }
    }

    /**
     * 应用持久化保存的主题
     */
    private static void applySavedTheme() {
        // 清除旧 flatlaf-demo-1.4 残留的 ui-resourceName（其携带的 JSON 主题引用了
        // 3.7.2 已移除的 $ColorPalette.table 变量，会导致 IntelliJTheme.setup 解析失败）
        String resourceName = Db.getSetingValue("ui-resourceName");
        if (resourceName != null) {
            Db.removeSetingK("ui-resourceName");
        }
        String lafClassName = Db.getSetingValue("ui-lafClassName");
        if (lafClassName == null) {
            // 默认使用 FlatIntelliJLaf（现代化主题）
            lafClassName = FlatIntelliJLaf.class.getName();
            Db.updateSetingKV("ui-lafClassName", lafClassName);
        }
        try {
            // 统一使用 lafClassName 实例化主题（内置 FlatLaf + flatlaf-intellij-themes 3.7.2 类）
            Class<?> lafClass = Class.forName(lafClassName);
            FlatLaf laf = (FlatLaf) lafClass.getDeclaredConstructor().newInstance();
            FlatLaf.setup(laf);
        } catch (Throwable t) {
            // 降级到 FlatIntelliJLaf
            try {
                FlatLaf.setup(new FlatIntelliJLaf());
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * 全局默认值
     */
    private static void configureGlobalDefaults() {
        // macOS 菜单栏适配
        if (SystemInfo.isMacOS && System.getProperty("apple.laf.useScreenMenuBar") == null) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        // 统一圆角
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

        // 阴影
        UIManager.put("Popup.dropShadowBorderWidth", 4);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("OptionPane.buttonMinimumWidth", 80);

        // 统一边距 (FlatLaf 的 *Component*.padding 期望 Insets，不是 EmptyBorder)
        UIManager.put("Button.margin", new EmptyBorder(6, 14, 6, 14));
        UIManager.put("ToggleButton.margin", new EmptyBorder(6, 14, 6, 14));
        UIManager.put("ComboBox.padding", new java.awt.Insets(4, 8, 4, 8));
        UIManager.put("Spinner.padding", new java.awt.Insets(4, 8, 4, 8));
        UIManager.put("TextField.padding", new java.awt.Insets(4, 8, 4, 8));
        UIManager.put("FormattedTextField.padding", new java.awt.Insets(4, 8, 4, 8));
        UIManager.put("PasswordField.padding", new java.awt.Insets(4, 8, 4, 8));

        // 默认字体：优先使用用户在设置中选择的字体，降级到系统默认中文字体
        String userFontName = core.Db.getSetingValue("font-name");
        String userFontType = core.Db.getSetingValue("font-type");
        String userFontSize = core.Db.getSetingValue("font-size");
        int fontStyle = Font.PLAIN;
        int fontSize = 13;
        String fontName;
        if (userFontName != null && userFontSize != null) {
            // 使用用户设置的字体
            fontName = userFontName;
            try {
                fontSize = Integer.parseInt(userFontSize);
            } catch (NumberFormatException ignored) {
            }
            if (userFontType != null) {
                try {
                    fontStyle = Integer.parseInt(userFontType);
                } catch (NumberFormatException ignored) {
                }
            }
        } else {
            // 降级到系统默认中文字体
            fontName = SystemInfo.isWindows ? "Microsoft YaHei UI" : "Microsoft YaHei";
        }
        try {
            Font base = new Font(fontName, fontStyle, fontSize);
            UIManager.put("defaultFont", base);
            UIManager.put("Button.font", base);
            UIManager.put("Label.font", base);
            UIManager.put("Menu.font", base);
            UIManager.put("MenuItem.font", base);
            UIManager.put("Table.font", base);
            UIManager.put("TableHeader.font", base);
            UIManager.put("Tree.font", base);
            UIManager.put("List.font", base);
            UIManager.put("TextField.font", base);
            UIManager.put("PasswordField.font", base);
            UIManager.put("ComboBox.font", base);
            UIManager.put("CheckBox.font", base);
            UIManager.put("RadioButton.font", base);
            UIManager.put("TabbedPane.font", base);
            UIManager.put("TitledBorder.font", base);
        } catch (Throwable ignored) {
        }
    }

    /**
     * 菜单栏样式
     */
    private static void configureMenuBar() {
        UIManager.put("MenuBar.background", UIManager.getColor("Panel.background"));
        // 让菜单栏与主面板同色，去除视觉割裂
        try {
            Color accent = UIManager.getColor("Panel.background");
            if (accent != null) {
                UIManager.put("MenuBar.background", accent);
                UIManager.put("Menu.background", accent);
            }
        } catch (Throwable ignored) {
        }
    }

    /**
     * 表格样式
     */
    private static void configureTables() {
        // 关闭网格线，依赖交替行色与边距做区分
        UIManager.put("Table.showHorizontalLines", false);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.intercellSpacing", new java.awt.Dimension(0, 0));
        // 行高
        UIManager.put("Table.rowHeight", 30);
        // 交替行色
        UIManager.put("Table.alternateRowColor", true);
        try {
            Color base = UIManager.getColor("Table.background");
            if (base != null) {
                // 交替色：原色 + 7% 亮度
                Color alt = new Color(
                        Math.max(0, base.getRed() - 8),
                        Math.max(0, base.getGreen() - 8),
                        Math.max(0, base.getBlue() - 8)
                );
                UIManager.put("Table.alternateRowBackground", alt);
            }
        } catch (Throwable ignored) {
        }
        // 选择高亮：基于主题强调色做半透明，跟随主题深浅
        UIManager.put("Table.selectionInactiveBackground", withAlpha(accentColor(), 60));
        UIManager.put("Table.selectionInactiveForeground", UIManager.getColor("Table.foreground"));
        // 表头
        UIManager.put("TableHeader.height", 36);
        UIManager.put("TableHeader.cellBorder", BorderFactory.createEmptyBorder(6, 8, 6, 8));
    }

    /**
     * 树样式
     */
    private static void configureTrees() {
        UIManager.put("Tree.rowHeight", 28);
        UIManager.put("Tree.paintSelectionBorder", false);
        // 选中条：基于主题强调色半透明，跟随主题深浅
        UIManager.put("Tree.selectionBackground", withAlpha(accentColor(), 60));
        UIManager.put("Tree.selectionBorderColor", new Color(0, 0, 0, 0));
        UIManager.put("Tree.editorBorder", BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    /**
     * 列表样式
     */
    private static void configureLists() {
        UIManager.put("List.rowHeight", 28);
        UIManager.put("List.selectionInactiveBackground", withAlpha(accentColor(), 60));
    }

    /**
     * 文本组件
     */
    private static void configureTextComponents() {
        // TextArea / TextPane 关闭圆角以兼容代码高亮
        UIManager.put("TextArea.background", UIManager.getColor("EditorPane.background"));
    }

    /**
     * 按钮样式
     */
    private static void configureButtons() {
        try {
            // 默认按钮强调色
            Color accent = UIManager.getColor("Component.accentColor");
            if (accent == null) {
                accent = new Color(60, 130, 246);
            }
            UIManager.put("Button.default.background", accent);
            // 默认按钮前景：根据强调色亮度自动选黑/白，避免浅色强调色上白字看不清
            UIManager.put("Button.default.foreground", contrastForeground(accent));
            UIManager.put("Button.default.boldText", true);

            // 主按钮悬停态增强
            Color hover = new Color(
                    Math.max(0, accent.getRed() - 20),
                    Math.max(0, accent.getGreen() - 20),
                    Math.max(0, accent.getBlue() - 20)
            );
            UIManager.put("Button.default.hoverBackground", hover);
            UIManager.put("Button.focusedBackground", UIManager.getColor("Button.background"));
        } catch (Throwable ignored) {
        }
    }

    /**
     * 弹出菜单
     */
    private static void configurePopupMenu() {
        UIManager.put("PopupMenu.dropShadowPainted", true);
        Color pcb = UIManager.getColor("Component.borderColor");
        if (pcb == null) pcb = new Color(128, 128, 128, 80);
        UIManager.put("PopupMenu.border", BorderFactory.createCompoundBorder(
                new LineBorder(pcb, 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        // MenuItem 间距
        UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder(6, 12, 6, 12));
        UIManager.put("MenuItem.margin", new EmptyBorder(0, 0, 0, 0));
    }

    /**
     * 分割面板
     */
    private static void configureSplitPane() {
        UIManager.put("SplitPane.dividerSize", 6);
        UIManager.put("SplitPane.continuousLayout", true);
        UIManager.put("SplitPaneDivider.gripColor", new Color(180, 180, 180, 0));  // 隐藏 grip dot
        UIManager.put("SplitPaneDivider.draggingColor", withAlpha(accentColor(), 60));
    }

    /**
     * 文件选择器
     */
    private static void configureFileChooser() {
        UIManager.put("FileChooser.newFolderIcon", null);
        UIManager.put("FileChooser.homeFolderIcon", null);
        UIManager.put("FileChooser.upFolderIcon", null);
    }

    /**
     * 滚动面板
     */
    private static void configureScrollPane() {
        // 滚动条现代化
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
        UIManager.put("ScrollBar.allowsAbsolutePositioning", true);
        // 滚动面板边框
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
    }

    /**
     * Tab 面板
     */
    private static void configureTabbedPane() {
        // 移除默认边框，让 Tab 看起来更紧凑
        UIManager.put("TabbedPane.tabInsets", new java.awt.Insets(6, 12, 6, 12));
        UIManager.put("TabbedPane.tabAreaInsets", new java.awt.Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.contentBorderInsets", new java.awt.Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.selectedBackground", UIManager.getColor("Panel.background"));
        UIManager.put("TabbedPane.showContentSeparator", true);
        // 关闭粗选中边框
        UIManager.put("TabbedPane.focusColor", UIManager.getColor("TabbedPane.selectedBackground"));
        UIManager.put("TabbedPane.tabSeparatorsFullHeight", false);
    }

    /**
     * 工具方法：将十六进制字符串转为 Color
     */
    public static Color hex(String hex) {
        if (hex == null || hex.isEmpty()) {
            return Color.GRAY;
        }
        try {
            return Color.decode(hex.trim());
        } catch (NumberFormatException e) {
            return Color.GRAY;
        }
    }

    /** 取当前主题强调色（accent），回退到 List.selectionBackground 或固定蓝 */
    private static Color accentColor() {
        Color c = UIManager.getColor("Component.accentColor");
        if (c == null) c = UIManager.getColor("List.selectionBackground");
        if (c == null) c = new Color(60, 130, 246);
        return c;
    }

    /** 给颜色加透明度（alpha 0-255） */
    private static Color withAlpha(Color c, int alpha) {
        if (c == null) return new Color(60, 110, 180, alpha);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    /** 根据背景色亮度返回对比前景色（亮背景→黑字，暗背景→白字） */
    private static Color contrastForeground(Color bg) {
        if (bg == null) return Color.WHITE;
        double y = (0.299 * bg.getRed() + 0.587 * bg.getGreen() + 0.114 * bg.getBlue()) / 255.0;
        return y > 0.55 ? Color.BLACK : Color.WHITE;
    }

    /**
     * 工具方法：为深色背景生成柔和的交替色
     */
    public static Color darken(Color src, int amount) {
        return new Color(
                Math.max(0, src.getRed() - amount),
                Math.max(0, src.getGreen() - amount),
                Math.max(0, src.getBlue() - amount)
        );
    }

    /**
     * 工具方法：生成半透明叠加色（用于选择高亮）
     */
    public static Color overlay(Color src, Color overlay, int alpha) {
        return new Color(
                (src.getRed() * (255 - alpha) + overlay.getRed() * alpha) / 255,
                (src.getGreen() * (255 - alpha) + overlay.getGreen() * alpha) / 255,
                (src.getBlue() * (255 - alpha) + overlay.getBlue() * alpha) / 255
        );
    }

    /**
     * 创建带圆角与浅色细线的内嵌边框（用于卡片容器）
     */
    public static javax.swing.border.Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, new Color(128, 128, 128, 60)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );
    }
}
