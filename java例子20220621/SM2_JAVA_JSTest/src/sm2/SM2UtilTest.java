package sm2;

import java.util.Arrays;

import org.bouncycastle.math.ec.ECPoint;

/**
 *   <B>说 明<B/>:SM2非对称加解密工具类测试
 */
public class SM2UtilTest {
  
    /** 元消息串 */
    private static String M = "哈哈哈，&*&…………&、、//\\!@#$%^&*()物品woyebuzhidaowozijiqiaodesha!@#$%^&*())))))ooooooooppppppppppppppppppplllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkffffffffffffffffffffffffffffffffffffff";
     
    public static void main(String[] args) {
    	//ECPoint public1 =(90649ceb3811f552a821f3ccdcb34f946d66b00f7a8f4cffacb05cb13015db52,6262094c6c2c08cbc82584452473124c34041086badabe63863c3baf249ebbf7,1,fffffffeffffffffffffffffffffffffffffffff00000000fffffffffffffffc);
       // String private1="19465513443183782198140879323737321984603726240734928278292019499241960375650";
    	SM2Util sm2 = new SM2Util();
        SM2KeyPair keyPair = sm2.generateKeyPair();
       System.out.println("public="+keyPair.getPublicKey());
       System.out.println("private="+keyPair.getPrivateKey());
        byte[] data = sm2.encrypt(M,keyPair.getPublicKey());
     
        System.out.println("data is:"+Arrays.toString(data));
        sm2.decrypt(data, keyPair.getPrivateKey());//71017045908707391874054405929626258767106914144911649587813342322113806533034
    }
     
}