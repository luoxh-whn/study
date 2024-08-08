package SM2SM3;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;



public class SM2Utils

{
	 
  
	/**sm2曲线参数名称*/
    public static final String CRYPTO_NAME_SM2 = "sm2p256v1";
    /**sm2-签名id*/
    public static final String SIGN_ID = "1234567812345678";
    /**
     * 获取sm2密钥对
     * BC库使用的公钥=64个字节+1个字节（04标志位），BC库使用的私钥=32个字节
     * SM2秘钥的组成部分有 私钥D 、公钥X 、 公钥Y , 他们都可以用长度为64的16进制的HEX串表示，
     * <br/>SM2公钥并不是直接由X+Y表示 , 而是额外添加了一个头，当启用压缩时:公钥=有头+公钥X ，即省略了公钥Y的部分
     *
     * @param compressed 是否压缩公钥（加密解密都使用BC库才能使用压缩）
     * @return
     */
    public static  String[] getSm2Keys(boolean compressed) {
        //获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        //构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //1.创建密钥生成器
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        //2.初始化生成器,带上随机数
        try {
            keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        //3.生成密钥对
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        ECPublicKeyParameters publicKeyParameters = (ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic();
        ECPoint ecPoint = publicKeyParameters.getQ();
        // 把公钥放入map中,默认压缩公钥
        // 公钥前面的02或者03表示是压缩公钥,04表示未压缩公钥,04的时候,可以去掉前面的04
        String publicKey = Hex.toHexString(ecPoint.getEncoded(compressed));
        ECPrivateKeyParameters privateKeyParameters = (ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
        BigInteger intPrivateKey = privateKeyParameters.getD();
        // 把私钥放入map中
        String privateKey = intPrivateKey.toString(16);
        String[] KeyPairOfString = new String[2];
        KeyPairOfString[0] = publicKey;
        KeyPairOfString[1] = privateKey;
 
        return KeyPairOfString;
    }
    /**
     * SM2加密算法
     * @param publicKey 公钥
     * @param data 待加密的数据
     * @return 密文，BC库产生的密文带由04标识符，与非BC库对接时需要去掉开头的04
     */
    public static  String encrypt(String publicKey, String data){
        // 按国密排序标准加密
        return encrypt(publicKey, data, SM2EngineExtend.CIPHERMODE_NORM);
    }
 
    /**
     * SM2加密算法
     * @param publicKey 公钥
     * @param data 待加密的数据
     * @param cipherMode 密文排列方式0-C1C2C3；1-C1C3C2；
     * @return 密文，BC库产生的密文带由04标识符，与非BC库对接时需要去掉开头的04
     */
    public static  String encrypt(String publicKey, String data, int cipherMode) {
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        // 构造ECC算法参数，曲线方程、椭圆曲线G点、大整数N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);
 
        SM2EngineExtend sm2Engine = new SM2EngineExtend();
        // 设置sm2为加密模式
        sm2Engine.init(true, cipherMode, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));
 
        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes();
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Hex.toHexString(arrayOfBytes);
    }
 
    /**
     * SM2解密算法
     * @param privateKey    私钥
     * @param cipherData    密文数据
     * @return
     */
    public static   String decrypt(String privateKey, String cipherData) {
        // // 按国密排序标准解密
        return decrypt(privateKey, cipherData, SM2EngineExtend.CIPHERMODE_NORM);
    }
 
    /**
     * SM2解密算法
     * @param privateKey    私钥
     * @param cipherData    密文数据
     * @param cipherMode 密文排列方式0-C1C2C3；1-C1C3C2；
     * @return
     */
    public static  String decrypt(String privateKey, String cipherData, int cipherMode) {
        // 使用BC库加解密时密文以04开头，传入的密文前面没有04则补上
        if (!cipherData.startsWith("04")) {
            cipherData = "04" + cipherData;
        }
        byte[] cipherDataByte = Hex.decode(cipherData);
 
        //获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        //构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
 
        BigInteger privateKeyD = new BigInteger(privateKey, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);
 
        SM2EngineExtend sm2Engine = new SM2EngineExtend();
        // 设置sm2为解密模式
        sm2Engine.init(false, cipherMode, privateKeyParameters);
 
        String result = "";
        try {
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            return new String(arrayOfBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
 
    /**
     * 私钥签名
     *
     * @param data 未加密的密码明文
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static  byte[] signByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
        sig.initSign(privateKey);
        sig.update(data);
        byte[] ret = sig.sign();
        return ret;
    }
 
    /**
     * 公钥验签
     *
     * @param data 未加密的密码明文
     * @param publicKey
     * @param signature 签名
     * @return
     * @throws Exception
     */
    public static  boolean verifyByPublicKey(byte[] data, PublicKey publicKey, byte[] signature) throws Exception {
        Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
        sig.initVerify(publicKey);
        sig.update(data);
        boolean ret = sig.verify(signature);
        return ret;
    }

	public static void main(String[] args) throws Exception {
		
		 
		  String pwd = "123456 湖北宜昌开发区";
	 
		// 生成密钥对
        SM2EngineExtend  sm2ee =new SM2EngineExtend();
    	
		sm2ee.setDebug(false);
		
		SM2KeyPair keyPair = sm2ee.generateKeyPair();
			
		
        sm2ee.exportPublicKey(keyPair.getPublicKey(), "D:/publickey.pem");
        sm2ee.exportPrivateKey(keyPair.getPrivateKey(), "D:/privatekey.pem");
		System.out.println("-----------------公钥加密与解密-----------------");
		 // 公钥前面的02或者03表示是压缩公钥,04表示未压缩公钥,04的时候,可以去掉前面的04
		ECPoint publicKey = sm2ee.importPublicKey("D:/publickey.pem");
		BigInteger privateKey = sm2ee.importPrivateKey("D:/privatekey.pem");
		/*测试1*/
		
	  
	
	    
    	
		String puKey1 =  Util.byteToHex(publicKey.getEncoded(true));
		String priKey1 = Util.byteToHex(privateKey.toByteArray());
		System.out.println("pubKey1="+puKey1);
		System.out.println("priKey1="+priKey1);
		  // 国密加密
        String encrypt = SM2Utils.encrypt(puKey1, pwd+"罗学华",0);
        System.out.println("export国密0加密后密码:"+encrypt);
  
         // 国密解密
         String decrypt = SM2Utils.decrypt(priKey1, encrypt,0);
         System.out.println("export国密解密后密码:"+decrypt);
  
         // BC库加密
         String encrypt1 = SM2Utils.encrypt(puKey1, pwd+"当阳", 1);
         System.out.println("exportBC加密1："+encrypt1);
         String decrypt1 = SM2Utils.decrypt(priKey1, encrypt1, 1);
         System.out.println("exportBC解密："+decrypt1);
  
    	
    	
    	
    	 System.out.println("未加密密码："+pwd);
         String[] sm2Keys = SM2Utils.getSm2Keys(false);
         String pubKey = sm2Keys[0];
         System.out.println("公钥："+pubKey);
         String priKey = sm2Keys[1];
         System.out.println("私钥："+priKey);
  
         // 国密加密
        encrypt = SM2Utils.encrypt(pubKey, pwd);
        System.out.println("国密加密后密码0:"+encrypt);
  
         // 国密解密
         decrypt = SM2Utils.decrypt(priKey, encrypt);
         System.out.println("国密解密后密码0:"+decrypt);
  
         // BC库加密
          encrypt1 = SM2Utils.encrypt(pubKey, pwd, 1);
         System.out.println("BC加密："+encrypt1);
          decrypt1 = SM2Utils.decrypt(priKey, encrypt1, 1);
         System.out.println("BC解密："+decrypt1);
        
         
	}
}