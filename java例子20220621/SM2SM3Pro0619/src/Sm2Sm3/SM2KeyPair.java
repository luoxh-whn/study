package Sm2Sm3;

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

	public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    

    public void setPublicKey(ECPoint publicKey) {
        this.publicKey = publicKey;
    }
  
    public SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
  
    public ECPoint getPublicKey() {
        return publicKey;
    }
  
    public BigInteger getPrivateKey() {
        return privateKey;
    }
     
    //HardPubKey:3059301306072A8648CE3D020106082A811CCF5501822D03420004+X+Y
    //SoftPubKey:04+X+Y
   
    public String getPubHexInSoft(){
    	
        return byteToHex(publicKey.getEncoded(false));
    }
    
    public String getPriHexInSoft(){
       return byteToHex(privateKey.toByteArray());
    }
  
    
	public void setPriHexInSoft(String priHex){
        
		   privateKey = new BigInteger(priHex, 16);
		
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
	
}