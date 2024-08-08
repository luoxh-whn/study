package Test;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;


/**
 * bcprov-jdk15on �汾����(1.61-1.68)
 * @author dashou
 * @date 2021-4-13
 */

public class SM2Util {
    private BouncyCastleProvider provider;
    // ��ȡSM2��ز���
    private X9ECParameters parameters;
    // ��Բ���߲������
    private ECParameterSpec ecParameterSpec;
    // ��Բ���߲������
	private ECParameterSpec ecParameterSpec;
	// ��ȡ��Բ����KEY������
    private KeyFactory keyFactory;

    private SM2Util(){
        try {
            provider = new BouncyCastleProvider();
            parameters = GMNamedCurves.getByName("sm2p256v1");
            ecParameterSpec = new ECParameterSpec(parameters.getCurve(),
                    parameters.getG(), parameters.getN(), parameters.getH());
            keyFactory = KeyFactory.getInstance("EC", provider);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SM2�㷨������Կ��
     *
     * @return ��Կ����Ϣ
     */
    public KeyPair generateSm2KeyPair() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // ��ȡһ����Բ�������͵���Կ��������
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", provider);
        SecureRandom random = new SecureRandom();
        // ʹ��SM2���㷨�����ʼ����Կ������
        kpg.initialize(sm2Spec, random);
        // ��ȡ��Կ��
        KeyPair keyPair = kpg.generateKeyPair();
        return keyPair;
    }

    /**
     * ����
     *
     * @param input  �������ı�
     * @param pubKey ��Կ
     * @return
     */
    public String encode(String input, String pubKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException,
            InvalidKeySpecException, InvalidKeyException {
        // ��ȡSM2��ز���
        X9ECParameters parameters = GMNamedCurves.getByName("sm2p256v1");
        // ��Բ���߲������
        ECParameterSpec ecParameterSpec = new ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH());
        // ����ԿHEX�ַ���ת��Ϊ��Բ���߶�Ӧ�ĵ�
        ECPoint ecPoint = parameters.getCurve().decodePoint(Hex.decode(pubKey));
        // ��ȡ��Բ����KEY������
        KeyFactory keyFactory = KeyFactory.getInstance("EC", provider);
        BCECPublicKey key = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
        // ��ȡSM2������
        Cipher cipher = Cipher.getInstance("SM2", provider);
        // ��ʼ��Ϊ����ģʽ
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // ���ܲ�����Ϊbase64��ʽ
        return  Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
    }

    /**
     * ����
     *
     * @param input  �������ı�
     * @param prvKey ˽Կ
     * @return
     */
    public byte[] decoder(String input, String prvKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // ��ȡSM2������
        Cipher cipher = Cipher.getInstance("SM2", provider);
        // ��˽ԿHEX�ַ���ת��ΪXֵ
        BigInteger bigInteger = new BigInteger(prvKey, 16);
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger,
                ecParameterSpec));
        // ��ʼ��Ϊ����ģʽ
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        // ����
        return cipher.doFinal(Base64.getDecoder().decode(input));
    }

    /**
     * ǩ��
     *
     * @param plainText ��ǩ���ı�
     * @param prvKey    ˽Կ
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public String sign(String plainText, String prvKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        // ����ǩ������
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
        // ��˽ԿHEX�ַ���ת��ΪXֵ
        BigInteger bigInteger = new BigInteger(prvKey, 16);
        BCECPrivateKey privateKey = (BCECPrivateKey) keyFactory.generatePrivate(new ECPrivateKeySpec(bigInteger,
                ecParameterSpec));
        // ��ʼ��Ϊǩ��״̬
        signature.initSign(privateKey);
        // ����ǩ���ֽ�
        signature.update(plainText.getBytes());
        // ǩ��
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public boolean verify(String plainText, String signatureValue, String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        // ����ǩ������
        Signature signature = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), provider);
        // ����ԿHEX�ַ���ת��Ϊ��Բ���߶�Ӧ�ĵ�
        ECPoint ecPoint = parameters.getCurve().decodePoint(Hex.decode(pubKey));
        BCECPublicKey key = (BCECPublicKey) keyFactory.generatePublic(new ECPublicKeySpec(ecPoint, ecParameterSpec));
        // ��ʼ��Ϊ��ǩ״̬
        signature.initVerify(key);
        signature.update(plainText.getBytes());
        return signature.verify(Base64.getDecoder().decode(signatureValue));
    }

    /**
     * ֤����ǩ
     *
     * @param certStr      ֤�鴮
     * @param plaintext    ǩ��ԭ��
     * @param signValueStr ǩ������ǩ��ֵ �˴���ǩ��ֵʵ���Ͼ��� R��S��sequence
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public boolean certVerify(String certStr, String plaintext, String signValueStr)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, CertificateException {

        byte[] signValue = Base64.getDecoder().decode(signValueStr);
        /*
         * ����֤��
         */
        CertificateFactory factory = new CertificateFactory();
        X509Certificate certificate = (X509Certificate) factory
                .engineGenerateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(certStr)));
        // ��֤ǩ��
        Signature signature = Signature.getInstance(certificate.getSigAlgName(), provider);
        signature.initVerify(certificate);
        signature.update(plaintext.getBytes());
        return signature.verify(signValue);
    }

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String str = "�����ܲ���һ��ͨ��";
        SM2Util sm2 = new SM2Util();
        KeyPair keyPair = sm2.generateSm2KeyPair();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        // �õ���Կ
        String pubKey = new String(Hex.encode(publicKey.getQ().getEncoded(true)));//trueѹ����ʽ
        String prvKey = privateKey.getD().toString(16);
        System.out.println("Private Key: " + prvKey);
        System.out.println("Public Key: " + pubKey);
        // �ӽ��ܲ���
          try {
            System.out.println("����ǰ��" + str);
            String encode = sm2.encode(str, pubKey);
            System.out.println("���ܺ�" + encode);
            String decoder = new String(sm2.decoder(encode, prvKey));
            System.out.println("���ܺ�" + decoder);
        } catch (Exception e) {
            System.out.println("�ӽ��ܲ��Դ���");
        }
        // ǩ������ǩ����
        try {
            System.out.println("ǩ��Դ���ݣ�" + str);
            String signStr = sm2.sign(str, prvKey);
            System.out.println("ǩ�������ݣ�" + signStr);
            boolean verify = sm2.verify(str, signStr, pubKey);
            System.out.println("ǩ����֤�����" + verify);
        } catch (Exception e) {
            System.out.println("ǩ������ǩ���Դ���");
        }
    }
}