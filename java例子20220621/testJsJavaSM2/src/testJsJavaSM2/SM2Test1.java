package testJsJavaSM2;

import java.io.IOException;

public class SM2Test1 {
    public static void main(String[] args) throws IOException {

        // 获取公钥私钥对
        SM2KeyPair sm2KeyPair = SM2Utils.generateKeyPair();

        // 获取公钥和私钥
        String publickey=sm2KeyPair.getPublicKey();
        System.out.println(publickey);
        String privatekey=sm2KeyPair.getPrivateKey();
        System.out.println(privatekey);
  //      公钥: 0484AE143E2D5C01907ACCB03A00531D6EF98BCEFACE1ADCE31CAEA2887BAEB86B76871BFFA97C909E590AFF6007CD32924C36218BC85D40EB5F2BF6132F86C55E
     //   私钥: 70E0553D1DBA9B25FDBF5F6E4B89029201291A240C2E5EF0EFD443FE0E8CAD3D
   //     0484AE143E2D5C01907ACCB03A00531D6EF98BCEFACE1ADCE31CAEA2887BAEB86B76871BFFA97C909E590AFF6007CD32924C36218BC85D40EB5F2BF6132F86C55E
   //     70E0553D1DBA9B25FDBF5F6E4B89029201291A240C2E5EF0EFD443FE0E8CAD3D
    //    加密后信息：04E10063E3BFECF5BB073E749E7FF7B3E928D0F5972DC97FA345C24227AD83B2E557F1946F04CD893D6795F856B8F7F9667201410C02934E5834776E69F0E23DCE56AEC53DF89478110CD00C91DCEDCBF2E7A800DEAA96C0D6A79CDB63A1C72E54C10E97432F4B60323DB189A16ED77EEF5EC7254BF4
  //      解密后信息:哈哈哈，嘿嘿嘿
        // 需要加密的信息
        String info="哈哈哈，嘿嘿嘿";
        // 将信息加密，公钥的字节码需要使用util里的hexToByte方法
        String encrypt = SM2Utils.SM2Encode(publickey,info);

        System.out.println("加密后信息："+encrypt);

        // 将加密后的信息解密，私钥和加密后的数据的字节码对象需要用util里的hexToByte方法
        String res = SM2Utils.SM2Decode(privatekey,encrypt);;
        // 将字节数组转为字符串
        System.out.println("解密后信息:"+res);
        
        System.out.println("解密后信息:"+  SM2Utils.SM2Decode(privatekey,"048256d423556aa8b7fc2d6b9f82938db4dd297d9f7cc6edf009a4b2dee3dda61f95eed663345507ab2cd9fed57d9d5c037210c9927ef11e4e616c6518ac011bc273c57ce2fcf51b7997b793fe7e6cf033a78f3f8b97bd86ad4868e5eb4eb1dc05725adfb4e248e794732440db274bd9a4e0680c17652d89f9361662cf72e09db42652a444c1387920d66beceb11c575b4406be9e5"));


    }

}
