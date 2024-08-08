package test0618;

import java.io.IOException;
import java.math.BigInteger;



import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class SM2Utils 
{
	//生成随机秘钥对
	public static void generateKeyPair(){
		SM2 sm2 = SM2.Instance();
		AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
		ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
		ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
		BigInteger privateKey = ecpriv.getD();
		ECPoint publicKey = ecpub.getQ();
		
		System.out.println("公钥: " + Util.byteToHex(publicKey.getEncoded(false)));
		System.out.println("私钥: " + Util.byteToHex(privateKey.toByteArray()));
	}
	
	//数据加密
	public static String encrypt(byte[] publicKey, byte[] data) throws IOException
	{
		if (publicKey == null || publicKey.length == 0)
		{
			return null;
		}
		
		if (data == null || data.length == 0)
		{
			return null;
		}
		
		byte[] source = new byte[data.length];
		System.arraycopy(data, 0, source, 0, data.length);
		
		Cipher cipher = new Cipher();
		SM2 sm2 = SM2.Instance();
		ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);
		
		ECPoint c1 = cipher.Init_enc(sm2, userKey);
		cipher.Encrypt(source);
		byte[] c3 = new byte[32];
		cipher.Dofinal(c3);
		
//		System.out.println("C1 " + Util.byteToHex(c1.getEncoded()));
//		System.out.println("C2 " + Util.byteToHex(source));
//		System.out.println("C3 " + Util.byteToHex(c3));
		//C1 C2 C3拼装成加密字串
		return Util.byteToHex(c1.getEncoded(false)) + Util.byteToHex(source) + Util.byteToHex(c3);
		
	}
	
	//数据解密
	public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException
	{
		if (privateKey == null || privateKey.length == 0)
		{
			return null;
		}
		
		if (encryptedData == null || encryptedData.length == 0)
		{
			return null;
		}
		//加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
		String data = Util.byteToHex(encryptedData);
		/***分解加密字串
		 * （C1 = C1标志位2位 + C1实体部分128位 = 130）
		 * （C3 = C3实体部分64位  = 64）
		 * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
		 */
		byte[] c1Bytes = Util.hexToByte(data.substring(0,130));
		int c2Len = encryptedData.length - 97;
		byte[] c2 = Util.hexToByte(data.substring(130,130 + 2 * c2Len));
		byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len,194 + 2 * c2Len));
		
		SM2 sm2 = SM2.Instance();
		BigInteger userD = new BigInteger(1, privateKey);
		
		//通过C1实体字节来生成ECPoint
		ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
		Cipher cipher = new Cipher();
		cipher.Init_dec(userD, c1);
		cipher.Decrypt(c2);
		cipher.Dofinal(c3);
		
		//返回解密结果
		return c2;
	}
	
	public static void main(String[] args) throws Exception 
	{
		//生成密钥对
		generateKeyPair();
		
		String plainText = "ererfeiisgoL偶luo罗学华d";
		byte[] sourceData = plainText.getBytes();
		
		
		//以下的秘钥能够使用generateKeyPair()生成的秘钥内容
		// 国密规范正式私钥
		String prik = "00ECF5E1A36C1DB7CC97246A8A768DF5E8158E0039E989C92908E14BA822A2061F";
		// 国密规范正式公钥
		String pubk = "04BE36806FBC588C7B17C2F9FE1CC9D2C186D086C07FC9C194AE72282B7F09F559448397D201489732BD2EB70F31A11CE2DACDA6BC79B4EAE960D4B96142AC64A1";
		
		System.out.println("加密: ");
		String cipherText = SM2Utils.encrypt(Util.hexToByte(pubk), sourceData);
		System.out.println(cipherText);
		System.out.println("解密: ");
		plainText = new String(SM2Utils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText)));
		System.out.println(plainText);
		
		
		//--->SM2公钥：04339149dbdce1c3d0884df14d4d45e41bc95b86f14b5e5da1f4ef8a7604eaa52230096cae14fc6c1bb7805adcf564d23c77cd72d6c27547dc342e35751b3fd7cb
		//---->SM2私钥：d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193
		//解码模式C1C3C2:0待解密数据：04d2ae2aaa753118b069ca356e733015558a0b511d651437122d7c25abe6bcf95d3838411731d3bb85531ca3043e1f94933f82324dbba23b0f6a450303e98297485b111e185debc02adc43d9ceb33d2549442dcac53a1fbcaa1c21d1b747aab1153f23e566f760b60203da7662cb18f9533dec91614660e1ce11961f26d9e873b73c1aa28ae474bd8a29815ee0d5dcf4644c39918f461d5d5a9aea992a
		//---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1Ng==
		//--->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456
		//解码模式C1C2C3=0表示待解密数据：04dc3c2bfe52e4b36c18ffd48475b5fb4e0b8247be70c70d15dce7f94c8beb76ac201197c563d8cfcd5b59d10465b05e1828b40d6b62d5f8065099f47b13e9ec7e8a2015427435936a6307b1e76a1d18011a6ec773ac0d124323d9d2f34dbc17f8f2eac1958995e5189b15da5242acef31339687b8fb5fc420b045c6996b9be4150d1e0cebfc3cd8f637646fef57ba3f4cf5209a6fb4db8a4204fa9533
		//---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1Ng==
		//---->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456
		//解码模式C1C3C2=1表示待解密数据：04c07e53a77f33c4d25cce550316a24260bab8b94962453fbf8a550f524ad33662512ce1c598d74653fbf794c9bbbe7bd8d71fc3df548a6ef501f6de42e7426ec00f044cd37739252530f9b9d8198ac06ccf4fb125e81830f34c32d9d83e8b5bfbcc287ff303b660f3eea1b9cae67275c274afcd833b44df8ec3d6348fafd01553340cfeaa4e3c0bd511fee96f8bb400e3ea95b9478ff50af69f872dc9594bb1fa7587f8ff911a5ad507b3ea64671e8894eb021639
		//---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1NiBjaXBoZXJfbW9kZSA9QzFDM0My
		//---->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456 cipher_mode =C1C3C2
		//解码模式C1C3C2=1表示待解密数据：04702359cfe52f177307aa70631a36fc31005fe1217f6861a67dc4fe9dff66858703ad0c7aeb6e7a1678d5e88fe2ced731ebff0de059e03ef5b00074fd26c5da736a4db1688cdabe45e4a4c9c2e1b4d5266dc1909a09f9a4e6f37f2072e516348e4d9072339b5f9339d2b200953b0f783e5f2178a4d60b25c4545fe3348ad3c0a774a7de8b862bd3a92d90ab3df48fc5636ff20e1b1a9564266c6106b579f325fb2f8fa31b83e1fe39fa7c38c7
		//---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1NiBtb2RlPUMxQzNDMg==
		//---->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456 mode=C1C3C2
        prik ="d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
		cipherText = "04dc3c2bfe52e4b36c18ffd48475b5fb4e0b8247be70c70d15dce7f94c8beb76ac201197c563d8cfcd5b59d10465b05e1828b40d6b62d5f8065099f47b13e9ec7e8a2015427435936a6307b1e76a1d18011a6ec773ac0d124323d9d2f34dbc17f8f2eac1958995e5189b15da5242acef31339687b8fb5fc420b045c6996b9be4150d1e0cebfc3cd8f637646fef57ba3f4cf5209a6fb4db8a4204fa9533";
		System.out.println("解密: ");
		System.out.println("密文"+cipherText);
		plainText = new String(java.util.Base64.getDecoder().decode(SM2Utils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText))));
		System.out.println(plainText);	
	}
}