import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;




public class RSA {

    private PrivateKey privateKey;
    private PublicKey publicKey;


    public void init(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch (Exception ignored){}
    }

    public void initFromStrings(String publicKeyString){
        try {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(publicKeyString));

            KeyFactory factory = KeyFactory.getInstance("RSA");
            publicKey = factory.generatePublic(keySpecPublic);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void printKeys(){
        System.out.println("public key :  \n"+ encode(this.publicKey.getEncoded()));
        System.out.println("private key : \n"+ encode(this.privateKey.getEncoded()));
    }

    public String encrypt(String message) throws Exception{
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes );
        return encode(encryptedBytes);
    }



    public String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    public String decrypt(String encryptedMessage) throws Exception{
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, "UTF-8");
    }

    public byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }

    public String getPublicKey(){
        return encode(publicKey.getEncoded());
    }

}
