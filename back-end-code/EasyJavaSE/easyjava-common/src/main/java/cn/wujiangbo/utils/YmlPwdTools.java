package cn.wujiangbo.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * <p>yml中密码的加密和解密</p>
 *
 */
public class YmlPwdTools {

    private static String PWD = "easyjava#KLOp*AndSe";//加密的密钥，随便自己填写，很重要千万不要告诉别人
    private static String ALGORITHM = "PBEWithMD5AndDES";//加密算法

    public static void main(String[] args){
        getYmlPwd("123456");
        getTextPwd("mILmC70DuJVQYI778mZ7xg==");
    }

    //解密
    public static void getTextPwd(String encryptedText){
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm(ALGORITHM);
        config.setPassword(PWD);
        standardPBEStringEncryptor.setConfig(config);
        //指定需要解密的密文
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println("解密后的明文=" + plainText);
    }

    //加密
    public static void getYmlPwd(String plainText){
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        // 加密的算法，这个算法是默认的
        config.setAlgorithm(ALGORITHM);
        // 加密的密钥，随便自己填写，很重要千万不要告诉别人
        config.setPassword(PWD);
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println("加密后的密文=" + encryptedText);
    }
}
