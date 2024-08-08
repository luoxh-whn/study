package Test;

import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import SM2SM3.SM2EngineExtend;
import SM2SM3.SM2KeyPair;
import SM2SM3.SM2Utils;
import SM2SM3.Util;

public class EncryptAndDecryptTest {
	 

private static String pwd = "123456";





 public static void main(String[] args) throws Exception {
 	
 	  
		
		String[] stringKeyPair = SM2Utils.getSm2Keys(false);
		
       
		
	  
	  String pubKey1= stringKeyPair[0];
	    
 	  String priKey1 = stringKeyPair[1];
		
		System.out.println("pubKey1="+pubKey1);
		System.out.println("priKey1="+priKey1);
		
		  // 国密加密
        String encrypt = SM2Utils.encrypt(pubKey1, pwd,0);
        System.out.println("export国密0加密后密码:"+encrypt);
  
         // 国密解密
         String decrypt = SM2Utils.decrypt(priKey1, encrypt,0);
         System.out.println("export国密解密后密码:"+decrypt);
  
         // BC库加密
         String encrypt1 = SM2Utils.encrypt(pubKey1, pwd,1);
         System.out.println("exportBC加密1："+encrypt1);
         String decrypt1 = SM2Utils.decrypt(priKey1, encrypt1,1);
         System.out.println("exportBC解密："+decrypt1);
         
      // 生成密钥对
         SM2EngineExtend  sm2ee =new SM2EngineExtend();
     	
 		sm2ee.setDebug(false);
 		
 		//SM2KeyPair keyPair = sm2ee.generateKeyPair();
 			
 		
        // sm2ee.exportPublicKey(keyPair.getPublicKey(), "D:/publickey.pem");
        // sm2ee.exportPrivateKey(keyPair.getPrivateKey(), "D:/privatekey.pem");
 		System.out.println("-----------------公钥加密与解密-----------------");
 		 // 公钥前面的02或者03表示是压缩公钥,04表示未压缩公钥,04的时候,可以去掉前面的04
 		ECPoint publicKey = sm2ee.importPublicKey("D:/publickey.pem");
 		BigInteger privateKey = sm2ee.importPrivateKey("D:/privatekey.pem");
 		/*测试1*/
 		

 		
 		System.out.println("----------------从文件导入公钥结束-----------------");
 		String pubKey =  Util.byteToHex(publicKey.getEncoded(false));
 		String priKey = Util.byteToHex(privateKey.toByteArray());
 		 
 		System.out.println("-----------------再公钥加密与解密-----------------");
 		System.out.println("pubKey="+pubKey);
 		System.out.println("priKey="+priKey);
 		System.out.println("-----------------公钥加密与解密-----------------");
 		 // 国密加密
         encrypt = SM2Utils.encrypt(pubKey, pwd+"罗学华",0);
         System.out.println("export国密0加密后密码:"+encrypt);
  
         // 国密解密
        decrypt = SM2Utils.decrypt(priKey, encrypt,0);
         System.out.println("export国密解密后密码:"+decrypt);
  
         // BC库加密
         encrypt1 = SM2Utils.encrypt(pubKey, pwd+"当阳",1);
         System.out.println("exportBC加密1："+encrypt1);
         decrypt1 = SM2Utils.decrypt(priKey, encrypt1,1);
         System.out.println("exportBC解密："+decrypt1);
 
     }

}


