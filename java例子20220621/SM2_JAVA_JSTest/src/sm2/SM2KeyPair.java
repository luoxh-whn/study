package sm2;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;
  
/**
 *   <B>˵ ��<B/>:SM2��˽Կʵ����
 */
public class SM2KeyPair {
     
    /** ��Կ */
    private  ECPoint publicKey;
     
    /** ˽Կ */
    private BigInteger privateKey;
  
   public  SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
  
    public ECPoint getPublicKey() {
        return publicKey;
    }
  
    public BigInteger getPrivateKey() {
        return privateKey;
    }
    
    
    
     
}