package Sm2Sm3;

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



public class Sm2Util {
    

	/**sm2���߲�������*/
    public static final String CRYPTO_NAME_SM2 = "sm2p256v1";
    /**sm2-ǩ��id*/
    public static final String SIGN_ID = "1234567812345678";
    /**
     * ��ȡsm2��Կ��
     * BC��ʹ�õĹ�Կ=64���ֽ�+1���ֽڣ�04��־λ����BC��ʹ�õ�˽Կ=32���ֽ�
     * SM2��Կ����ɲ����� ˽ԿD ����ԿX �� ��ԿY , ���Ƕ������ó���Ϊ64��16���Ƶ�HEX����ʾ��
     * <br/>SM2��Կ������ֱ����X+Y��ʾ , ���Ƕ��������һ��ͷ��������ѹ��ʱ:��Կ=��ͷ+��ԿX ����ʡ���˹�ԿY�Ĳ���
     *
     * @param compressed �Ƿ�ѹ����Կ�����ܽ��ܶ�ʹ��BC�����ʹ��ѹ����
     * @return
     */
    public static  String[] getSm2Keys(boolean compressed) {
        //��ȡһ��SM2���߲���
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        //����domain����
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //1.������Կ������
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        //2.��ʼ��������,���������
        try {
            keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        //3.������Կ��
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        ECPublicKeyParameters publicKeyParameters = (ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic();
        ECPoint ecPoint = publicKeyParameters.getQ();
        // �ѹ�Կ����map��,Ĭ��ѹ����Կ
        // ��Կǰ���02����03��ʾ��ѹ����Կ,04��ʾδѹ����Կ,04��ʱ��,����ȥ��ǰ���04
        String publicKey = Hex.toHexString(ecPoint.getEncoded(compressed));
        ECPrivateKeyParameters privateKeyParameters = (ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
        BigInteger intPrivateKey = privateKeyParameters.getD();
        // ��˽Կ����map��
        String privateKey = intPrivateKey.toString(16);
        String[] KeyPairOfString = new String[2];
        KeyPairOfString[0] = publicKey;
        KeyPairOfString[1] = privateKey;
 
        return KeyPairOfString;
    }
    /**
     * SM2�����㷨
     * @param publicKey ��Կ
     * @param data �����ܵ�����
     * @return ���ģ�BC����������Ĵ���04��ʶ�������BC��Խ�ʱ��Ҫȥ����ͷ��04
     */
    public static  String encrypt(String publicKey, String data){
        // �����������׼����
        return encrypt(publicKey, data, SM2EngineExtend.CIPHERMODE_NORM);
    }
 
    /**
     * SM2�����㷨
     * @param publicKey ��Կ
     * @param data �����ܵ�����
     * @param cipherMode �������з�ʽ0-C1C2C3��1-C1C3C2��
     * @return ���ģ�BC����������Ĵ���04��ʶ�������BC��Խ�ʱ��Ҫȥ����ͷ��04
     */
    public static  String encrypt(String publicKey, String data, int cipherMode) {
        // ��ȡһ��SM2���߲���
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        // ����ECC�㷨���������߷��̡���Բ����G�㡢������N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //��ȡ��Կ��
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // ��Կǰ���02����03��ʾ��ѹ����Կ��04��ʾδѹ����Կ, 04��ʱ�򣬿���ȥ��ǰ���04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);
 
        SM2EngineExtend sm2Engine = new SM2EngineExtend();
        // ����sm2Ϊ����ģʽ
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
     * SM2�����㷨
     * @param privateKey    ˽Կ
     * @param cipherData    ��������
     * @return
     */
    public static   String decrypt(String privateKey, String cipherData) {
        // // �����������׼����
        return decrypt(privateKey, cipherData, SM2EngineExtend.CIPHERMODE_NORM);
    }
 
    /**
     * SM2�����㷨
     * @param privateKey    ˽Կ
     * @param cipherData    ��������
     * @param cipherMode �������з�ʽ0-C1C2C3��1-C1C3C2��
     * @return
     */
    public static  String decrypt(String privateKey, String cipherData, int cipherMode) {
        // ʹ��BC��ӽ���ʱ������04��ͷ�����������ǰ��û��04����
        if (!cipherData.startsWith("04")) {
            cipherData = "04" + cipherData;
        }
        byte[] cipherDataByte = Hex.decode(cipherData);
 
        //��ȡһ��SM2���߲���
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(CRYPTO_NAME_SM2);
        //����domain����
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
 
        BigInteger privateKeyD = new BigInteger(privateKey, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);
 
        SM2EngineExtend sm2Engine = new SM2EngineExtend();
        // ����sm2Ϊ����ģʽ
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
     * ˽Կǩ��
     *
     * @param data δ���ܵ���������
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
     * ��Կ��ǩ
     *
     * @param data δ���ܵ���������
     * @param publicKey
     * @param signature ǩ��
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
}