package testJsJavaSM2;


public class SM2KeyPair {

    /** ¹«Ô¿ */
    private  String publicKey;

    /** Ë½Ô¿ */
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
