package core.ui.component.dialog;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes.FlatIJLookAndFeelInfo;
import core.ui.ModernUITheme;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主题选择面板 —— 基于 flatlaf 3.7.2 原生 API。
 * <p>
 * 使用 {@link JComboBox} 而非 JList 作为选择控件，避免 FlatLaf 的 FlatListUI 在
 * 模态 JDialog 首次显示时 selection listener 时序问题（BasicListUI$Handler 引用已卸载的
 * UI 实例导致 this$0.list 为 null 抛 NPE）。ComboBox.padding 已由 ModernUITheme 兜底。
 * <p>
 * 列表每一项展示：色块图标（直观区分深/浅色）+ 友好主题名 + [深色/浅色] 标记。
 */
public class ThemesPanel extends JPanel {

   /** 主题信息（统一使用 lafClassName，不再使用 resourceName） */
   public static final class ThemeInfo {
      public final String name;
      public final String lafClassName;
      public final boolean dark;

      public ThemeInfo(String name, String lafClassName, boolean dark) {
         this.name = name;
         this.lafClassName = lafClassName;
         this.dark = dark;
      }
   }

   private final DefaultComboBoxModel<ThemeInfo> model = new DefaultComboBoxModel<>();
   private final JComboBox<ThemeInfo> themeCombo = new JComboBox<>(model);
   private boolean firePreview = true;

   public ThemesPanel() {
      super(new BorderLayout());
      // 顶部说明
      JLabel tip = new JLabel("  选择主题可实时预览；确定后点下方「修改」保存，重启程序后永久生效");
      tip.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
      tip.setFont(tip.getFont().deriveFont(11f));
      add(tip, BorderLayout.NORTH);

      // 1. 内置 FlatLaf 主题
      addTheme("Flat Light", FlatLightLaf.class.getName(), false);
      addTheme("Flat Dark", FlatDarkLaf.class.getName(), true);
      addTheme("Flat IntelliJ", FlatIntelliJLaf.class.getName(), false);
      addTheme("Flat Darcula", FlatDarculaLaf.class.getName(), true);

      // 2. flatlaf-intellij-themes 3.7.2 提供的所有 IJ 主题
      try {
         FlatIJLookAndFeelInfo[] infos = FlatAllIJThemes.INFOS;
         if (infos != null) {
            for (FlatIJLookAndFeelInfo info : infos) {
               addTheme(info.getName(), info.getClassName(), info.isDark());
            }
         }
      } catch (Throwable t) {
         // 忽略，至少保留内置主题
      }

      themeCombo.setRenderer(new ThemeCellRenderer());
      themeCombo.setMaximumRowCount(24);
      // 选中当前 LAF 对应的主题（构造期间不触发实时预览）
      firePreview = false;
      selectCurrent();
      firePreview = true;
      // 实时预览（3.7.2 原生 API，安全）
      themeCombo.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!firePreview) {
               return;
            }
            Object item = themeCombo.getSelectedItem();
            if (item instanceof ThemeInfo) {
               ThemeInfo info = (ThemeInfo) item;
               if (info.lafClassName != null) {
                  applyTheme(info);
               }
            }
         }
      });
      JPanel top = new JPanel(new BorderLayout());
      top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      top.add(themeCombo, BorderLayout.NORTH);
      add(top, BorderLayout.CENTER);
   }

   private void addTheme(String name, String lafClassName, boolean dark) {
      model.addElement(new ThemeInfo(name, lafClassName, dark));
   }

   /** 把原始主题名转为更友好的显示名（去掉冗余的 "Flat " 前缀） */
   private static String friendlyName(String name) {
      if (name == null) {
         return "";
      }
      if (name.startsWith("Flat ")) {
         return name.substring(5);
      }
      return name;
   }

   private void selectCurrent() {
      String current = null;
      try {
         current = UIManager.getLookAndFeel().getClass().getName();
      } catch (Throwable ignore) {
      }
      if (current == null) {
         current = FlatIntelliJLaf.class.getName();
      }
      for (int i = 0; i < model.getSize(); i++) {
         ThemeInfo info = model.getElementAt(i);
         if (info.lafClassName != null && current.equals(info.lafClassName)) {
            themeCombo.setSelectedIndex(i);
            return;
         }
      }
      // 回退：选中第一个真实主题项
      for (int i = 0; i < model.getSize(); i++) {
         if (model.getElementAt(i).lafClassName != null) {
            themeCombo.setSelectedIndex(i);
            return;
         }
      }
   }

   /** 返回当前选中的主题信息 */
   public ThemeInfo getSelected() {
      Object item = themeCombo.getSelectedItem();
      if (item instanceof ThemeInfo) {
         ThemeInfo info = (ThemeInfo) item;
         return info.lafClassName != null ? info : null;
      }
      return null;
   }

   /** 实时预览：用 3.7.2 原生 API 安装主题并刷新所在窗口 */
   private void applyTheme(ThemeInfo info) {
      try {
         Class<?> cls = Class.forName(info.lafClassName);
         FlatLaf laf = (FlatLaf) cls.getDeclaredConstructor().newInstance();
         FlatLaf.setup(laf);
         // 重新应用 ModernUITheme 颜色配置，让选择高亮/边框/按钮强调色跟随新主题深浅
         ModernUITheme.configureAll();
         // 刷新主窗口与当前对话框，让预览覆盖所有已打开窗口（菜单栏/按钮等）
         SwingUtilities.invokeLater(() -> {
            try {
               core.ui.MainActivity main = core.ui.MainActivity.getFrame();
               if (main != null) {
                  SwingUtilities.updateComponentTreeUI(main);
               }
               Window w = SwingUtilities.getWindowAncestor(ThemesPanel.this);
               if (w != null) {
                  SwingUtilities.updateComponentTreeUI(w);
               }
            } catch (Throwable ignore) {
            }
         });
      } catch (Throwable t) {
         // 预览失败静默忽略，不影响用户选择与保存
      }
   }

   // ==================== 下拉渲染器 ====================

   /** 下拉项渲染器：色块 + 友好名 + 深/浅标记 */
   private static class ThemeCellRenderer extends DefaultListCellRenderer {
      private final Icon lightIcon = new ColorIcon(new Color(245, 246, 247), new Color(60, 110, 180));
      private final Icon darkIcon = new ColorIcon(new Color(45, 46, 49), new Color(100, 150, 220));

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
         super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         if (!(value instanceof ThemeInfo)) {
            return this;
         }
         ThemeInfo info = (ThemeInfo) value;
         setText(friendlyName(info.name) + "    " + (info.dark ? "[深色]" : "[浅色]"));
         setIcon(info.dark ? darkIcon : lightIcon);
         setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
         setIconTextGap(8);
         return this;
      }
   }

   /** 圆角色块图标：底色代表深/浅，底部一条强调色提示主色调 */
   private static class ColorIcon implements Icon {
      private final Color bg;
      private final Color accent;
      private static final int SIZE = 16;

      ColorIcon(Color bg, Color accent) {
         this.bg = bg;
         this.accent = accent;
      }

      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
         Graphics2D g2 = (Graphics2D) g.create();
         try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(x, y, SIZE, SIZE, 3, 3);
            g2.setColor(new Color(128, 128, 128, 120));
            g2.drawRoundRect(x, y, SIZE, SIZE, 3, 3);
            g2.setColor(accent);
            g2.fillRoundRect(x + 3, y + SIZE - 6, SIZE - 6, 3, 2, 2);
         } finally {
            g2.dispose();
         }
      }

      @Override
      public int getIconWidth() {
         return SIZE;
      }

      @Override
      public int getIconHeight() {
         return SIZE;
      }
   }
}
