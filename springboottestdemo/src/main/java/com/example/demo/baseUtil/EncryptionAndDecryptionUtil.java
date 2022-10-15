package com.example.demo.baseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加解密工具类
 *
 * @author chenxin
 * @date 2022/10/10 15:21
 */
public class EncryptionAndDecryptionUtil {
    private final static Logger logger = LoggerFactory.getLogger(EncryptionAndDecryptionUtil.class);

    /**
     * 清算中心加密key
     */
    public static final String QS_KEY = "qscenter!123";

    /**
     * 描述: 清算中心加密
     * 公司: www.tydic.com
     *
     * @author ningjianguo
     * @time 2019/5/23 14:25
     */
    public static String qsEncode(String content) {
        return aesEncode(QS_KEY, content);
    }

    /**
     * 描述: 清算中心解密
     * 公司: www.tydic.com
     *
     * @author ningjianguo
     * @time 2019/5/23 14:26
     */
    public static String qsDecode(String content) {
        logger.info("密文:{}", content);
        return aesDecode(QS_KEY, content);
    }

    /*
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String aesEncode(String encodeRules, String content) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encodeRules.getBytes());
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, secureRandom);
            //3.产生原始对称密钥
            SecretKey originalKey = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = originalKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byteEcode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byteAes = cipher.doFinal(byteEcode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String aesEncode = new String(new BASE64Encoder().encode(byteAes));
            //11.将字符串返回
            return aesEncode;
        } catch (NoSuchAlgorithmException e) {
            logger.error("调用加密方法NoSuchAlgorithmException：{}",e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error("调用加密方法NoSuchPaddingException()方法异常：{}",e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("调用加密方法InvalidKeyException()方法异常：{}",e.getMessage());
        } catch (IllegalBlockSizeException e) {
            logger.error("调用加密方法IllegalBlockSizeException()方法异常：{}",e.getMessage());
        } catch (BadPaddingException e) {
            logger.error("调用加密方法BadPaddingException()方法异常：{}",e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error("调用加密方法UnsupportedEncodingException()方法异常：{}",e.getMessage());
        }

        //如果有错就返加nulll
        return null;
    }

    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String aesDecode(String encodeRules, String content) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encodeRules.getBytes());
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, secureRandom);
            //3.产生原始对称密钥
            SecretKey originalKey = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = originalKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byteContent = new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte[] byteDecode = cipher.doFinal(byteContent);
            String aesDecode = new String(byteDecode, "utf-8");
            return aesDecode;
        } catch (NoSuchAlgorithmException e) {
            logger.error("解密NoSuchAlgorithmException()方法异常：{}",e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error("解密NoSuchPaddingException()方法异常：{}",e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("解密InvalidKeyException()方法异常：{}",e.getMessage());
        } catch (IOException e) {
            logger.error("解密IOException()方法异常：{}",e.getMessage());
        } catch (IllegalBlockSizeException e) {
            logger.error("解密IllegalBlockSizeException()方法异常：{}",e.getMessage());
        } catch (BadPaddingException e) {
            logger.error("解密BadPaddingException()方法异常：{}",e.getMessage());
        }

        //如果有错就返加nulll
        return null;
    }

    public static void main(String[] args) {
//        String encodeStr = qsEncode("");
//        System.out.println("加密串：" + encodeStr);

        String decodeStr = qsDecode("ae65b41b3a9a44f3:testgdhbGD234");
        System.out.println("解密串：" + decodeStr);
    }
}
