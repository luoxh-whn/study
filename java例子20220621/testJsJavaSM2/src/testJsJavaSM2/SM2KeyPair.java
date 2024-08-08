package testJsJavaSM2;


public class SM2KeyPair {

    /** ��Կ */
    private  String publicKey;

    /** ˽Կ */
    private String privateKey;

    SM2KeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

}
