import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends SocketConnected{

    private String encryptedAESKey;
    ServerSocket server = null;
    ServerSocket fileServer = null;

    public Server() {

        rsa = new RSA();
        rsa.init();
        aes = new AES();
        String publicKeyString = rsa.getPublicKey();
        try {
            this.server = new ServerSocket(8999);
            this.fileServer = new ServerSocket(9989);
            System.out.println("연결 대기 중 ...");
            this.socket = this.server.accept();
            System.out.println("연결 완료!");
            System.out.println("파일 소켓 연결 대기 중...");
            fileSocket = fileServer.accept();
            System.out.println("파일 소켓 연결 완료!");

            // sending rsa public key - not encrypted
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(publicKeyString + "\n");
            out.flush();

            while(encryptedAESKey == null) {
                // receiving aes private key
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                encryptedAESKey = in.readLine().replace("\n", "");
                String decryptedAESKey = rsa.decrypt(encryptedAESKey);
                aes.initFromStrings(decryptedAESKey);

//                System.out.println("public key string : " + publicKeyString);
//                System.out.println("aes key string : " + decryptedAESKey);
            }

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

