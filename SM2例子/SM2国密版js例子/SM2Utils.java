package Test;


import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.encoders.Base64;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * @ClassName SM2Utils
 * @Description SM2算法工具类
 * @Author msx
 * @Date 20220618
 * @Version 1.0
 */
public class SM2Utils {

    /**
     * @Description 生成秘钥对
     * @Author msx
     * @return KeyPair
     */
    public static KeyPair createECKeyPair() {
        //使用标准名称创建EC参数生成的参数规范
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");

        // 获取一个椭圆曲线类型的密钥对生成器
        final KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
            // 使用SM2算法域参数集初始化密钥生成器（默认使用以最高优先级安装的提供者的 SecureRandom 的实现作为随机源）
            // kpg.initialize(sm2Spec);

            // 使用SM2的算法域参数集和指定的随机源初始化密钥生成器
            kpg.initialize(sm2Spec, new SecureRandom());

            // 通过密钥生成器生成密钥对
            return kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description 公钥加密
     * @Author msx
     * @param publicKeyHex SM2十六进制公钥
     * @param data         明文数据
     * @return String
     */
    public static String encrypt(String publicKeyHex, String data) {
        return encrypt(getECPublicKeyByPublicKeyHex(publicKeyHex), data, 1);
    }

    /**
     * @Description 公钥加密
     * @Author msx
     * @param publicKey SM2公钥
     * @param data      明文数据
     * @param modeType  加密模式
     * @return String
     */
    public static String encrypt(BCECPublicKey publicKey, String data, int modeType) {
        //加密模式
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        if (modeType != 1) {
            mode = SM2Engine.Mode.C1C2C3;
        }
        //通过公钥对象获取公钥的基本域参数。
        ECParameterSpec ecParameterSpec = publicKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        //通过公钥值和公钥基本参数创建公钥参数对象
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
        //根据加密模式实例化SM2公钥加密引擎
        SM2Engine sm2Engine = new SM2Engine(mode);
        //初始化加密引擎
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes = null;
        try {
            //将明文字符串转换为指定编码的字节串
            byte[] in = data.getBytes("utf-8");
            //通过加密引擎对字节数串行加密
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            System.out.println("SM2加密时出现异常:" + e.getMessage());
            e.printStackTrace();
        }
        //将加密后的字节串转换为十六进制字符串
        return Hex.toHexString(arrayOfBytes);
    }

    /**
     * @Description 私钥解密
     * @Author msx
     * @param privateKeyHex SM2十六进制私钥
     * @param cipherData    密文数据
     * @return String
     */
    public static String decrypt(String privateKeyHex, String cipherData) {
        return decrypt(getBCECPrivateKeyByPrivateKeyHex(privateKeyHex), cipherData, 1);
    }
    /**
     * @Description 私钥解密
     * @Author msx
     * @param privateKeyHex SM2十六进制私钥
     * @param cipherData    密文数据
     * @return String
     */
    public static String decrypt(String privateKeyHex, String cipherData,int modeType) {
        return decrypt(getBCECPrivateKeyByPrivateKeyHex(privateKeyHex), cipherData, modeType);
    }

    /**
     * @Description 私钥解密
     * @Author msx
     * @param privateKeyHex SM私钥
     * @param cipherData 密文数据
     * @param modeType   解密模式
     * @return
     */
    public static String decrypt(BCECPrivateKey privateKey, String cipherData, int modeType) {
        //解密模式
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        if (modeType != 1)
            mode = SM2Engine.Mode.C1C2C3;
        //将十六进制字符串密文转换为字节数组（需要与加密一致，加密是：加密后的字节数组转换为了十六进制字符串）
        byte[] cipherDataByte = Hex.decode(cipherData);
        //通过私钥对象获取私钥的基本域参数。
        ECParameterSpec ecParameterSpec = privateKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        //通过私钥值和私钥钥基本参数创建私钥参数对象
        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(),
                ecDomainParameters);
        //通过解密模式创建解密引擎并初始化
        SM2Engine sm2Engine = new SM2Engine(mode);
        sm2Engine.init(false, ecPrivateKeyParameters);
        String result = null;
        try {
            //通过解密引擎对密文字节串进行解密
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            //将解密后的字节串转换为utf8字符编码的字符串（需要与明文加密时字符串转换成字节串所指定的字符编码保持一致）
            result = new String(arrayOfBytes, "utf-8");
        } catch (Exception e) {
            System.out.println("SM2解密时出现异常" + e.getMessage());
        }
        return result;
    }
    //椭圆曲线ECParameters ASN.1 结构
    private static X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
    //椭圆曲线公钥或私钥的基本域参数。
    private static ECParameterSpec ecDomainParameters = new ECParameterSpec(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

    /**
     * @Description 公钥字符串转换为 BCECPublicKey 公钥对象
     * @Author msx
     * @param pubKeyHex 64字节十六进制公钥字符串(如果公钥字符串为65字节首个字节为0x04：表示该公钥为非压缩格式，操作时需要删除)
     * @return BCECPublicKey SM2公钥对象
     */
    public static BCECPublicKey getECPublicKeyByPublicKeyHex(String pubKeyHex) {
        //截取64字节有效的SM2公钥（如果公钥首个字节为0x04）
        if (pubKeyHex.length() > 128) {
            pubKeyHex = pubKeyHex.substring(pubKeyHex.length() - 128);
        }
        //将公钥拆分为x,y分量（各32字节）
        String stringX = pubKeyHex.substring(0, 64);
        String stringY = pubKeyHex.substring(stringX.length());
        //将公钥x、y分量转换为BigInteger类型
        BigInteger x = new BigInteger(stringX, 16);
        BigInteger y = new BigInteger(stringY, 16);
        //通过公钥x、y分量创建椭圆曲线公钥规范
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(x9ECParameters.getCurve().createPoint(x, y), ecDomainParameters);
        //通过椭圆曲线公钥规范，创建出椭圆曲线公钥对象（可用于SM2加密及验签）
        return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

    /**
     * @Description 私钥字符串转换为 BCECPrivateKey 私钥对象
     * @Author msx
     * @param privateKeyHex 32字节十六进制私钥字符串
     * @return BCECPrivateKey SM2私钥对象
     */
    public static BCECPrivateKey getBCECPrivateKeyByPrivateKeyHex(String privateKeyHex) {
        //将十六进制私钥字符串转换为BigInteger对象
        BigInteger d = new BigInteger(privateKeyHex, 16);
        //通过私钥和私钥域参数集创建椭圆曲线私钥规范
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(d, ecDomainParameters);
        //通过椭圆曲线私钥规范，创建出椭圆曲线私钥对象（可用于SM2解密和签名）
        return new BCECPrivateKey("EC", ecPrivateKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

    public static void main(String[] args) {
        String publicKeyHex = null;
        String privateKeyHex = null;
        KeyPair keyPair = createECKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        if (publicKey instanceof BCECPublicKey) {
            //获取65字节非压缩缩的十六进制公钥串(0x04)
            publicKeyHex = Hex.toHexString(((BCECPublicKey) publicKey).getQ().getEncoded(false));
            System.out.println("---->SM2公钥：" + publicKeyHex);
        }
        PrivateKey privateKey = keyPair.getPrivate();
        if (privateKey instanceof BCECPrivateKey) {
            //获取32字节十六进制私钥串
            privateKeyHex = ((BCECPrivateKey) privateKey).getD().toString(16);
            System.out.println("---->SM2私钥：" + privateKeyHex);
        }

        /**
         * 公钥加密
         */
        String data = "=========待加密数据=========";

        //将十六进制公钥串转换为 BCECPublicKey 公钥对象
        String encryptData = encrypt(publicKeyHex, data);
        System.out.println("---->加密结果：" + encryptData);

        /**
         * 私钥解密
         */
        //将十六进制私钥串转换为 BCECPrivateKey 私钥对象
        data = decrypt(privateKeyHex, encryptData);
        System.out.println("---->解密结果：" + data);
        
        //--->SM2公钥：04339149dbdce1c3d0884df14d4d45e41bc95b86f14b5e5da1f4ef8a7604eaa52230096cae14fc6c1bb7805adcf564d23c77cd72d6c27547dc342e35751b3fd7cb
        //---->SM2私钥：d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193
        //---->加密结果：049153482f8c25dd59118d9cba3a2daa4a1603f7411fb4cf7fbbc4455ec9df83ada7f3c3c70bf91cc2920719365853dbe6befc0b1f7a70b5813c73bb2ced988e54eb4aeb7b862e35355595db6bf3da539cd68f086347904af1fddc04ea40e6d9136b13b71661f91c7b429cb01cd41ab2e62192425e1a60e5b41baef24cd41d3082ba
        //---->解密结果：=========待加密数据=========

        encryptData="04d2ae2aaa753118b069ca356e733015558a0b511d651437122d7c25abe6bcf95d3838411731d3bb85531ca3043e1f94933f82324dbba23b0f6a450303e98297485b111e185debc02adc43d9ceb33d2549442dcac53a1fbcaa1c21d1b747aab1153f23e566f760b60203da7662cb18f9533dec91614660e1ce11961f26d9e873b73c1aa28ae474bd8a29815ee0d5dcf4644c39918f461d5d5a9aea992a";
        privateKeyHex="d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
        System.out.println("//--->SM2公钥：04339149dbdce1c3d0884df14d4d45e41bc95b86f14b5e5da1f4ef8a7604eaa52230096cae14fc6c1bb7805adcf564d23c77cd72d6c27547dc342e35751b3fd7cb\n//---->SM2私钥：d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193");
        data = decrypt(privateKeyHex, encryptData,0);
        System.out.println("解码模式C1C3C2:0"+"待解密数据："+ encryptData);
       
        String data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,0).getBytes()));
        System.out.println("---->解密结果：" + data);
        System.out.println("---->解密结果base64解码：" + data1.toString());
        encryptData="04dc3c2bfe52e4b36c18ffd48475b5fb4e0b8247be70c70d15dce7f94c8beb76ac201197c563d8cfcd5b59d10465b05e1828b40d6b62d5f8065099f47b13e9ec7e8a2015427435936a6307b1e76a1d18011a6ec773ac0d124323d9d2f34dbc17f8f2eac1958995e5189b15da5242acef31339687b8fb5fc420b045c6996b9be4150d1e0cebfc3cd8f637646fef57ba3f4cf5209a6fb4db8a4204fa9533";
        privateKeyHex="d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
        
        System.out.println("解码模式C1C2C3=0表示"+"待解密数据："+ encryptData);
        data = decrypt(privateKeyHex, encryptData,0);
        data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,0).getBytes()));
        System.out.println("---->解密结果：" + data);
        System.out.println("---->解密结果base64解码：" + data1);
        encryptData="04c07e53a77f33c4d25cce550316a24260bab8b94962453fbf8a550f524ad33662512ce1c598d74653fbf794c9bbbe7bd8d71fc3df548a6ef501f6de42e7426ec00f044cd37739252530f9b9d8198ac06ccf4fb125e81830f34c32d9d83e8b5bfbcc287ff303b660f3eea1b9cae67275c274afcd833b44df8ec3d6348fafd01553340cfeaa4e3c0bd511fee96f8bb400e3ea95b9478ff50af69f872dc9594bb1fa7587f8ff911a5ad507b3ea64671e8894eb021639";
        System.out.println("解码模式C1C3C2=1表示"+"待解密数据："+ encryptData);
        data = decrypt(privateKeyHex, encryptData,1);
        data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,1).getBytes()));
        System.out.println("---->解密结果：" + data);
        System.out.println("---->解密结果base64解码：" + data1);
        
        encryptData="04702359cfe52f177307aa70631a36fc31005fe1217f6861a67dc4fe9dff66858703ad0c7aeb6e7a1678d5e88fe2ced731ebff0de059e03ef5b00074fd26c5da736a4db1688cdabe45e4a4c9c2e1b4d5266dc1909a09f9a4e6f37f2072e516348e4d9072339b5f9339d2b200953b0f783e5f2178a4d60b25c4545fe3348ad3c0a774a7de8b862bd3a92d90ab3df48fc5636ff20e1b1a9564266c6106b579f325fb2f8fa31b83e1fe39fa7c38c7";
        System.out.println("解码模式C1C3C2=1表示"+"待解密数据："+ encryptData);
        data = decrypt(privateKeyHex, encryptData,1);
        data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,1).getBytes()));
        System.out.println("---->解密结果：" + data);
        System.out.println("---->解密结果base64解码：" + data1);
    }
}


