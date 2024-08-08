package testJsJavaSM2;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class SM2Utils {
    //���������Կ��
    public static SM2KeyPair generateKeyPair(){
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        System.out.println("��Կ: " + Util.byteToHex(publicKey.getEncoded()));
        System.out.println("˽Կ: " + Util.byteToHex(privateKey.toByteArray()));
        return  new SM2KeyPair(Util.byteToHex(publicKey.getEncoded()),Util.byteToHex(privateKey.toByteArray()));
    }



    //���ݼ���
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

//      System.out.println("C1 " + Util.byteToHex(c1.getEncoded()));
//      System.out.println("C2 " + Util.byteToHex(source));
//      System.out.println("C3 " + Util.byteToHex(c3));
        //C1 C2 C3ƴװ�ɼ����ִ�
        return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(source) + Util.byteToHex(c3);

    }

    //���ݽ���
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
        //�����ֽ�����ת��Ϊʮ�����Ƶ��ַ��� ���ȱ�ΪencryptedData.length * 2
        String data = Util.byteToHex(encryptedData);
        /***�ֽ�����ִ�
         * ��C1 = C1��־λ2λ + C1ʵ�岿��128λ = 130��
         * ��C3 = C3ʵ�岿��64λ  = 64��
         * ��C2 = encryptedData.length * 2 - C1����  - C2���ȣ�
         */
        byte[] c1Bytes = Util.hexToByte(data.substring(0,130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Util.hexToByte(data.substring(130,130 + 2 * c2Len));
        byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len,194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        //ͨ��C1ʵ���ֽ�������ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);

      //���ؽ��ܽ��
        return c2;
    }
    public static String SM2Encode(String publickey, String data) throws IOException {
        String res = encrypt(Util.hexToByte(publickey), data.getBytes("UTF-8"));
        return res;
    }
    public static String SM2Decode(String privatekey, String encrypt) throws IOException {
        byte[] decrypt = decrypt(Util.hexToByte(privatekey), Util.hexToByte(encrypt));
        return new String(decrypt, Charset.forName("UTF-8"));
    }
}
