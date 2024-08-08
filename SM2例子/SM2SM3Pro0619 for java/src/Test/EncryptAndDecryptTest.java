package Test;

import org.junit.Test;

import Sm2Sm3.Sm2Util;

public class EncryptAndDecryptTest {
	 
    private static String pwd = "123456";
 
   @Test
   public void Test1()
   {
	 //--->SM2公钥：04339149dbdce1c3d0884df14d4d45e41bc95b86f14b5e5da1f4ef8a7604eaa52230096cae14fc6c1bb7805adcf564d23c77cd72d6c27547dc342e35751b3fd7cb
	 //---->SM2私钥：d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193
	// 解码模式C1C3C2:0待解密数据：04d2ae2aaa753118b069ca356e733015558a0b511d651437122d7c25abe6bcf95d3838411731d3bb85531ca3043e1f94933f82324dbba23b0f6a450303e98297485b111e185debc02adc43d9ceb33d2549442dcac53a1fbcaa1c21d1b747aab1153f23e566f760b60203da7662cb18f9533dec91614660e1ce11961f26d9e873b73c1aa28ae474bd8a29815ee0d5dcf4644c39918f461d5d5a9aea992a
	// ---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1Ng==
	 //---->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456
	// 解码模式C1C2C3=0表示待解密数据：04dc3c2bfe52e4b36c18ffd48475b5fb4e0b8247be70c70d15dce7f94c8beb76ac201197c563d8cfcd5b59d10465b05e1828b40d6b62d5f8065099f47b13e9ec7e8a2015427435936a6307b1e76a1d18011a6ec773ac0d124323d9d2f34dbc17f8f2eac1958995e5189b15da5242acef31339687b8fb5fc420b045c6996b9be4150d1e0cebfc3cd8f637646fef57ba3f4cf5209a6fb4db8a4204fa9533
	// ---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1Ng==
	// ---->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456
	// 解码模式C1C3C2=1表示待解密数据：04c07e53a77f33c4d25cce550316a24260bab8b94962453fbf8a550f524ad33662512ce1c598d74653fbf794c9bbbe7bd8d71fc3df548a6ef501f6de42e7426ec00f044cd37739252530f9b9d8198ac06ccf4fb125e81830f34c32d9d83e8b5bfbcc287ff303b660f3eea1b9cae67275c274afcd833b44df8ec3d6348fafd01553340cfeaa4e3c0bd511fee96f8bb400e3ea95b9478ff50af69f872dc9594bb1fa7587f8ff911a5ad507b3ea64671e8894eb021639
	// ---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1NiBjaXBoZXJfbW9kZSA9QzFDM0My
	// ---->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456 cipher_mode =C1C3C2
//	 解码模式C1C3C2=1表示待解密数据：04702359cfe52f177307aa70631a36fc31005fe1217f6861a67dc4fe9dff66858703ad0c7aeb6e7a1678d5e88fe2ced731ebff0de059e03ef5b00074fd26c5da736a4db1688cdabe45e4a4c9c2e1b4d5266dc1909a09f9a4e6f37f2072e516348e4d9072339b5f9339d2b200953b0f783e5f2178a4d60b25c4545fe3348ad3c0a774a7de8b862bd3a92d90ab3df48fc5636ff20e1b1a9564266c6106b579f325fb2f8fa31b83e1fe39fa7c38c7
//	 ---->解密结果：6Kej5a+G5ZCO5L+h5oGvOuWTiOWTiOWTiO+8jOWYv+WYv+WYvzEyMzQ1NiBtb2RlPUMxQzNDMg==
//	 ---->解密结果base64解码：解密后信息:哈哈哈，嘿嘿嘿123456 mode=C1C3C2  
	   //Bytes =  java.util.Base64.getDecoder().decode()

	   String priKey = "d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
	   System.out.println("私钥Hex："+priKey);
	   String encrypt  ="04702359cfe52f177307aa70631a36fc31005fe1217f6861a67dc4fe9dff66858703ad0c7aeb6e7a1678d5e88fe2ced731ebff0de059e03ef5b00074fd26c5da736a4db1688cdabe45e4a4c9c2e1b4d5266dc1909a09f9a4e6f37f2072e516348e4d9072339b5f9339d2b200953b0f783e5f2178a4d60b25c4545fe3348ad3c0a774a7de8b862bd3a92d90ab3df48fc5636ff20e1b1a9564266c6106b579f325fb2f8fa31b83e1fe39fa7c38c7";
	   // 国密BC解密
	   System.out.println("芯片BC加密数据C1C3C2格式："+encrypt);
       String decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt)));
       System.out.println("芯片BC解密后密码:"+decrypt);
       // 国密解密
       encrypt ="04d2ae2aaa753118b069ca356e733015558a0b511d651437122d7c25abe6bcf95d3838411731d3bb85531ca3043e1f94933f82324dbba23b0f6a450303e98297485b111e185debc02adc43d9ceb33d2549442dcac53a1fbcaa1c21d1b747aab1153f23e566f760b60203da7662cb18f9533dec91614660e1ce11961f26d9e873b73c1aa28ae474bd8a29815ee0d5dcf4644c39918f461d5d5a9aea992a";
       System.out.println("国密加密数据C1C2C3格式："+encrypt);
       decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt,0)));
       System.out.println("国密解密后数据:"+decrypt);
   }
   
   
    public static void main(String[] args){
    	new EncryptAndDecryptTest().Test1();
    	 System.out.println("未加密密码："+pwd);
         String[] sm2Keys = Sm2Util.getSm2Keys(false);
         String pubKey = sm2Keys[0];
         System.out.println("公钥："+pubKey);
         String priKey = sm2Keys[1];
         System.out.println("私钥："+priKey);
  
         // 国密加密
        String encrypt = Sm2Util.encrypt(pubKey, pwd);
       System.out.println("国密加密后密码:"+encrypt);
  
         // 国密解密
         String decrypt = Sm2Util.decrypt(priKey, encrypt);
         System.out.println("国密解密后密码:"+decrypt);
  
         // BC库加密
         String encrypt1 = Sm2Util.encrypt(pubKey, pwd, 1);
         System.out.println("BC加密："+encrypt1);
         String decrypt1 = Sm2Util.decrypt(priKey, encrypt1, 1);
         System.out.println("BC解密："+decrypt1);
  
         
         
         
      }
    }

