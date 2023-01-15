import java.io.*;
import java.net.Socket;


public class Client extends SocketConnected {



    public Client() {
        rsa = new RSA();
        aes = new AES();
        aes.init();

        try {
            System.out.println("연결 대기 중 ...");
            socket = new Socket("127.0.0.1", 8999);
            System.out.println("연결 완료!");
            System.out.println("파일 소켓 연결 대기 중 ...");
            fileSocket = new Socket("127.0.0.1", 9989);
            System.out.println("파일 소켓 연결 완료!");

            while(publicKeyString == null) {
                // receiving rsa public key
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                publicKeyString = in.readLine().replace("\n", "");
                rsa.initFromStrings(publicKeyString);
            }

            // sending aes private key
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String AESKeyString = aes.getKey();
            String RSAEncryptedAESKeyString = rsa.encrypt(AESKeyString);

//            System.out.println("public key string : " + publicKeyString);
//            System.out.println("aes key string : " + AESKeyString);

            out.write(RSAEncryptedAESKeyString + "\n");
            out.flush();


            //listening and sending
            thread1.start();
            thread2.start();
            thread3 = new Thread(new FileReceiver(fileSocket, this), "fileIn");
            thread3.start();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}
