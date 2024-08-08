package Test1;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class Sm2lib {

	public static void main(String[] args) {
		
		
		//私钥:8cd385a72cb6e6e5b726b7b00a4149a8ff2b820d6226515f0edc7db31b4b20bb
//		公钥:04321ce3dc8b6c271cfc30d215ab15aab05fa23db68c3a469cf27a66ce483db54291794cb71ea6414706d931027c141f5c584bf57ab9eb9d9d0532301495b9220a

		// TODO Auto-generated method stub
		/*X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
		ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
		try {
			keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();

		//私钥，16进制格式，自己保存，格式如a2081b5b81fbea0b6b973a3ab6dbbbc65b1164488bf22d8ae2ff0b8260f64853
		BigInteger privatekey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate()).getD();
		String privateKeyHex = privatekey.toString(16);
		System.out.println("私钥:"+privateKeyHex);

		//公钥，16进制格式，发给前端，格式如04813d4d97ad31bd9d18d785f337f683233099d5abed09cb397152d50ac28cc0ba43711960e811d90453db5f5a9518d660858a8d0c57e359a8bf83427760ebcbba
		ECPoint ecPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
		byte[] ddd = ecPoint.getEncoded(false);
		String publicKeyHex = Hex.toHexString(ddd);
		byte[] ppp = ByteUtils.fromHexString(publicKeyHex);
		System.out.println("公钥:"+publicKeyHex);*/
		decode();
		encode();
		test();
	}

	public static void decode(){
		String cipherData = "04d7f6f64545b8cc9426e1599418cb69f441de0f0a1952638604febe1b95bbe37e2c403725b6cba8df2092ce926133f0b73ebe78c7d46dfa2065dee675ea5dc4a76627b4b2ff06c81ab89e2101851358d2e1dd59b1cf7ad82e2d50121051f2cc2149d5182ccf05f4498540";
		byte[] cipherDataByte = Hex.decode(cipherData);

		//刚才的私钥Hex，先还原私钥
		String privateKey = "8cd385a72cb6e6e5b726b7b00a4149a8ff2b820d6226515f0edc7db31b4b20bb";
		BigInteger privateKeyD = new BigInteger(privateKey, 16);
		X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
		
		ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

		//用私钥解密
		SM2Engine sm2Engine = new SM2Engine();
		sm2Engine.init(false, privateKeyParameters);

		//processBlock得到Base64格式，记得解码
		byte[] arrayOfBytes;
		try {
//			byte[] dd = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
//			arrayOfBytes = Base64.getDecoder().decode(dd);
			arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
			//得到明文：SM2 Encryption Test
			String data = new String(arrayOfBytes);
			System.out.println(data);
		} catch (InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void encode(){
		//刚才的私钥Hex，先还原私钥
		String publicKey = "04321ce3dc8b6c271cfc30d215ab15aab05fa23db68c3a469cf27a66ce483db54291794cb71ea6414706d931027c141f5c584bf57ab9eb9d9d0532301495b9220a";
		byte[] bb = ByteUtils.fromHexString(publicKey);
		String plaintext = "hello word";
		 // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造ECC算法参数，曲线方程、椭圆曲线G点、大整数N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);
 
        SM2Engine sm2Engine = new SM2Engine();
         // 设置sm2为加密模式
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));
 
        byte[] arrayOfBytes = null;
        try {
            byte[] in = plaintext.getBytes();
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
            System.out.println(Hex.toHexString(arrayOfBytes));
        } catch (Exception e) {
        }
	}
	

	public static void test(){
		String cipherData = "04d2ae2aaa753118b069ca356e733015558a0b511d651437122d7c25abe6bcf95d3838411731d3bb85531ca3043e1f94933f82324dbba23b0f6a450303e98297485b111e185debc02adc43d9ceb33d2549442dcac53a1fbcaa1c21d1b747aab1153f23e566f760b60203da7662cb18f9533dec91614660e1ce11961f26d9e873b73c1aa28ae474bd8a29815ee0d5dcf4644c39918f461d5d5a9aea992a";
		byte[] cipherDataByte = Hex.decode(cipherData);

		//刚才的私钥Hex，先还原私钥
		String privateKey = "d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
		BigInteger privateKeyD = new BigInteger(privateKey, 16);
		X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
		
		ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

		//用私钥解密
		SM2Engine sm2Engine = new SM2Engine();
		sm2Engine.init(false, privateKeyParameters);

		//processBlock得到Base64格式，记得解码
		byte[] arrayOfBytes;
		try {
			arrayOfBytes = Base64.getDecoder().decode(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));
			//得到明文：SM2 Encryption Test
			String data = new String(arrayOfBytes);
			System.out.println(data);
		} catch (InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void test1(){
		String cipherData = "";
		byte[] cipherDataByte = Hex.decode(cipherData);

		//刚才的私钥Hex，先还原私钥
		String privateKey = "";
		BigInteger privateKeyD = new BigInteger(privateKey, 16);
		X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
		
		ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

		//用私钥解密
		SM2Engine sm2Engine = new SM2Engine();
		sm2Engine.init(false, privateKeyParameters);

		//processBlock得到Base64格式，记得解码
		byte[] arrayOfBytes;
		try {
			arrayOfBytes = Base64.getDecoder().decode(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));
			//得到明文：SM2 Encryption Test
			String data = new String(arrayOfBytes);
			System.out.println(data);
		} catch (InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
