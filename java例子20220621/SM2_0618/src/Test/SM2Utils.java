package Test;


import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

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
import org.bouncycastle.util.encoders.Hex;

/**
 * @ClassName SM2Utils
 * @Description SM2绠楁硶宸ュ叿绫�
 * @Author msx
 * @Date 20220618
 * @Version 1.0
 */
public class SM2Utils {

    /**
     * @Description 鐢熸垚绉橀挜瀵�
     * @Author msx
     * @return KeyPair
     */
    public static KeyPair createECKeyPair() {
        //浣跨敤鏍囧噯鍚嶇О鍒涘缓EC鍙傛暟鐢熸垚鐨勫弬鏁拌鑼�
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");

        // 鑾峰彇涓�涓き鍦嗘洸绾跨被鍨嬬殑瀵嗛挜瀵圭敓鎴愬櫒
        final KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
            // 浣跨敤SM2绠楁硶鍩熷弬鏁伴泦鍒濆鍖栧瘑閽ョ敓鎴愬櫒锛堥粯璁や娇鐢ㄤ互鏈�楂樹紭鍏堢骇瀹夎鐨勬彁渚涜�呯殑 SecureRandom 鐨勫疄鐜颁綔涓洪殢鏈烘簮锛�
            // kpg.initialize(sm2Spec);

            // 浣跨敤SM2鐨勭畻娉曞煙鍙傛暟闆嗗拰鎸囧畾鐨勯殢鏈烘簮鍒濆鍖栧瘑閽ョ敓鎴愬櫒
            kpg.initialize(sm2Spec, new SecureRandom());

            // 閫氳繃瀵嗛挜鐢熸垚鍣ㄧ敓鎴愬瘑閽ュ
            return kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description 鍏挜鍔犲瘑
     * @Author msx
     * @param publicKeyHex SM2鍗佸叚杩涘埗鍏挜
     * @param data         鏄庢枃鏁版嵁
     * @return String
     */
    public static String encrypt(String publicKeyHex, String data) {
        return encrypt(getECPublicKeyByPublicKeyHex(publicKeyHex), data, 1);
    }

    /**
     * @Description 鍏挜鍔犲瘑
     * @Author msx
     * @param publicKey SM2鍏挜
     * @param data      鏄庢枃鏁版嵁
     * @param modeType  鍔犲瘑妯″紡
     * @return String
     */
    public static String encrypt(BCECPublicKey publicKey, String data, int modeType) {
        //鍔犲瘑妯″紡
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        if (modeType != 1) {
            mode = SM2Engine.Mode.C1C2C3;
        }
        //閫氳繃鍏挜瀵硅薄鑾峰彇鍏挜鐨勫熀鏈煙鍙傛暟銆�
        ECParameterSpec ecParameterSpec = publicKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        //閫氳繃鍏挜鍊煎拰鍏挜鍩烘湰鍙傛暟鍒涘缓鍏挜鍙傛暟瀵硅薄
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
        //鏍规嵁鍔犲瘑妯″紡瀹炰緥鍖朣M2鍏挜鍔犲瘑寮曟搸
        SM2Engine sm2Engine = new SM2Engine(mode);
        //鍒濆鍖栧姞瀵嗗紩鎿�
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes = null;
        try {
            //灏嗘槑鏂囧瓧绗︿覆杞崲涓烘寚瀹氱紪鐮佺殑瀛楄妭涓�
            byte[] in = data.getBytes("utf-8");
            //閫氳繃鍔犲瘑寮曟搸瀵瑰瓧鑺傛暟涓茶鍔犲瘑
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            System.out.println("SM2鍔犲瘑鏃跺嚭鐜板紓甯�:" + e.getMessage());
            e.printStackTrace();
        }
        //灏嗗姞瀵嗗悗鐨勫瓧鑺備覆杞崲涓哄崄鍏繘鍒跺瓧绗︿覆
        return Hex.toHexString(arrayOfBytes);
    }

    /**
     * @Description 绉侀挜瑙ｅ瘑
     * @Author msx
     * @param privateKeyHex SM2鍗佸叚杩涘埗绉侀挜
     * @param cipherData    瀵嗘枃鏁版嵁
     * @return String
     */
    public static String decrypt(String privateKeyHex, String cipherData) {
        return decrypt(getBCECPrivateKeyByPrivateKeyHex(privateKeyHex), cipherData, 1);
    }
    /**
     * @Description 绉侀挜瑙ｅ瘑
     * @Author msx
     * @param privateKeyHex SM2鍗佸叚杩涘埗绉侀挜
     * @param cipherData    瀵嗘枃鏁版嵁
     * @return String
     */
    public static String decrypt(String privateKeyHex, String cipherData,int modeType) {
        return decrypt(getBCECPrivateKeyByPrivateKeyHex(privateKeyHex), cipherData, modeType);
    }

    /**
     * @Description 绉侀挜瑙ｅ瘑
     * @Author msx
     * @param privateKeyHex SM绉侀挜
     * @param cipherData 瀵嗘枃鏁版嵁
     * @param modeType   瑙ｅ瘑妯″紡
     * @return
     */
    public static String decrypt(BCECPrivateKey privateKey, String cipherData, int modeType) {
        //瑙ｅ瘑妯″紡
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        if (modeType != 1)
            mode = SM2Engine.Mode.C1C2C3;
        //灏嗗崄鍏繘鍒跺瓧绗︿覆瀵嗘枃杞崲涓哄瓧鑺傛暟缁勶紙闇�瑕佷笌鍔犲瘑涓�鑷达紝鍔犲瘑鏄細鍔犲瘑鍚庣殑瀛楄妭鏁扮粍杞崲涓轰簡鍗佸叚杩涘埗瀛楃涓诧級
        byte[] cipherDataByte = Hex.decode(cipherData);
        //閫氳繃绉侀挜瀵硅薄鑾峰彇绉侀挜鐨勫熀鏈煙鍙傛暟銆�
        ECParameterSpec ecParameterSpec = privateKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        //閫氳繃绉侀挜鍊煎拰绉侀挜閽ュ熀鏈弬鏁板垱寤虹閽ュ弬鏁板璞�
        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(privateKey.getD(),
                ecDomainParameters);
        //閫氳繃瑙ｅ瘑妯″紡鍒涘缓瑙ｅ瘑寮曟搸骞跺垵濮嬪寲
        SM2Engine sm2Engine = new SM2Engine(mode);
        sm2Engine.init(false, ecPrivateKeyParameters);
        String result = null;
        try {
            //閫氳繃瑙ｅ瘑寮曟搸瀵瑰瘑鏂囧瓧鑺備覆杩涜瑙ｅ瘑
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            //灏嗚В瀵嗗悗鐨勫瓧鑺備覆杞崲涓簎tf8瀛楃缂栫爜鐨勫瓧绗︿覆锛堥渶瑕佷笌鏄庢枃鍔犲瘑鏃跺瓧绗︿覆杞崲鎴愬瓧鑺備覆鎵�鎸囧畾鐨勫瓧绗︾紪鐮佷繚鎸佷竴鑷达級
            result = new String(arrayOfBytes, "utf-8");
        } catch (Exception e) {
            System.out.println("SM2瑙ｅ瘑鏃跺嚭鐜板紓甯�" + e.getMessage());
        }
        return result;
    }
    //妞渾鏇茬嚎ECParameters ASN.1 缁撴瀯
    private static X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
    //妞渾鏇茬嚎鍏挜鎴栫閽ョ殑鍩烘湰鍩熷弬鏁般��
    private static ECParameterSpec ecDomainParameters = new ECParameterSpec(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

    /**
     * @Description 鍏挜瀛楃涓茶浆鎹负 BCECPublicKey 鍏挜瀵硅薄
     * @Author msx
     * @param pubKeyHex 64瀛楄妭鍗佸叚杩涘埗鍏挜瀛楃涓�(濡傛灉鍏挜瀛楃涓蹭负65瀛楄妭棣栦釜瀛楄妭涓�0x04锛氳〃绀鸿鍏挜涓洪潪鍘嬬缉鏍煎紡锛屾搷浣滄椂闇�瑕佸垹闄�)
     * @return BCECPublicKey SM2鍏挜瀵硅薄
     */
    public static BCECPublicKey getECPublicKeyByPublicKeyHex(String pubKeyHex) {
        //鎴彇64瀛楄妭鏈夋晥鐨凷M2鍏挜锛堝鏋滃叕閽ラ涓瓧鑺備负0x04锛�
        if (pubKeyHex.length() > 128) {
            pubKeyHex = pubKeyHex.substring(pubKeyHex.length() - 128);
        }
        //灏嗗叕閽ユ媶鍒嗕负x,y鍒嗛噺锛堝悇32瀛楄妭锛�
        String stringX = pubKeyHex.substring(0, 64);
        String stringY = pubKeyHex.substring(stringX.length());
        //灏嗗叕閽銆亂鍒嗛噺杞崲涓築igInteger绫诲瀷
        BigInteger x = new BigInteger(stringX, 16);
        BigInteger y = new BigInteger(stringY, 16);
        //閫氳繃鍏挜x銆亂鍒嗛噺鍒涘缓妞渾鏇茬嚎鍏挜瑙勮寖
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(x9ECParameters.getCurve().createPoint(x, y), ecDomainParameters);
        //閫氳繃妞渾鏇茬嚎鍏挜瑙勮寖锛屽垱寤哄嚭妞渾鏇茬嚎鍏挜瀵硅薄锛堝彲鐢ㄤ簬SM2鍔犲瘑鍙婇獙绛撅級
        return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

    /**
     * @Description 绉侀挜瀛楃涓茶浆鎹负 BCECPrivateKey 绉侀挜瀵硅薄
     * @Author msx
     * @param privateKeyHex 32瀛楄妭鍗佸叚杩涘埗绉侀挜瀛楃涓�
     * @return BCECPrivateKey SM2绉侀挜瀵硅薄
     */
    public static BCECPrivateKey getBCECPrivateKeyByPrivateKeyHex(String privateKeyHex) {
        //灏嗗崄鍏繘鍒剁閽ュ瓧绗︿覆杞崲涓築igInteger瀵硅薄
        BigInteger d = new BigInteger(privateKeyHex, 16);
        //閫氳繃绉侀挜鍜岀閽ュ煙鍙傛暟闆嗗垱寤烘き鍦嗘洸绾跨閽ヨ鑼�
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(d, ecDomainParameters);
        //閫氳繃妞渾鏇茬嚎绉侀挜瑙勮寖锛屽垱寤哄嚭妞渾鏇茬嚎绉侀挜瀵硅薄锛堝彲鐢ㄤ簬SM2瑙ｅ瘑鍜岀鍚嶏級
        return new BCECPrivateKey("EC", ecPrivateKeySpec, BouncyCastleProvider.CONFIGURATION);
    }

    public static void main(String[] args) {
        String publicKeyHex = null;
        String privateKeyHex = null;
        KeyPair keyPair = createECKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        if (publicKey instanceof BCECPublicKey) {
            //鑾峰彇65瀛楄妭闈炲帇缂╃缉鐨勫崄鍏繘鍒跺叕閽ヤ覆(0x04)
            publicKeyHex = Hex.toHexString(((BCECPublicKey) publicKey).getQ().getEncoded(false));
            System.out.println("---->SM2鍏挜锛�" + publicKeyHex);
        }
        PrivateKey privateKey = keyPair.getPrivate();
        if (privateKey instanceof BCECPrivateKey) {
            //鑾峰彇32瀛楄妭鍗佸叚杩涘埗绉侀挜涓�
            privateKeyHex = ((BCECPrivateKey) privateKey).getD().toString(16);
            System.out.println("---->SM2绉侀挜锛�" + privateKeyHex);
        }

        /**
         * 鍏挜鍔犲瘑
         */
        String data = "=========寰呭姞瀵嗘暟鎹�=========";

        //灏嗗崄鍏繘鍒跺叕閽ヤ覆杞崲涓� BCECPublicKey 鍏挜瀵硅薄
        String encryptData = encrypt(publicKeyHex, data);
        System.out.println("---->鍔犲瘑缁撴灉锛�" + encryptData);

        /**
         * 绉侀挜瑙ｅ瘑
         */
        //灏嗗崄鍏繘鍒剁閽ヤ覆杞崲涓� BCECPrivateKey 绉侀挜瀵硅薄
        data = decrypt(privateKeyHex, encryptData);
        System.out.println("---->瑙ｅ瘑缁撴灉锛�" + data);
        
        //--->SM2鍏挜锛�04339149dbdce1c3d0884df14d4d45e41bc95b86f14b5e5da1f4ef8a7604eaa52230096cae14fc6c1bb7805adcf564d23c77cd72d6c27547dc342e35751b3fd7cb
        //---->SM2绉侀挜锛歞855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193
        //---->鍔犲瘑缁撴灉锛�049153482f8c25dd59118d9cba3a2daa4a1603f7411fb4cf7fbbc4455ec9df83ada7f3c3c70bf91cc2920719365853dbe6befc0b1f7a70b5813c73bb2ced988e54eb4aeb7b862e35355595db6bf3da539cd68f086347904af1fddc04ea40e6d9136b13b71661f91c7b429cb01cd41ab2e62192425e1a60e5b41baef24cd41d3082ba
        //---->瑙ｅ瘑缁撴灉锛�=========寰呭姞瀵嗘暟鎹�=========

        encryptData="04d2ae2aaa753118b069ca356e733015558a0b511d651437122d7c25abe6bcf95d3838411731d3bb85531ca3043e1f94933f82324dbba23b0f6a450303e98297485b111e185debc02adc43d9ceb33d2549442dcac53a1fbcaa1c21d1b747aab1153f23e566f760b60203da7662cb18f9533dec91614660e1ce11961f26d9e873b73c1aa28ae474bd8a29815ee0d5dcf4644c39918f461d5d5a9aea992a";
        privateKeyHex="d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
        System.out.println("//--->SM2鍏挜锛�04339149dbdce1c3d0884df14d4d45e41bc95b86f14b5e5da1f4ef8a7604eaa52230096cae14fc6c1bb7805adcf564d23c77cd72d6c27547dc342e35751b3fd7cb\n//---->SM2绉侀挜锛歞855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193");
        data = decrypt(privateKeyHex, encryptData,0);
        System.out.println("瑙ｇ爜妯″紡C1C3C2:0"+"寰呰В瀵嗘暟鎹細"+ encryptData);
       
        String data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,0).getBytes()));
        System.out.println("---->瑙ｅ瘑缁撴灉锛�" + data);
        System.out.println("---->瑙ｅ瘑缁撴灉base64瑙ｇ爜锛�" + data1.toString());
        encryptData="04dc3c2bfe52e4b36c18ffd48475b5fb4e0b8247be70c70d15dce7f94c8beb76ac201197c563d8cfcd5b59d10465b05e1828b40d6b62d5f8065099f47b13e9ec7e8a2015427435936a6307b1e76a1d18011a6ec773ac0d124323d9d2f34dbc17f8f2eac1958995e5189b15da5242acef31339687b8fb5fc420b045c6996b9be4150d1e0cebfc3cd8f637646fef57ba3f4cf5209a6fb4db8a4204fa9533";
        privateKeyHex="d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
        
        System.out.println("瑙ｇ爜妯″紡C1C2C3=0琛ㄧず"+"寰呰В瀵嗘暟鎹細"+ encryptData);
        data = decrypt(privateKeyHex, encryptData,0);
        data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,0).getBytes()));
        System.out.println("---->瑙ｅ瘑缁撴灉锛�" + data);
        System.out.println("---->瑙ｅ瘑缁撴灉base64瑙ｇ爜锛�" + data1);
        encryptData="04c07e53a77f33c4d25cce550316a24260bab8b94962453fbf8a550f524ad33662512ce1c598d74653fbf794c9bbbe7bd8d71fc3df548a6ef501f6de42e7426ec00f044cd37739252530f9b9d8198ac06ccf4fb125e81830f34c32d9d83e8b5bfbcc287ff303b660f3eea1b9cae67275c274afcd833b44df8ec3d6348fafd01553340cfeaa4e3c0bd511fee96f8bb400e3ea95b9478ff50af69f872dc9594bb1fa7587f8ff911a5ad507b3ea64671e8894eb021639";
        System.out.println("瑙ｇ爜妯″紡C1C3C2=1琛ㄧず"+"寰呰В瀵嗘暟鎹細"+ encryptData);
        data = decrypt(privateKeyHex, encryptData,1);
        data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,1).getBytes()));
        System.out.println("---->瑙ｅ瘑缁撴灉锛�" + data);
        System.out.println("---->瑙ｅ瘑缁撴灉base64瑙ｇ爜锛�" + data1);
        
        encryptData="04702359cfe52f177307aa70631a36fc31005fe1217f6861a67dc4fe9dff66858703ad0c7aeb6e7a1678d5e88fe2ced731ebff0de059e03ef5b00074fd26c5da736a4db1688cdabe45e4a4c9c2e1b4d5266dc1909a09f9a4e6f37f2072e516348e4d9072339b5f9339d2b200953b0f783e5f2178a4d60b25c4545fe3348ad3c0a774a7de8b862bd3a92d90ab3df48fc5636ff20e1b1a9564266c6106b579f325fb2f8fa31b83e1fe39fa7c38c7";
        System.out.println("瑙ｇ爜妯″紡C1C3C2=1琛ㄧず"+"寰呰В瀵嗘暟鎹細"+ encryptData);
        data = decrypt(privateKeyHex, encryptData,1);
        data1= new String( java.util.Base64.getDecoder().decode(decrypt(privateKeyHex, encryptData,1).getBytes()));
        System.out.println("---->瑙ｅ瘑缁撴灉锛�" + data);
        System.out.println("---->瑙ｅ瘑缁撴灉base64瑙ｇ爜锛�" + data1);
    }
}


