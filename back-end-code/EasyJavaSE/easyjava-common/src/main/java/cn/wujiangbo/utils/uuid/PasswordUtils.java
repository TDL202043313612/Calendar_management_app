package cn.wujiangbo.utils.uuid;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
//import sun.misc.BASE64Decoder;
//import java.util.Base64;

import java.io.IOException;

/**
 * 密码工具类
 *
 */
public class PasswordUtils {

    static RSA rsa = new RSA();

    public static void main(String[] args) throws IOException {
        System.out.println(encryptPassword("admin123456"));
        System.out.println(decryptPassword("YWRtaW4xMjM0NTY="));
    }

    /**
     * 加密
     * @param password
     * @return
     */
    public static String encryptPassword(String password){
        //公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(password, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
        return Base64.encode(decrypt);
    }

    /**
     * 解密
     * @param password
     * @return
     */
    public static String decryptPassword(String password){
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        String str = "";
        try {
            str = new String(decoder.decode(password), CharsetUtil.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}