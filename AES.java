import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES {
    private SecretKey key;
    private byte[] VI;
    private final int KEY_SIZE = 256;
    private final int T_LEN = 128;

    public void init(){
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(KEY_SIZE);
            key = generator.generateKey();
            VI = getKey().substring(0, 16).getBytes();
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void initFromStrings(String key){
        this.key = new SecretKeySpec(decode(key), "AES");
        this.VI = getKey().substring(0, 16).getBytes();
    }

    public String getKey() {
        return encode(key.getEncoded());
    }

    public String encrypt(String message) throws Exception{

        byte[] messageToByte = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, VI);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encryptedBytes = encryptionCipher.doFinal(messageToByte);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception{
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, VI);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted, "UTF-8");
    }
    public String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }


}
