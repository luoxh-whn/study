package Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
public class SM2 {
	private static BigInteger n = new BigInteger(
			"FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);
	private static BigInteger p = new BigInteger(
			"FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);
	private static BigInteger a = new BigInteger(
			"FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);
	private static BigInteger b = new BigInteger(
			"28E9FA9E" + "9D9F5E34" + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41" + "4D940E93", 16);
	private static BigInteger gx = new BigInteger(
			"32C4AE2C" + "1F198119" + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589" + "334C74C7", 16);
	private static BigInteger gy = new BigInteger(
			"BC3736A2" + "F4F6779C" + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5" + "2139F0A0", 16);
	private static ECDomainParameters ecc_bc_spec;
	private static int w = (int) Math.ceil(n.bitLength() * 1.0 / 2) - 1;
	private static BigInteger _2w = new BigInteger("2").pow(w);
	private static final int DIGEST_LENGTH = 32;
	
	//private static SecureRandom random = new SecureRandom();//
	private static SecureRandom random =new SecureRandom();
	private static ECCurve.Fp curve;
	private static ECPoint G;
	private boolean debug = false;
	private static X9ECParameters x9ECParameters =    GMNamedCurves.getByName(Constant.CRYPTO_NAME_SM2);
    //��Բ���߹�Կ��˽Կ�Ļ����������
    private static ECParameterSpec ecDomainParameters = new ECParameterSpec(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * ��16���ƴ�ӡ�ֽ�����
	 *
	 * @param b
	 */
	public static void printHexString(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
		}
		System.out.println();
	}

	/**
	 * �����������
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
	 * �ж��ֽ������Ƿ�ȫ0
	 *
	 * @param buffer
	 * @return
	 */
	private boolean allZero(byte[] buffer) {
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] != 0)
				return false;
		}
		return true;
	}

	/**
	 * ��Կ����
	 *
	 * @param input
	 *            ����ԭ��
	 * @param publicKey
	 *            ��Կ
	 * @return
	 */
	public byte[] encrypt(String input, ECPoint publicKey) {

		byte[] inputBuffer = input.getBytes();
		if (debug)
			printHexString(inputBuffer);

		byte[] C1Buffer;
		ECPoint kpb;
		byte[] t;
		do {
			/* 1 ���������k��k����[1, n-1] */
			BigInteger k = random(n);
			if (debug) {
				System.out.print("k: ");
				printHexString(k.toByteArray());
			}

			/* 2 ������Բ���ߵ�C1 = [k]G = (x1, y1) */
			ECPoint C1 = G.multiply(k);
			C1Buffer = C1.getEncoded(false);
			if (debug) {
				System.out.print("C1: ");
				printHexString(C1Buffer);
			}

			/*
			 * 3 ������Բ���ߵ� S = [h]Pb
			 */
			BigInteger h = ecc_bc_spec.getH();
			if (h != null) {
				ECPoint S = publicKey.multiply(h);
				if (S.isInfinity())
					throw new IllegalStateException();
			}

			/* 4 ���� [k]PB = (x2, y2) */
			kpb = publicKey.multiply(k).normalize();

			/* 5 ���� t = KDF(x2||y2, klen) */
			byte[] kpbBytes = kpb.getEncoded(false);
			t = KDF(kpbBytes, inputBuffer.length);
			// DerivationFunction kdf = new KDF1BytesGenerator(new
			// ShortenedDigest(new SHA256Digest(), DIGEST_LENGTH));
			//
			// t = new byte[inputBuffer.length];
			// kdf.init(new ISO18033KDFParameters(kpbBytes));
			// kdf.generateBytes(t, 0, t.length);
		} while (allZero(t));

		/* 6 ����C2=M^t */
		byte[] C2 = new byte[inputBuffer.length];
		for (int i = 0; i < inputBuffer.length; i++) {
			C2[i] = (byte) (inputBuffer[i] ^ t[i]);
		}

		/* 7 ����C3 = Hash(x2 || M || y2) */
		byte[] C3 = sm3hash(kpb.getXCoord().toBigInteger().toByteArray(), inputBuffer,
				kpb.getYCoord().toBigInteger().toByteArray());

		/* 8 ������� C=C1 || C2 || C3 */

		byte[] encryptResult = new byte[C1Buffer.length + C2.length + C3.length];

		System.arraycopy(C1Buffer, 0, encryptResult, 0, C1Buffer.length);
		System.arraycopy(C2, 0, encryptResult, C1Buffer.length, C2.length);
		System.arraycopy(C3, 0, encryptResult, C1Buffer.length + C2.length, C3.length);

		if (debug) {
			System.out.print("����: ");
			printHexString(encryptResult);
		}

		return encryptResult;
	}

	/**
	 * ˽Կ����
	 *
	 * @param encryptData
	 *            ���������ֽ�����
	 * @param privateKey
	 *            ����˽Կ
	 * @return
	 */
	public String decrypt(byte[] encryptData, BigInteger privateKey) {

		if (debug)
			System.out.println("encryptData length: " + encryptData.length);

		byte[] C1Byte = new byte[65];
		System.arraycopy(encryptData, 0, C1Byte, 0, C1Byte.length);

		ECPoint C1 = curve.decodePoint(C1Byte).normalize();

		/*
		 * ������Բ���ߵ� S = [h]C1 �Ƿ�Ϊ�����
		 */
		BigInteger h = ecc_bc_spec.getH();
		if (h != null) {
			ECPoint S = C1.multiply(h);
			if (S.isInfinity())
				throw new IllegalStateException();
		}
		/* ����[dB]C1 = (x2, y2) */
		ECPoint dBC1 = C1.multiply(privateKey).normalize();

		/* ����t = KDF(x2 || y2, klen) */
		byte[] dBC1Bytes = dBC1.getEncoded(false);
		int klen = encryptData.length - 65 - DIGEST_LENGTH;
		byte[] t = KDF(dBC1Bytes, klen);
		// DerivationFunction kdf = new KDF1BytesGenerator(new
		// ShortenedDigest(new SHA256Digest(), DIGEST_LENGTH));
		// if (debug)
		// System.out.println("klen = " + klen);
		// kdf.init(new ISO18033KDFParameters(dBC1Bytes));
		// kdf.generateBytes(t, 0, t.length);

		if (allZero(t)) {
			System.err.println("all zero");
			throw new IllegalStateException();
		}

		/* 5 ����M'=C2^t */
		byte[] M = new byte[klen];
		for (int i = 0; i < M.length; i++) {
			M[i] = (byte) (encryptData[C1Byte.length + i] ^ t[i]);
		}
		if (debug)
			printHexString(M);

		/* 6 ���� u = Hash(x2 || M' || y2) �ж� u == C3�Ƿ���� */
		byte[] C3 = new byte[DIGEST_LENGTH];

		if (debug)
			try {
				System.out.println("M = " + new String(M, "UTF8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		System.arraycopy(encryptData, encryptData.length - DIGEST_LENGTH, C3, 0, DIGEST_LENGTH);
		byte[] u = sm3hash(dBC1.getXCoord().toBigInteger().toByteArray(), M,
				dBC1.getYCoord().toBigInteger().toByteArray());
		if (Arrays.equals(u, C3)) {
			if (debug)
				System.out.println("���ܳɹ�");
			try {
				return new String(M, "UTF8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			if (debug) {
				System.out.print("u = ");
				printHexString(u);
				System.out.print("C3 = ");
				printHexString(C3);
				System.err.println("������֤ʧ��");
			}
			return null;
		}

	}

	// /**
	// * SHAժҪ
	// * @param x2
	// * @param M
	// * @param y2
	// * @return
	// */
	// private byte[] calculateHash(BigInteger x2, byte[] M, BigInteger y2) {
	// ShortenedDigest digest = new ShortenedDigest(new SHA256Digest(),
	// DIGEST_LENGTH);
	// byte[] buf = x2.toByteArray();
	// digest.update(buf, 0, buf.length);
	// digest.update(M, 0, M.length);
	// buf = y2.toByteArray();
	// digest.update(buf, 0, buf.length);
	//
	// buf = new byte[DIGEST_LENGTH];
	// digest.doFinal(buf, 0);
	//
	// return buf;
	// }

	/**
	 * �ж��Ƿ��ڷ�Χ��
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
	 * �ж����ɵĹ�Կ�Ƿ�Ϸ�
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
	 * ������Կ��
	 *
	 * @return
	 */
	public SM2KeyPair generateKeyPair() {

		BigInteger d = random(n.subtract(new BigInteger("1")));

		SM2KeyPair keyPair = new SM2KeyPair(G.multiply(d).normalize(), d);

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

	@SuppressWarnings("deprecation")
	public SM2() {
		curve = new ECCurve.Fp(p, // q
				a, // a
				b); // b
		G = curve.createPoint(gx, gy);
		ecc_bc_spec = new ECDomainParameters(curve, G, n);
	}

	public SM2(boolean debug) {
		this();
		this.debug = debug;
	}

	/**
	 * ������Կ������
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
	 * �ӱ��ص��빫Կ
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
			return curve.decodePoint(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����˽Կ������
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
	 * �ӱ��ص���˽Կ
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
	 * �ֽ�����ƴ��
	 *
	 * @param params
	 * @return
	 */
	private static byte[] join(byte[]... params) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] res = null;
		try {
			for (int i = 0; i < params.length; i++) {
				baos.write(params[i]);
			}
			res = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * sm3ժҪ
	 *
	 * @param params
	 * @return
	 */
	private static byte[] sm3hash(byte[]... params) {
		byte[] res = null;
		try {
			res = SM3.hash(join(params));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * ȡ���û���ʶ�ֽ�����
	 *
	 * @param IDA
	 * @param aPublicKey
	 * @return
	 */
	private static byte[] ZA(String IDA, ECPoint aPublicKey) {
		byte[] idaBytes = IDA.getBytes();
		int entlenA = idaBytes.length * 8;
		byte[] ENTLA = new byte[] { (byte) (entlenA & 0xFF00), (byte) (entlenA & 0x00FF) };
		byte[] ZA = sm3hash(ENTLA, idaBytes, a.toByteArray(), b.toByteArray(), gx.toByteArray(), gy.toByteArray(),
				aPublicKey.getXCoord().toBigInteger().toByteArray(),
				aPublicKey.getYCoord().toBigInteger().toByteArray());
		return ZA;
	}

	/**
	 * ǩ��
	 *
	 * @param M
	 *            ǩ����Ϣ
	 * @param IDA
	 *            ǩ����Ψһ��ʶ
	 * @param keyPair
	 *            ǩ������Կ��
	 * @return ǩ��
	 */
	public Signature sign(String M, String IDA, SM2KeyPair keyPair) {
		byte[] ZA = ZA(IDA, keyPair.getPublicKey());
		byte[] M_ = join(ZA, M.getBytes());
		BigInteger e = new BigInteger(1, sm3hash(M_));
		// BigInteger k = new BigInteger(
		// "6CB28D99 385C175C 94F94E93 4817663F C176D925 DD72B727 260DBAAE
		// 1FB2F96F".replace(" ", ""), 16);
		BigInteger k;
		BigInteger r;
		do {
			k = random(n);
			ECPoint p1 = G.multiply(k).normalize();
			BigInteger x1 = p1.getXCoord().toBigInteger();
			r = e.add(x1);
			r = r.mod(n);
		} while (r.equals(BigInteger.ZERO) || r.add(k).equals(n));

		BigInteger s = ((keyPair.getPrivateKey().add(BigInteger.ONE).modInverse(n))
				.multiply((k.subtract(r.multiply(keyPair.getPrivateKey()))).mod(n))).mod(n);

		return new Signature(r, s);
	}

	/**
	 * ��ǩ
	 *
	 * @param M
	 *            ǩ����Ϣ
	 * @param signature
	 *            ǩ��
	 * @param IDA
	 *            ǩ����Ψһ��ʶ
	 * @param aPublicKey
	 *            ǩ������Կ
	 * @return true or false
	 */
	public boolean verify(String M, Signature signature, String IDA, ECPoint aPublicKey) {
		if (!between(signature.r, BigInteger.ONE, n))
			return false;
		if (!between(signature.s, BigInteger.ONE, n))
			return false;

		byte[] M_ = join(ZA(IDA, aPublicKey), M.getBytes());
		BigInteger e = new BigInteger(1, sm3hash(M_));
		BigInteger t = signature.r.add(signature.s).mod(n);

		if (t.equals(BigInteger.ZERO))
			return false;

		ECPoint p1 = G.multiply(signature.s).normalize();
		ECPoint p2 = aPublicKey.multiply(t).normalize();
		BigInteger x1 = p1.add(p2).normalize().getXCoord().toBigInteger();
		BigInteger R = e.add(x1).mod(n);
		if (R.equals(signature.r))
			return true;
		return false;
	}

	/**
	 * ��Կ��������
	 *
	 * @param Z
	 * @param klen
	 *            ����klen�ֽ������ȵ���Կ
	 * @return
	 */
	private static byte[] KDF(byte[] Z, int klen) {
		int ct = 1;
		int end = (int) Math.ceil(klen * 1.0 / 32);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			for (int i = 1; i < end; i++) {
				baos.write(sm3hash(Z, SM3.toByteArray(ct)));
				ct++;
			}
			byte[] last = sm3hash(Z, SM3.toByteArray(ct));
			if (klen % 32 == 0) {
				baos.write(last);
			} else
				baos.write(last, 0, klen % 32);
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ����ʵ����
	 *
	 * @author Potato
	 *
	 */
	private static class TransportEntity implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final byte[] R; // R��
		final byte[] S; // ��֤S
		final byte[] Z; // �û���ʶ
		final byte[] K; // ��Կ

		public TransportEntity(byte[] r, byte[] s, byte[] z, ECPoint pKey) {
			R = r;
			S = s;
			Z = z;
			K = pKey.getEncoded(false);
		}
	}

	/**
	 * ��ԿЭ�̸�����
	 *
	 * @author Potato
	 *
	 */
	public static class KeyExchange {
		BigInteger rA;
		ECPoint RA;
		ECPoint V;
		byte[] Z;
		byte[] key;

		String ID;
		SM2KeyPair keyPair;

		public KeyExchange(String ID, SM2KeyPair keyPair) {
			this.ID = ID;
			this.keyPair = keyPair;
			this.Z = ZA(ID, keyPair.getPublicKey());
		}

		/**
		 * ��ԿЭ�̷����һ��
		 *
		 * @return
		 */
		public TransportEntity keyExchange_1() {
			rA = random(n);
			// rA=new BigInteger("83A2C9C8 B96E5AF7 0BD480B4 72409A9A 327257F1
			// EBB73F5B 073354B2 48668563".replace(" ", ""),16);
			RA = G.multiply(rA).normalize();
			return new TransportEntity(RA.getEncoded(false), null, Z, keyPair.getPublicKey());
		}

		/**
		 * ��ԿЭ����Ӧ��
		 *
		 * @param entity
		 *            ����ʵ��
		 * @return
		 */
		public TransportEntity keyExchange_2(TransportEntity entity) {
			BigInteger rB = random(n);
			// BigInteger rB=new BigInteger("33FE2194 0342161C 55619C4A 0C060293
			// D543C80A F19748CE 176D8347 7DE71C80".replace(" ", ""),16);
			ECPoint RB = G.multiply(rB).normalize();

			this.rA = rB;
			this.RA = RB;

			BigInteger x2 = RB.getXCoord().toBigInteger();
			x2 = _2w.add(x2.and(_2w.subtract(BigInteger.ONE)));

			BigInteger tB = keyPair.getPrivateKey().add(x2.multiply(rB)).mod(n);
			ECPoint RA = curve.decodePoint(entity.R).normalize();

			BigInteger x1 = RA.getXCoord().toBigInteger();
			x1 = _2w.add(x1.and(_2w.subtract(BigInteger.ONE)));

			ECPoint aPublicKey = curve.decodePoint(entity.K).normalize();
			ECPoint temp = aPublicKey.add(RA.multiply(x1).normalize()).normalize();
			ECPoint V = temp.multiply(ecc_bc_spec.getH().multiply(tB)).normalize();
			if (V.isInfinity())
				throw new IllegalStateException();
			this.V = V;

			byte[] xV = V.getXCoord().toBigInteger().toByteArray();
			byte[] yV = V.getYCoord().toBigInteger().toByteArray();
			byte[] KB = KDF(join(xV, yV, entity.Z, this.Z), 16);
			key = KB;
			System.out.print("Э�̵�B��Կ:");
			printHexString(KB);
			byte[] sB = sm3hash(new byte[] { 0x02 }, yV,
					sm3hash(xV, entity.Z, this.Z, RA.getXCoord().toBigInteger().toByteArray(),
							RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
							RB.getYCoord().toBigInteger().toByteArray()));
			return new TransportEntity(RB.getEncoded(false), sB, this.Z, keyPair.getPublicKey());
		}

		/**
		 * ��ԿЭ�̷��𷽵ڶ���
		 *
		 * @param entity
		 *            ����ʵ��
		 */
		public TransportEntity keyExchange_3(TransportEntity entity) {
			BigInteger x1 = RA.getXCoord().toBigInteger();
			x1 = _2w.add(x1.and(_2w.subtract(BigInteger.ONE)));

			BigInteger tA = keyPair.getPrivateKey().add(x1.multiply(rA)).mod(n);
			ECPoint RB = curve.decodePoint(entity.R).normalize();

			BigInteger x2 = RB.getXCoord().toBigInteger();
			x2 = _2w.add(x2.and(_2w.subtract(BigInteger.ONE)));

			ECPoint bPublicKey = curve.decodePoint(entity.K).normalize();
			ECPoint temp = bPublicKey.add(RB.multiply(x2).normalize()).normalize();
			ECPoint U = temp.multiply(ecc_bc_spec.getH().multiply(tA)).normalize();
			if (U.isInfinity())
				throw new IllegalStateException();
			this.V = U;

			byte[] xU = U.getXCoord().toBigInteger().toByteArray();
			byte[] yU = U.getYCoord().toBigInteger().toByteArray();
			byte[] KA = KDF(join(xU, yU, this.Z, entity.Z), 16);
			key = KA;
			System.out.print("Э�̵�A��Կ:");
			printHexString(KA);
			byte[] s1 = sm3hash(new byte[] { 0x02 }, yU,
					sm3hash(xU, this.Z, entity.Z, RA.getXCoord().toBigInteger().toByteArray(),
							RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
							RB.getYCoord().toBigInteger().toByteArray()));
			if (Arrays.equals(entity.S, s1))
				System.out.println("B->A ��Կȷ�ϳɹ�");
			else
				System.out.println("B->A ��Կȷ��ʧ��");
			byte[] sA = sm3hash(new byte[] { 0x03 }, yU,
					sm3hash(xU, this.Z, entity.Z, RA.getXCoord().toBigInteger().toByteArray(),
							RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
							RB.getYCoord().toBigInteger().toByteArray()));

			return new TransportEntity(RA.getEncoded(false), sA, this.Z, keyPair.getPublicKey());
		}

		/**
		 * ��Կȷ�����һ��
		 *
		 * @param entity
		 *            ����ʵ��
		 */
		public void keyExchange_4(TransportEntity entity) {
			byte[] xV = V.getXCoord().toBigInteger().toByteArray();
			byte[] yV = V.getYCoord().toBigInteger().toByteArray();
			ECPoint RA = curve.decodePoint(entity.R).normalize();
			byte[] s2 = sm3hash(new byte[] { 0x03 }, yV,
					sm3hash(xV, entity.Z, this.Z, RA.getXCoord().toBigInteger().toByteArray(),
							RA.getYCoord().toBigInteger().toByteArray(),
							this.RA.getXCoord().toBigInteger().toByteArray(),
							this.RA.getYCoord().toBigInteger().toByteArray()));
			if (Arrays.equals(entity.S, s2))
				System.out.println("A->B ��Կȷ�ϳɹ�");
			else
				System.out.println("A->B ��Կȷ��ʧ��");
		}
	}
	
	
	 /**
     * @Description ��Կ�ַ���ת��Ϊ BCECPublicKey ��Կ����
     * @Author msx
     * @param pubKeyHex 64�ֽ�ʮ�����ƹ�Կ�ַ���(�����Կ�ַ���Ϊ65�ֽ��׸��ֽ�Ϊ0x04����ʾ�ù�ԿΪ��ѹ����ʽ������ʱ��Ҫɾ��)
     * @return BCECPublicKey SM2��Կ����
     */
    public static BCECPublicKey getECPublicKeyByPublicKeyHex(String pubKeyHex) {
        //��ȡ64�ֽ���Ч��SM2��Կ�������Կ�׸��ֽ�Ϊ0x04��
        if (pubKeyHex.length() > 128) {
            pubKeyHex = pubKeyHex.substring(pubKeyHex.length() - 128);
        }
        //����Կ���Ϊx,y��������32�ֽڣ�
        String stringX = pubKeyHex.substring(0, 64);
        String stringY = pubKeyHex.substring(stringX.length());
        //����Կx��y����ת��ΪBigInteger����
        BigInteger x = new BigInteger(stringX, 16);
        BigInteger y = new BigInteger(stringY, 16);
        //ͨ����Կx��y����������Բ���߹�Կ�淶
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(x9ECParameters.getCurve().createPoint(x, y), ecDomainParameters);
        //ͨ����Բ���߹�Կ�淶����������Բ���߹�Կ���󣨿�����SM2���ܼ���ǩ��
        return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }
  
  
    
   
	public static void main(String[] args) throws UnsupportedEncodingException {
         
		SM2 sm02 = new SM2();
		sm02.setDebug(false);
		//SM2KeyPair keyPair = sm02.generateKeyPair();
			
		
        //sm02.exportPublicKey(keyPair.getPublicKey(), "D:/publickey.pem");
        //sm02.exportPrivateKey(keyPair.getPrivateKey(), "D:/privatekey.pem");
		System.out.println("-----------------��Կ���������-----------------");
		 // ��Կǰ���02����03��ʾ��ѹ����Կ,04��ʾδѹ����Կ,04��ʱ��,����ȥ��ǰ���04
		ECPoint publicKey = sm02.importPublicKey("D:/publickey.pem");
		BigInteger privateKey = sm02.importPrivateKey("D:/privatekey.pem");
		/*����1*/
		SM2KeyPair keyPair = new SM2KeyPair(publicKey,privateKey);
		
	  
	
	    ECPoint publicKey1 = SM2.curve.decodePoint(Hex.decode(keyPair.getPubHexInSoft()));
	
	    keyPair.setPublicKey(publicKey1);
	    
				
	
		
		Util.byteToHex(publicKey.getEncoded(true));

		System.out.println("kp_publickeyHex="+keyPair.getPubHexInSoft());
        System.out.println("public_keyHex1="+Util.byteToHex(publicKey.getEncoded(true)));
		System.out.println("kp_private_keyHex="+keyPair.getPriHexInSoft());
		System.out.println("private_keyHex="+Util.byteToHex(privateKey.toByteArray()));
		byte[] data = sm02.encrypt("���Լ���aaaaaaaaa3333**����%��aa��ѩ��123aabb", publicKey);
		System.out.print("����:");
		SM2.printHexString(data);
		System.out.println("���ܺ�����:" + sm02.decrypt(data, privateKey));
		
		System.out.print("˽Կ����:");
		System.out.println("private_keyHex="+keyPair.getPriHexInSoft());
		
		String dataHex ="04702bfb33f8121837603e135ba345d9292b673fac38840933da1dcf528a44273b67d85497f83ff78459fde4233ebab2462f7832f4e86640ad0807973db4e3e070fde4e6d2e20c6baad2997e42762c3ffe4adb8cc1f57439a5dd066d5b6e4ae611a8ab70bea4a0a8102cd8f50db998717f468f4359d77736805bc77d3b86498e80cd8c9421359c89a6faf37d4ad430f4c83240bd859f619e830e4c71ca31c9cf14fcae5a2f120f9143";
		data = Util.hexStringToBytes(dataHex);
		System.out.print("����:");
		System.out.println(dataHex);
		System.out.println("���ܺ�����:" + sm02.decrypt(data, privateKey));
		System.out.println("-----------------ǩ������ǩ-----------------");
		String IDA = "Heartbeats";
		String M = "Ҫǩ������Ϣ";
		Signature signature = sm02.sign(M, IDA, new SM2KeyPair(publicKey, privateKey));
		System.out.println("�û���ʶ:" + IDA);
		System.out.println("ǩ����Ϣ:" + M);
		System.out.println("����ǩ��:" + signature);
		System.out.println("��֤ǩ��:" + sm02.verify(M, signature, IDA, publicKey));
		//publikeyHex =04c0c0ed67489c524027f66975b21419a3ff43781db54335cd5b38557843498cbd262b1e2ac978d1aedc37aacaa93cd69fd3241ce9d8e4d1429118bc1f1f0af926
		System.out.println("-----------------��ԿЭ��-----------------");
		String aID = "AAAAAAAAAAAAA";
		SM2KeyPair aKeyPair = sm02.generateKeyPair();
		KeyExchange aKeyExchange = new KeyExchange(aID, aKeyPair);

		String bID = "BBBBBBBBBBBBB";
		SM2KeyPair bKeyPair = sm02.generateKeyPair();
		KeyExchange bKeyExchange = new KeyExchange(bID, bKeyPair);
		TransportEntity entity1 = aKeyExchange.keyExchange_1();
		TransportEntity entity2 = bKeyExchange.keyExchange_2(entity1);
		TransportEntity entity3 = aKeyExchange.keyExchange_3(entity2);
		bKeyExchange.keyExchange_4(entity3);
	}

	public static class Signature {
		BigInteger r;
		BigInteger s;

		public Signature(BigInteger r, BigInteger s) {
			this.r = r;
			this.s = s;
		}

		public String toString() {
			return r.toString(16) + "," + s.toString(16);
		}
	}
}