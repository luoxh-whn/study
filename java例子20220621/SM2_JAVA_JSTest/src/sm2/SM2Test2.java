package sm2;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import lombok.Data;

@Data

public class SM2Test2 {

	public static void main(String[] args)   {
		// TODO Auto-generated method stub

		X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
		ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
		
		//2.��ʼ��������,���������
        try {
            keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
        } catch (NoSuchAlgorithmException e) {
           
        }
      
		
		AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
		 
		//˽Կ��16���Ƹ�ʽ���Լ����棬��ʽ��a2081b5b81fbea0b6b973a3ab6dbbbc65b1164488bf22d8ae2ff0b8260f64853
		BigInteger privatekey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate()).getD();
		String privateKeyHex = privatekey.toString(16);
		 
		System.out.println("private_key="+privateKeyHex);
		
	     ECPublicKeyParameters publicKeyParameters = (ECPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
	        ECPoint ecPoint = publicKeyParameters.getQ();
	        // �ѹ�Կ����map��,Ĭ��ѹ����Կ
	        // ��Կǰ���02����03��ʾ��ѹ����Կ,04��ʾδѹ����Կ,04��ʱ��,����ȥ��ǰ���04
	        String publicKey = Hex.toHexString(ecPoint.getEncoded(false));
	        System.out.println("public_key="+publicKey);   
	
		//��Կ��16���Ƹ�ʽ������ǰ�ˣ���ʽ��04813d4d97ad31bd9d18d785f337f683233099d5abed09cb397152d50ac28cc0ba43711960e811d90453db5f5a9518d660858a8d0c57e359a8bf83427760ebcbba
		//ECPoint ecPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
	        //String publicKey = Hex.toHexString(ecPoint.getEncoded(false));
	        
		
		//JS���ܲ���������
		String cipherData = "04dc3c2bfe52e4b36c18ffd48475b5fb4e0b8247be70c70d15dce7f94c8beb76ac201197c563d8cfcd5b59d10465b05e1828b40d6b62d5f8065099f47b13e9ec7e8a2015427435936a6307b1e76a1d18011a6ec773ac0d124323d9d2f34dbc17f8f2eac1958995e5189b15da5242acef31339687b8fb5fc420b045c6996b9be4150d1e0cebfc3cd8f637646fef57ba3f4cf5209a6fb4db8a4204fa9533";
		byte[] cipherDataByte = Hex.decode(cipherData);
		 
		//�ղŵ�˽ԿHex���Ȼ�ԭ˽Կ
		String privateKey = "d855cdde88941d79abf67bf7edb275464587b71920b213b7edc65e5540bc0193";
		BigInteger privateKeyD = new BigInteger(privateKey, 16);
		X9ECParameters sm2ECParameters1 = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters1 = new ECDomainParameters(sm2ECParameters1.getCurve(), sm2ECParameters1.getG(), sm2ECParameters1.getN());
		ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters1);
		 
		//��˽Կ����
		SM2Engine sm2Engine = new SM2Engine(); //��׼��ʽC1C2C3 �Ǳ�׼��ʽC1C3C2
		//SM2EngineExtend sm2Engine =new  SM2EngineExtend();
		
		sm2Engine.init(false, privateKeyParameters);
	
		//processBlock�õ�Base64��ʽ���ǵý���
		byte[] arrayOfBytes={};
		try {
			arrayOfBytes =  java.util.Base64.getDecoder().decode(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));
		} catch (InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		//�õ����ģ�SM2 Encryption Test
		String data = new String(arrayOfBytes);
		System.out.println("data="+data);
	}

	

}
