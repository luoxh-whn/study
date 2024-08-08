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

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECFieldElement.Fp;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

/**
 * 对org.bouncycastle:bcprov-jdk15on:1.57扩展 <br/>
 * BC库加密结果是按C1C2C3，国密标准是C1C3C2（加密芯片也是这个排列）， <br/>
 * 本扩展主要实现加密结果排列方式可选
 */
public class SM2EngineExtend123ok {

	// 国密参数
	public static String[] ecc_param = { "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
			"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC",
			"28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93",
			"FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123",
			"32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7",
			"BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0" };

	public final BigInteger p;
	public final BigInteger a;
	public final BigInteger b;
	public final BigInteger n;
	public final BigInteger gx;
	public final BigInteger gy;
	public final ECCurve curve;
	public final ECPoint point_g;
	public final ECDomainParameters bc_spec;
	public final ECKeyPairGenerator key_pair_generator;
	public final ECFieldElement gx_fieldelement;
	public final ECFieldElement gy_fieldelement;
	private Digest digest ;
	private static  SecureRandom random = new SecureRandom();
	/** 是否为加密模式 */
	private boolean forEncryption;
	private ECKeyParameters ecKey;
	private ECDomainParameters ecParams;
	private int curveLength;

	/** 密文排序方式 */
	private int cipherMode;

	/** BC库默认排序方式-C1C2C3 */
	public static int CIPHERMODE_BC = 0;
	/** 国密标准排序方式-C1C3C2 */
	public static int CIPHERMODE_NORM = 1;
	
	private static final int DIGEST_LENGTH = 32;
	private boolean debug = false;

	@SuppressWarnings("deprecation")
	public SM2EngineExtend123ok() {

		this.p = new BigInteger(ecc_param[0], 16);
		this.a = new BigInteger(ecc_param[1], 16);
		this.b = new BigInteger(ecc_param[2], 16);
		this.n = new BigInteger(ecc_param[3], 16);
		this.gx = new BigInteger(ecc_param[4], 16);
		this.gy = new BigInteger(ecc_param[5], 16);

		this.gx_fieldelement = new Fp(this.p, this.gx);
		this.gy_fieldelement = new Fp(this.p, this.gy);

		this.curve = new ECCurve.Fp(this.p, this.a, this.b);
		this.point_g = new ECPoint.Fp(this.curve, this.gx_fieldelement, this.gy_fieldelement);

		this.bc_spec = new ECDomainParameters(this.curve, this.point_g, this.n);

		ECKeyGenerationParameters ecgenparam;
		ecgenparam = new ECKeyGenerationParameters(this.bc_spec, new SecureRandom());

		this.key_pair_generator = new ECKeyPairGenerator();
		this.key_pair_generator.init(ecgenparam);
		
		this.digest = new SM3Digest();
	}

	public void SM2EngineExtend(Digest digest) {

		// this.digest = digest;
		this.digest = digest;

	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
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
	 * 设置密文排序方式
	 * 
	 * @param cipherMode
	 */
	public void setCipherMode(int cipherMode) {
		this.cipherMode = cipherMode;
	}

	/**
	 * 默认初始化方法，使用国密排序标准
	 * 
	 * @param forEncryption
	 *            - 是否以加密模式初始化
	 * @param param
	 *            - 曲线参数
	 */
	public void init(boolean forEncryption, CipherParameters param) {
		init(forEncryption, CIPHERMODE_NORM, param);
	}

	/**
	 * 默认初始化方法，使用国密排序标准
	 * 
	 * @param forEncryption
	 *            是否以加密模式初始化
	 * @param cipherMode
	 *            加密数据排列模式：1-标准排序；0-BC默认排序
	 * @param param
	 *            曲线参数
	 */
	public void init(boolean forEncryption, int cipherMode, CipherParameters param) {
		this.forEncryption = forEncryption;
		this.cipherMode = cipherMode;
		if (forEncryption) {
			ParametersWithRandom rParam = (ParametersWithRandom) param;

			ecKey = (ECKeyParameters) rParam.getParameters();
			ecParams = ecKey.getParameters();

			ECPoint s = ((ECPublicKeyParameters) ecKey).getQ().multiply(ecParams.getH());
			if (s.isInfinity()) {
				throw new IllegalArgumentException("invalid key: [h]Q at infinity");
			}

			random = rParam.getRandom();
		} else {
			ecKey = (ECKeyParameters) param;
			ecParams = ecKey.getParameters();
		}

		curveLength = (ecParams.getCurve().getFieldSize() + 7) / 8;
	}

	/**
	 * 加密或解密输入数据
	 * 
	 * @param in
	 * @param inOff
	 * @param inLen
	 * @return
	 * @throws InvalidCipherTextException
	 */
	public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
		if (forEncryption) {
			// 加密
			return encrypt(in, inOff, inLen);
		} else {
			return decrypt(in, inOff, inLen);
		}
	}

	/**
	 * 加密实现，根据cipherMode输出指定排列的结果，默认按标准方式排列
	 * 
	 * @param in
	 * @param inOff
	 * @param inLen
	 * @return
	 * @throws InvalidCipherTextException
	 */
	private byte[] encrypt(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
		byte[] c2 = new byte[inLen];

		System.arraycopy(in, inOff, c2, 0, c2.length);

		byte[] c1;
		ECPoint kPB;
		do {
			BigInteger k = nextK();

			ECPoint c1P = ecParams.getG().multiply(k).normalize();

			c1 = c1P.getEncoded(false);

			kPB = ((ECPublicKeyParameters) ecKey).getQ().multiply(k).normalize();

			kdf(digest, kPB, c2);
		} while (notEncrypted(c2, in, inOff));

		byte[] c3 = new byte[digest.getDigestSize()];

		addFieldElement(digest, kPB.getAffineXCoord());
		digest.update(in, inOff, inLen);
		addFieldElement(digest, kPB.getAffineYCoord());

		digest.doFinal(c3, 0);
		if (cipherMode == CIPHERMODE_NORM) {
			return Arrays.concatenate(c1, c3, c2);
		}
		return Arrays.concatenate(c1, c2, c3);
	}

	/**
	 * 解密实现，默认按标准排列方式解密，解密时解出c2部分原文并校验c3部分
	 * 
	 * @param in
	 * @param inOff
	 * @param inLen
	 * @return
	 * @throws InvalidCipherTextException
	 */
	private byte[] decrypt(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
		byte[] c1 = new byte[curveLength * 2 + 1];

		System.arraycopy(in, inOff, c1, 0, c1.length);

		ECPoint c1P = ecParams.getCurve().decodePoint(c1);

		// ECPoint s = c1P.multiply(ecParams.getH());
		ECPoint s = c1P.multiply(ecParams.getH());
		if (s.isInfinity()) {
			throw new InvalidCipherTextException("[h]C1 at infinity");
		}

		c1P = c1P.multiply(((ECPrivateKeyParameters) ecKey).getD()).normalize();

		byte[] c2 = new byte[inLen - c1.length - digest.getDigestSize()];
		if (cipherMode == CIPHERMODE_BC) {
			System.arraycopy(in, inOff + c1.length, c2, 0, c2.length);
		} else {
			// C1 C3 C2
			System.arraycopy(in, inOff + c1.length + digest.getDigestSize(), c2, 0, c2.length);
		}

		kdf(digest, c1P, c2);

		byte[] c3 = new byte[digest.getDigestSize()];

		addFieldElement(digest, c1P.getAffineXCoord());
		digest.update(c2, 0, c2.length);
		addFieldElement(digest, c1P.getAffineYCoord());

		digest.doFinal(c3, 0);

		int check = 0;
		// 检查密文输入值C3部分和由摘要生成的C3是否一致
		if (cipherMode == CIPHERMODE_BC) {
			for (int i = 0; i != c3.length; i++) {
				check |= c3[i] ^ in[c1.length + c2.length + i];
			}
		} else {
			for (int i = 0; i != c3.length; i++) {
				check |= c3[i] ^ in[c1.length + i];
			}
		}

		clearBlock(c1);
		clearBlock(c3);

		if (check != 0) {
			clearBlock(c2);
			throw new InvalidCipherTextException("invalid cipher text");
		}

		return c2;
	}

	private boolean notEncrypted(byte[] encData, byte[] in, int inOff) {
		for (int i = 0; i != encData.length; i++) {
			if (encData[i] != in[inOff]) {
				return false;
			}
		}

		return true;
	}

	private void kdf(Digest digest, ECPoint c1, byte[] encData) {
		int ct = 1;
		int v = digest.getDigestSize();

		byte[] buf = new byte[digest.getDigestSize()];
		int off = 0;

		for (int i = 1; i <= ((encData.length + v - 1) / v); i++) {
			addFieldElement(digest, c1.getAffineXCoord());
			addFieldElement(digest, c1.getAffineYCoord());
			digest.update((byte) (ct >> 24));
			digest.update((byte) (ct >> 16));
			digest.update((byte) (ct >> 8));
			digest.update((byte) ct);

			digest.doFinal(buf, 0);

			if (off + buf.length < encData.length) {
				xor(encData, buf, off, buf.length);
			} else {
				xor(encData, buf, off, encData.length - off);
			}

			off += buf.length;
			ct++;
		}
	}

	private void xor(byte[] data, byte[] kdfOut, int dOff, int dRemaining) {
		for (int i = 0; i != dRemaining; i++) {
			data[dOff + i] ^= kdfOut[i];
		}
	}

	private BigInteger nextK() {
		int qBitLength = ecParams.getN().bitLength();

		BigInteger k;
		do {
			k = new BigInteger(qBitLength, random);
		} while (k.equals(ECConstants.ZERO) || k.compareTo(ecParams.getN()) >= 0);

		return k;
	}

	private void addFieldElement(Digest digest, ECFieldElement v) {
		byte[] p = BigIntegers.asUnsignedByteArray(curveLength, v.toBigInteger());

		digest.update(p, 0, p.length);
	}

	/**
	 * clear possible sensitive data
	 */
	private void clearBlock(byte[] block) {
		for (int i = 0; i != block.length; i++) {
			block[i] = 0;
		}
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
			return curve.decodePoint(baos.toByteArray());
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
		 * 判断生成的公钥是否合法
		 *
		 * @param publicKey
		 * @return
		 */
		private boolean checkPublicKey(ECPoint publicKey) {

			if (!publicKey.isInfinity()) {

				BigInteger x = publicKey.getXCoord().toBigInteger();
				BigInteger y = publicKey.getYCoord().toBigInteger();

				if (between(x, new BigInteger("0"), p) && between(y, new BigInteger("0"), p)) {

					BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);

					if (debug)
						System.out.println("xResult: " + xResult.toString());

					BigInteger yResult = y.pow(2).mod(p);

					if (debug)
						System.out.println("yResult: " + yResult.toString());

					if (yResult.equals(xResult) && publicKey.multiply(n).isInfinity()) {
						return true;
					}
				}
			}
			return false;
		}
	
		
		/**
		 * 生成密钥对
		 *
		 * @return
		 */
		public SM2KeyPair generateKeyPair() {

			
			BigInteger d = random(n.subtract(new BigInteger("1")));
			SM2KeyPair keyPair = new SM2KeyPair(point_g.multiply(d).normalize(), d);

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

}