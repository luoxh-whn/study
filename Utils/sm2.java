package com.study.lin.cherrypro.Utils;

import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.security.Security;

/**
 * @Auther: lin
 * @Date: 2021/03/05/14:34
 * @Description:
 */
public class sm2 {
    public static void main(String[] args) {
        {
            try {

                byte[] pkcs12 = FileUtil.readFile("D:/123456.pfx");
                Security.addProvider(new BouncyCastleProvider());
                BCECPublicKey  pubKey = SM2CertUtil.getPublicKeyFromPfx(pkcs12,  "111111");
                BCECPrivateKey priKey = SM2CertUtil.getPrivateKeyFromPfx(pkcs12, "111111");
                String s2= "BB+brzw1mApr0hV447nL3eNmLnWcD4FswR3SeQaKwt/m3A1i9abvRb6Zys0TtJsLXgDlJxdveeHbMmvrfqEqWtE=vv3VVkZog1XzX2GapKHHYbBjKndPS7o4HdbO8G6aXvy9OpSnSy0hG8RZM8OcUuR0X4UIUmf0+cSxjQSxL2nBKLNcq7vlTCv4qtZBe/zFvinPt7VbzOVuYK+EMKiqnA==+WX9nIjlqL7oSbjd3DJWN8G7Z4LTdXMf85PY7k6UPt0=\n";
                String s1= "BHjdSeLfgc/K8Od6CV4+KzefP9/d3ojzG5oyojVaBSJLooedGxQC5lAopHziS9LYet2p5i3rMn+njYOXaZOdKZ8=Gc0WL+s5jR60Qv0EZuPtvvqzBdRD+nYnxSLraqxcMOpDpzqYMIXMt5w/jbClIjBHIjEQd9/K5B9b0tyxOjNZ9NHyM6QAN9LJiGqez6FHNIoY1NPoH4chGt5lbLguEf9KMBNCCg119H/+lx4Ple76QCjDEU8/lJHWq2w=\n";

                System.out.println("Pri Hex:"
                        + ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase());
                System.out.println("Pub X Hex:"
                        + ByteUtils.toHexString(pubKey.getQ().getAffineXCoord().getEncoded()).toUpperCase());
                System.out.println("Pub X Hex:"
                        + ByteUtils.toHexString(pubKey.getQ().getAffineYCoord().getEncoded()).toUpperCase());
                System.out.println("Pub Point Hex:"
                        + ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase());


                byte[] decryptedData1 = SM2Util.decrypt(SM2Engine.Mode.C1C2C3, priKey, Base64Utils.decode(s1));
                System.out.println("The first  SM2 decrypt result:\n" +new String(decryptedData1));

                byte[] decryptedData2 = SM2Util.decrypt(SM2Engine.Mode.C1C2C3, priKey, Base64Utils.decode(s2));
                System.out.println("The second SM2 decrypt result:\n" +new String(decryptedData2));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
