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
	   //-----------------��Կ���������-----------------
	  // pubKey1=02c52f66b9208010acd75ad328a87569b8c664fce195a807eab3f5994a0fb6ecce
	  // priKey1=00bd8395eb1bf389c7e003f64f736a00300c8adc3f3a3557e558ce96f16325375f
	  // export����0���ܺ�����:042647fb34ebb3fa3acae2037de45aec5e58c5142dee360870d95a18b384a6df8c1819f72c44daad54219a82f3d286dbd7d33974ab54c12440f8d14db340ab57723387847f1784de7f805394587082ca87453e92db11810cd1f69b186e5b226daf9df8d100fd24c4288ec7dce0d245c3
	  // export���ܽ��ܺ�����:123456��ѧ��
	  // exportBC����1��04727e34c9c8cd155e96966c6203c8fc205790f42aeb7f01f588132d1405a95907f2f256a63412972885ba13f07dc9dbc038da5a6817a19f44fc5b5542befd462583c71a0fc9fda4cb8fdf0b784b2f0096167e84e7669198ca1cb054b4a901e9b287fbc0f57c10bb519e214976
	 //  exportBC���ܣ�123456����
	 //  δ�������룺123456
	 

	   String priKey = "00bd8395eb1bf389c7e003f64f736a00300c8adc3f3a3557e558ce96f16325375f";
	   System.out.println("˽ԿHex��"+priKey);
	   String encrypt  ="042647fb34ebb3fa3acae2037de45aec5e58c5142dee360870d95a18b384a6df8c1819f72c44daad54219a82f3d286dbd7d33974ab54c12440f8d14db340ab57723387847f1784de7f805394587082ca87453e92db11810cd1f69b186e5b226daf9df8d100fd24c4288ec7dce0d245c3";
	   // ����BC����
	   System.out.println("0��ʽ0�⣺"+encrypt);
       //String decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt)));
	   String decrypt = Sm2Util.decrypt(priKey, encrypt,0);
       System.out.println("оƬBC���ܺ�����:"+decrypt);
       // ���ܽ���
       encrypt ="04727e34c9c8cd155e96966c6203c8fc205790f42aeb7f01f588132d1405a95907f2f256a63412972885ba13f07dc9dbc038da5a6817a19f44fc5b5542befd462583c71a0fc9fda4cb8fdf0b784b2f0096167e84e7669198ca1cb054b4a901e9b287fbc0f57c10bb519e214976";
       System.out.println("1��ʽ1��"+encrypt);
       //decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt,1)));
       decrypt = Sm2Util.decrypt(priKey, encrypt,1);
       System.out.println("���ܽ��ܺ�����:"+decrypt);
       priKey="008c5ccbaaaf7a00fd4c64e0a40baa23c0c11bccb676a9b59839c36809b7373e4d";
       // ���ܽ���
       encrypt  ="042647fb34ebb3fa3acae2037de45aec5e58c5142dee360870d95a18b384a6df8c1819f72c44daad54219a82f3d286dbd7d33974ab54c12440f8d14db340ab57723387847f1784de7f805394587082ca87453e92db11810cd1f69b186e5b226daf9df8d100fd24c4288ec7dce0d245c3";
	   // ����BC����
	   System.out.println("0��ʽ1�⣺"+encrypt);
       //String decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt)));
	    decrypt = Sm2Util.decrypt(priKey, encrypt,1);
       System.out.println("оƬBC���ܺ�����:"+decrypt);
       // ���ܽ���
       encrypt ="04727e34c9c8cd155e96966c6203c8fc205790f42aeb7f01f588132d1405a95907f2f256a63412972885ba13f07dc9dbc038da5a6817a19f44fc5b5542befd462583c71a0fc9fda4cb8fdf0b784b2f0096167e84e7669198ca1cb054b4a901e9b287fbc0f57c10bb519e214976";
       System.out.println("1��ʽ0�⣺"+encrypt);
       //decrypt = new String(java.util.Base64.getDecoder().decode(Sm2Util.decrypt(priKey, encrypt,1)));
       decrypt = Sm2Util.decrypt(priKey, encrypt,0);
       System.out.println("���ܽ��ܺ�����:"+decrypt);
       priKey="008c5ccbaaaf7a00fd4c64e0a40baa23c0c11bccb676a9b59839c36809b7373e4d";
       // ���ܽ���
   
   }

   /**
    * �ֽ�����ת��Ϊʮ�������ַ���
    *
    * @param b byte[] ��Ҫת�����ֽ�����
    * @return String ʮ�������ַ���
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
		System.out.println("-----------------��Կ���������-----------------");
		 // ��Կǰ���02����03��ʾ��ѹ����Կ,04��ʾδѹ����Կ,04��ʱ��,����ȥ��ǰ���04
		ECPoint publicKey = sm2ee.importPublicKey("D:/publickey.pem");
		BigInteger privateKey = sm2ee.importPrivateKey("D:/privatekey.pem");
		/*����1*/
		
	  
	
	    
    	
		String puKey1 =  byteToHex(publicKey.getEncoded(true));
		String priKey1 = byteToHex(privateKey.toByteArray());
		System.out.println("pubKey1="+puKey1);
		System.out.println("priKey1="+priKey1);
		  // ���ܼ���
        String encrypt = Sm2Util.encrypt(puKey1, pwd+"��ѧ��",0);
        System.out.println("export����0���ܺ�����:"+encrypt);
  
         // ���ܽ���
         String decrypt = Sm2Util.decrypt(priKey1, encrypt,0);
         System.out.println("export���ܽ��ܺ�����:"+decrypt);
  
         // BC�����
         String encrypt1 = Sm2Util.encrypt(puKey1, pwd+"����", 1);
         System.out.println("exportBC����1��"+encrypt1);
         String decrypt1 = Sm2Util.decrypt(priKey1, encrypt1, 1);
         System.out.println("exportBC���ܣ�"+decrypt1);
  
    	
    	
    	
    	 System.out.println("δ�������룺"+pwd);
         String[] sm2Keys = Sm2Util.getSm2Keys(false);
         String pubKey = sm2Keys[0];
         System.out.println("��Կ��"+pubKey);
         String priKey = sm2Keys[1];
         System.out.println("˽Կ��"+priKey);
  
         // ���ܼ���
        encrypt = Sm2Util.encrypt(pubKey, pwd);
        System.out.println("���ܼ��ܺ�����:"+encrypt);
  
         // ���ܽ���
         decrypt = Sm2Util.decrypt(priKey, encrypt);
         System.out.println("���ܽ��ܺ�����:"+decrypt);
  
         // BC�����
          encrypt1 = Sm2Util.encrypt(pubKey, pwd, 1);
         System.out.println("BC���ܣ�"+encrypt1);
          decrypt1 = Sm2Util.decrypt(priKey, encrypt1, 1);
         System.out.println("BC���ܣ�"+decrypt1);
  
         
         
         
      }
    }

