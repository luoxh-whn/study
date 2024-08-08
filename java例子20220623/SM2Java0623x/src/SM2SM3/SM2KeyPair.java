package SM2SM3;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;




/**
 *   <B>说 明<B/>:SM2公私钥实体类
 */
public class SM2KeyPair {
     
    /** 公钥 */
    private  ECPoint publicKey;
     
    /** 私钥 */
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
    	
        return Util.byteToHex(publicKey.getEncoded(false));
    }
    
    public String getPriHexInSoft(){
       return Util.byteToHex(privateKey.toByteArray());
    }
  
    
	public void setPriHexInSoft(String priHex){
        
		   privateKey = new BigInteger(priHex, 16);
		
	}

	
}