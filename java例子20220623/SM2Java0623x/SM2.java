package SM2SM3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

import lombok.Data;




@Data
public class SM2 
{
	//測试參数
//	public static final String[] ecc_param = {
//	    "8542D69E4C044F18E8B92435BF6FF7DE457283915C45517D722EDB8B08F1DFC3", 
//	    "787968B4FA32C3FD2417842E73BBFEFF2F3C848B6831D7E0EC65228B3937E498", 
//	    "63E4C6D3B23B0C849CF84241484BFE48F61D59A5B16BA06E6E12D1DA27C5249A", 
//	    "8542D69E4C044F18E8B92435BF6FF7DD297720630485628D5AE74EE7C32E79B7", 
//	    "421DEBD61B62EAB6746434EBC3CC315E32220B3BADD50BDC4C4E6C147FEDD43D", 
//	    "0680512BCBB42C07D47349D2153B70C4E5D7FDFCBFA36EA1A85841B9E46E09A2" 
//	};
	
	//正式參数
	public static String[] ecc_param = { 
		"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
		"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC",
		"28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93",
		"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123",
		"32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7",
		"BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0"
	};
	
	public static SM2 Instance() 
	{
		return new SM2();
	}

	public final BigInteger ecc_p;
	public final BigInteger ecc_a;
	public final BigInteger ecc_b;
	public final BigInteger ecc_n;
	public final BigInteger ecc_gx;
	public final BigInteger ecc_gy;
	public final ECCurve ecc_curve;
	public final ECPoint ecc_point_g;
	public final ECDomainParameters ecc_bc_spec;
	public final ECKeyPairGenerator ecc_key_pair_generator;
	public final ECFieldElement ecc_gx_fieldelement;
	public final ECFieldElement ecc_gy_fieldelement;
	
	private static  SecureRandom random = new SecureRandom();
	
	
	
	//private static final int DIGEST_LENGTH = 32;
	
	private boolean debug = false;
	
	private ECKeyParameters ecKey;
	private ECDomainParameters ecParams;
	
	public SM2() 
	{
		this.ecc_p = new BigInteger(ecc_param[0], 16);
		this.ecc_a = new BigInteger(ecc_param[1], 16);
		this.ecc_b = new BigInteger(ecc_param[2], 16);
		this.ecc_n = new BigInteger(ecc_param[3], 16);
		this.ecc_gx = new BigInteger(ecc_param[4], 16);
		this.ecc_gy = new BigInteger(ecc_param[5], 16);

		this.ecc_gx_fieldelement = new Fp(this.ecc_p, this.ecc_gx);
		this.ecc_gy_fieldelement = new Fp(this.ecc_p, this.ecc_gy);

		this.ecc_curve = new ECCurve.Fp(this.ecc_p, this.ecc_a, this.ecc_b);
		this.ecc_point_g = new ECPoint.Fp(this.ecc_curve, this.ecc_gx_fieldelement, this.ecc_gy_fieldelement);

		this.ecc_bc_spec = new ECDomainParameters(this.ecc_curve, this.ecc_point_g, this.ecc_n);

		ECKeyGenerationParameters ecc_ecgenparam;
		ecc_ecgenparam = new ECKeyGenerationParameters(this.ecc_bc_spec, new SecureRandom());

		this.ecc_key_pair_generator = new ECKeyPairGenerator();
		this.ecc_key_pair_generator.init(ecc_ecgenparam);
		
		
	}
	
	
	

	/**
	 * 导出公钥到本地
	 *
	 * @param publicKey
	 * @param path
	 */
	public void exportPublicKey(ECPoint publicKey, String path) {
		File file = new File(path);
		try {
			if (!file.exists())
				file.createNewFile();
			byte buffer[] = publicKey.getEncoded(false);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(buffer);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从本地导入公钥
	 *
	 * @param path
	 * @return
	 */
	public ECPoint importPublicKey(String path) {
		File file = new File(path);
		try {
			if (!file.exists())
				return null;
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte buffer[] = new byte[16];
			int size;
			while ((size = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, size);
			}
			fis.close();
			//return ecParams.getCurve().decodePoint(baos.toByteArray());
			return ecc_curve.decodePoint(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 导出私钥到本地
	 *
	 * @param privateKey
	 * @param path
	 */
	public void exportPrivateKey(BigInteger privateKey, String path) {
		File file = new File(path);
		try {
			if (!file.exists())
				file.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(privateKey);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	

	

	
	


	

	
	/**
	 * 从本地导入私钥
	 *
	 * @param path
	 * @return
	 */
	public BigInteger importPrivateKey(String path) {
		File file = new File(path);
		try {
			if (!file.exists())
				return null;
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			BigInteger res = (BigInteger) (ois.readObject());
			ois.close();
			fis.close();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 判断是否在范围内
	 *
	 * @param param
	 * @param min
	 * @param max
	 * @return
	 */
	private boolean between(BigInteger param, BigInteger min, BigInteger max) {
		if (param.compareTo(min) >= 0 && param.compareTo(max) < 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断生成的公钥是否合法
	 *
	 * @param publicKey
	 * @return
	 */
	//private boolean checkPublicKey(ECPoint publicKey) {
	  public  boolean checkPublicKey(ECPoint publicKey) {
		if (!publicKey.isInfinity()) {

			BigInteger x = publicKey.getXCoord().toBigInteger();
			BigInteger y = publicKey.getYCoord().toBigInteger();

			if (between(x, new BigInteger("0"), ecc_p) && between(y, new BigInteger("0"), ecc_p)) {

				BigInteger xResult = x.pow(3).add(ecc_a.multiply(x)).add(ecc_b).mod(ecc_p);

				if (debug)
					System.out.println("xResult: " + xResult.toString());

				BigInteger yResult = y.pow(2).mod(ecc_p);

				if (debug)
					System.out.println("yResult: " + yResult.toString());

				if (yResult.equals(xResult) && publicKey.multiply(ecc_n).isInfinity()) {
					return true;
				}
			}
		}
		return false;
	}

	
	
	
	

	
	/**
	 * 随机数生成器
	 *
	 * @param max
	 * @return
	 */
	private static BigInteger random(BigInteger max) {

		BigInteger r = new BigInteger(256, random);
		// int count = 1;

		while (r.compareTo(max) >= 0) {
			r = new BigInteger(128, random);
			// count++;
		}

		// System.out.println("count: " + count);
		return r;
	}


	/**
	 * 生成密钥对
	 *
	 * @return
	 */
	public SM2KeyPair generateKeyPair() {

		
		BigInteger ecc_d = random(ecc_n.subtract(new BigInteger("1")));
		SM2KeyPair keyPair = new SM2KeyPair(ecc_point_g.multiply(ecc_d).normalize(), ecc_d);

		if (checkPublicKey(keyPair.getPublicKey())) {
			if (debug)
				System.out.println("generate key successfully");
			return keyPair;
		} else {
			if (debug)
				System.err.println("generate key failed");
			return null;
		}
	}
	
}