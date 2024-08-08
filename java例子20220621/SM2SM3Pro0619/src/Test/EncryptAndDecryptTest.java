package Test;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;
import org.junit.Test;

import Sm2Sm3.SM2EngineExtend;
import Sm2Sm3.SM2KeyPair;
import Sm2Sm3.Sm2Util;

public class EncryptAndDecryptTest {
	 
    private static String pwd = "123456";
 
   @Test
   public void Test1()
   {
	   //-----------------公钥加密与解密-----------------
	  // pubKey1=02c52f66b9208010acd75ad328a87569b8c664fce195a807eab3f5994a0fb6ecce
	  // priKey1=00bd8395eb1bf389c7e003f64f736a00300c8adc3f3a3557e558ce96f16325375f
	  // export国密0加密后密码:042647fb34ebb3fa3acae2037de45aec5e58c5142dee360870d95a18b384a6df8c1819f72c44daad54219a82f3d286dbd7d33974ab54c12440f8d14db340ab57723387847f1784de7f805394587082ca87453e92db11810cd1f69b186e5b226daf9df8d100fd24c4288ec7dce0d245c3
	  // export国密解密后密码:123456罗学华
	  // exportBC加密1：04727e34c9c8cd155e96966c6203c8fc205790f42aeb7f01f588132d1405a95907f2f256a63412972885ba13f07dc9dbc038da5a6817a19f44fc5b5542befd462583c71a0fc9fda4cb8fdf0b784b2f0096167e84e7669198ca1cb054b4a901e9b287fbc0f57c10bb519e214976
	 //  exportBC解密：123456当阳
	 //  未加密密码：123456
	 

	   String priKey = "00bd8395eb1bf389c7e003f64f736a00300c8adc3f3a3557e558ce96f16325375f";
	   System.out.println("私钥Hex："+priKey);
	   String encrypt  ="042647fb34ebb3fa3acae2037de45aec5e58c5142dee360870d95a18b384a6df8c1819f72c44daad54219a82f3d286dbd7d33974ab54c12440f8d14db340ab57723387847f1784de7f805394587082ca87453e92db11810cd1f69b186e5b226daf9df8d100fd24c4288ec7dce0d245c3";
	   // 国密BC解密
	   System.out.println("0格式0解："+encrypt);
       //String decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt)));
	   String decrypt = Sm2Util.decrypt(priKey, encrypt,0);
       System.out.println("芯片BC解密后密码:"+decrypt);
       // 国密解密
       encrypt ="04727e34c9c8cd155e96966c6203c8fc205790f42aeb7f01f588132d1405a95907f2f256a63412972885ba13f07dc9dbc038da5a6817a19f44fc5b5542befd462583c71a0fc9fda4cb8fdf0b784b2f0096167e84e7669198ca1cb054b4a901e9b287fbc0f57c10bb519e214976";
       System.out.println("1格式1："+encrypt);
       //decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt,1)));
       decrypt = Sm2Util.decrypt(priKey, encrypt,1);
       System.out.println("国密解密后数据:"+decrypt);
       priKey="008c5ccbaaaf7a00fd4c64e0a40baa23c0c11bccb676a9b59839c36809b7373e4d";
       // 国密解密
       encrypt  ="042647fb34ebb3fa3acae2037de45aec5e58c5142dee360870d95a18b384a6df8c1819f72c44daad54219a82f3d286dbd7d33974ab54c12440f8d14db340ab57723387847f1784de7f805394587082ca87453e92db11810cd1f69b186e5b226daf9df8d100fd24c4288ec7dce0d245c3";
	   // 国密BC解密
	   System.out.println("0格式1解："+encrypt);
       //String decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt)));
	    decrypt = Sm2Util.decrypt(priKey, encrypt,1);
       System.out.println("芯片BC解密后密码:"+decrypt);
       // 国密解密
       encrypt ="04727e34c9c8cd155e96966c6203c8fc205790f42aeb7f01f588132d1405a95907f2f256a63412972885ba13f07dc9dbc038da5a6817a19f44fc5b5542befd462583c71a0fc9fda4cb8fdf0b784b2f0096167e84e7669198ca1cb054b4a901e9b287fbc0f57c10bb519e214976";
       System.out.println("1格式0解："+encrypt);
       //decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt,1)));
       decrypt = Sm2Util.decrypt(priKey, encrypt,0);
       System.out.println("国密解密后数据:"+decrypt);
       priKey="008c5ccbaaaf7a00fd4c64e0a40baa23c0c11bccb676a9b59839c36809b7373e4d";
       // 国密解密
   
   }

   /**
    * 字节数组转换为十六进制字符串
    *
    * @param b byte[] 需要转换的字节数组
    * @return String 十六进制字符串
    */
   public static String byteToHex(byte b[]) {
       if (b == null) {
           throw new IllegalArgumentException(
                   "Argument b ( byte array ) is null! ");
       }
       String hs = "";
       String stmp = "";
       for (int n = 0; n < b.length; n++) {
           stmp = Integer.toHexString(b[n] & 0xff);
           if (stmp.length() == 1) {
               hs = hs + "0" + stmp;
           } else {
               hs = hs + stmp;
           }
       }
       return hs.toLowerCase();
       //return hs.toUpperCase();
   }
   
    public static void main(String[] args){
    	
    	
    	
    	
    	new EncryptAndDecryptTest().Test1();
    	
    	SM2EngineExtend sm2ee =new SM2EngineExtend();
    	
		sm2ee.setDebug(false);
		
		//SM2KeyPair keyPair = sm2ee.generateKeyPair();
			
		
        //sm2ee.exportPublicKey(keyPair.getPublicKey(), "D:/publickey.pem");
        //sm2ee.exportPrivateKey(keyPair.getPrivateKey(), "D:/privatekey.pem");
		System.out.println("-----------------公钥加密与解密-----------------");
		 // 公钥前面的02或者03表示是压缩公钥,04表示未压缩公钥,04的时候,可以去掉前面的04
		ECPoint publicKey = sm2ee.importPublicKey("D:/publickey.pem");
		BigInteger privateKey = sm2ee.importPrivateKey("D:/privatekey.pem");
		/*测试1*/
		
	  
	
	    
    	
		String puKey1 =  byteToHex(publicKey.getEncoded(true));
		String priKey1 = byteToHex(privateKey.toByteArray());
		System.out.println("pubKey1="+puKey1);
		System.out.println("priKey1="+priKey1);
		  // 国密加密
        String encrypt = Sm2Util.encrypt(puKey1, pwd+"罗学华",0);
        System.out.println("export国密0加密后密码:"+encrypt);
  
         // 国密解密
         String decrypt = Sm2Util.decrypt(priKey1, encrypt,0);
         System.out.println("export国密解密后密码:"+decrypt);
  
         // BC库加密
         String encrypt1 = Sm2Util.encrypt(puKey1, pwd+"当阳", 1);
         System.out.println("exportBC加密1："+encrypt1);
         String decrypt1 = Sm2Util.decrypt(priKey1, encrypt1, 1);
         System.out.println("exportBC解密："+decrypt1);
  
    	
    	
    	
    	 System.out.println("未加密密码："+pwd);
         String[] sm2Keys = Sm2Util.getSm2Keys(false);
         String pubKey = sm2Keys[0];
         System.out.println("公钥："+pubKey);
         String priKey = sm2Keys[1];
         System.out.println("私钥："+priKey);
  
         // 国密加密
        encrypt = Sm2Util.encrypt(pubKey, pwd);
        System.out.println("国密加密后密码:"+encrypt);
  
         // 国密解密
         decrypt = Sm2Util.decrypt(priKey, encrypt);
         System.out.println("国密解密后密码:"+decrypt);
  
         // BC库加密
          encrypt1 = Sm2Util.encrypt(pubKey, pwd, 1);
         System.out.println("BC加密："+encrypt1);
          decrypt1 = Sm2Util.decrypt(priKey, encrypt1, 1);
         System.out.println("BC解密："+decrypt1);
  
         
         
         
      }
    }

