package util;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.ToolTipManager;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class UiFunction {
   public static String setSyntaxEditingStyle(RSyntaxTextArea textArea, String fileName) {
      String style = null;
      fileName = fileName.toLowerCase();
      if (fileName.endsWith(".as")) {
         style = "text/actionscript";
      } else if (fileName.endsWith(".asm")) {
         style = "text/asm";
      } else if (fileName.endsWith(".c")) {
         style = "text/c";
      } else if (fileName.endsWith(".clj")) {
         style = "text/clojure";
      } else if (!fileName.endsWith(".cpp") && !fileName.endsWith("cc")) {
         if (!fileName.endsWith(".cs") && !fileName.endsWith(".aspx") && !fileName.endsWith(".ashx") && !fileName.endsWith(".asmx")) {
            if (fileName.endsWith(".css")) {
               style = "text/css";
            } else if (fileName.endsWith(".d")) {
               style = "text/d";
            } else if (fileName.equals("dockfile")) {
               style = "text/dockerfile";
            } else if (fileName.endsWith(".dart")) {
               style = "text/dart";
            } else if (fileName.endsWith(".dpr") | fileName.endsWith(".dfm") | fileName.endsWith(".pas")) {
               style = "text/delphi";
            } else if (fileName.endsWith(".dtd")) {
               style = "text/dtd";
            } else if (fileName.endsWith(".f") | fileName.endsWith(".f90")) {
               style = "text/fortran";
            } else if (fileName.endsWith(".groovy")) {
               style = "text/groovy";
            } else if (fileName.equals("hosts")) {
               style = "text/hosts";
            } else if (fileName.equals(".htaccess")) {
               style = "text/htaccess";
            } else if (fileName.endsWith(".htm") | fileName.endsWith(".html")) {
               style = "text/html";
            } else if (fileName.endsWith(".ini")) {
               style = "text/ini";
            } else if (fileName.endsWith(".java") | fileName.endsWith(".class")) {
               style = "text/java";
            } else if (fileName.endsWith(".js")) {
               style = "text/javascript";
            } else if (fileName.endsWith(".json")) {
               style = "text/json";
            } else if (fileName.equals(".jshintrc")) {
               style = "text/jshintrc";
            } else if (!fileName.endsWith(".jsp") && !fileName.endsWith(".jspx")) {
               if (fileName.endsWith(".tex")) {
                  style = "text/latex";
               } else if (fileName.endsWith(".less")) {
                  style = "text/less";
               } else if (fileName.endsWith(".lsp")) {
                  style = "text/lisp";
               } else if (fileName.endsWith(".lua")) {
                  style = "text/lua";
               } else if (fileName.equals("makefile")) {
                  style = "text/makefile";
               } else if (fileName.endsWith(".mxml")) {
                  style = "text/mxml";
               } else if (fileName.endsWith(".nsi")) {
                  style = "text/nsis";
               } else if (fileName.endsWith(".pl") | fileName.endsWith(".perl")) {
                  style = "text/perl";
               } else if (!fileName.endsWith(".php") && !fileName.endsWith(".phtml") && !fileName.endsWith(".php4") && !fileName.endsWith(".php3") && !fileName.endsWith(".php5")) {
                  if (fileName.endsWith(".properties")) {
                     style = "text/properties";
                  } else if (fileName.endsWith(".py") | fileName.endsWith(".pyc")) {
                     style = "text/python";
                  } else if (fileName.endsWith(".rb") | fileName.endsWith(".rwb")) {
                     style = "text/ruby";
                  } else if (fileName.endsWith(".sas")) {
                     style = "text/sas";
                  } else if (fileName.endsWith(".scala")) {
                     style = "text/scala";
                  } else if (fileName.endsWith(".sql")) {
                     style = "text/sql";
                  } else if (fileName.endsWith(".tcl")) {
                     style = "text/tcl";
                  } else if (fileName.endsWith(".ts") | fileName.endsWith(".tsx")) {
                     style = "text/typescript";
                  } else if (fileName.endsWith(".sh")) {
                     style = "text/unix";
                  } else if (fileName.endsWith(".vb")) {
                     style = "text/vb";
                  } else if (fileName.endsWith(".bat")) {
                     style = "text/bat";
                  } else if (fileName.endsWith(".xml")) {
                     style = "text/xml";
                  } else if (fileName.endsWith(".yaml")) {
                     style = "text/yaml";
                  } else if (fileName.endsWith(".go")) {
                     style = "text/golang";
                  } else if (fileName.endsWith(".asp")) {
                     style = "text/javascript";
                  }
               } else {
                  style = "text/php";
               }
            } else {
               style = "text/jsp";
            }
         } else {
            style = "text/cs";
         }
      } else {
         style = "text/cpp";
      }

      if (style == null) {
         style = "text/plain";
      } else {
         LanguageSupportFactory.get().register(textArea);
         textArea.setCaretPosition(0);
         textArea.requestFocusInWindow();
         textArea.setMarkOccurrences(true);
         textArea.setCodeFoldingEnabled(true);
         textArea.setTabsEmulated(true);
         textArea.setTabSize(3);
         textArea.setUseFocusableTips(false);
         ToolTipManager.sharedInstance().registerComponent(textArea);
      }

      textArea.setSyntaxEditingStyle(style);
      // TODO: 2022/5/16  
      //textArea.registerReplaceDialog();
      //textArea.registerGoToDialog();
      return style;
   }

   public static Frame getParentFrame(Container container) {
      while(true) {
         if ((container = container.getParent()) != null) {
            if (!Frame.class.isAssignableFrom(container.getClass())) {
               continue;
            }

            return (Frame)container;
         }

         return null;
      }
   }

   public static Dialog getParentDialog(Container container) {
      while(true) {
         if ((container = container.getParent()) != null) {
            if (!Dialog.class.isAssignableFrom(container.getClass())) {
               continue;
            }

            return (Dialog)container;
         }

         return null;
      }
   }

   public static Window getParentWindow(Container container) {
      while(true) {
         if ((container = container.getParent()) != null) {
            if (!Window.class.isAssignableFrom(container.getClass())) {
               continue;
            }

            return (Window)container;
         }

         return null;
      }
   }

   public static int getFontType(String fontType) {
      return parseFontStyle(fontType);
   }

   /**
    * 获取系统中所有可用字体家族名（去重 + 排序）
    * 使用 family 而非 fontName，避免同一字体的多个变体重复出现
    * @return 排序后的字体家族名数组
    */
   public static String[] getAllFontName() {
      GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] fontNames = e.getAvailableFontFamilyNames();
      java.util.Arrays.sort(fontNames, String.CASE_INSENSITIVE_ORDER);
      return fontNames;
   }

   /**
    * 获取所有字体样式类型
    * @return 字体样式名称数组
    */
   public static String[] getAllFontType() {
      ArrayList<String> arrayList = new ArrayList();
      arrayList.add("PLAIN");
      arrayList.add("BOLD");
      arrayList.add("ITALIC");
      arrayList.add("BOLD|ITALIC");
      return (String[])arrayList.toArray(new String[0]);
   }

   /**
    * 获取所有可选字号（8-72）
    * @return 字号字符串数组
    */
   public static String[] getAllFontSize() {
      ArrayList<String> arrayList = new ArrayList();

      for(int i = 8; i <= 72; ++i) {
         arrayList.add(Integer.toString(i));
      }

      return (String[])arrayList.toArray(new String[0]);
   }

   /**
    * 将字体样式字符串解析为 Font 样式常量
    * @param styleName 样式名：PLAIN / BOLD / ITALIC / BOLD|ITALIC
    * @return Font.PLAIN / Font.BOLD / Font.ITALIC / Font.BOLD|Font.ITALIC
    */
   public static int parseFontStyle(String styleName) {
      if (styleName == null || styleName.isEmpty()) {
         return Font.PLAIN;
      }
      String upper = styleName.toUpperCase();
      boolean bold = upper.contains("BOLD");
      boolean italic = upper.contains("ITALIC");
      if (bold && italic) {
         return Font.BOLD | Font.ITALIC;
      } else if (bold) {
         return Font.BOLD;
      } else if (italic) {
         return Font.ITALIC;
      } else {
         return Font.PLAIN;
      }
   }

   /**
    * 根据 Font 样式常量返回可读字符串
    * @param style Font 样式常量
    * @return PLAIN / BOLD / ITALIC / BOLD|ITALIC
    */
   public static String getFontType(Font font) {
      int style = font.getStyle();
      if ((style & Font.BOLD) != 0 && (style & Font.ITALIC) != 0) {
         return "BOLD|ITALIC";
      } else if ((style & Font.BOLD) != 0) {
         return "BOLD";
      } else if ((style & Font.ITALIC) != 0) {
         return "ITALIC";
      } else {
         return "PLAIN";
      }
   }
}
